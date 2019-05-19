package com.politrons.infrastructure.dao.impl;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.politrons.domain.PaymentStateAggregateRoot;
import com.politrons.infrastructure.CassandraConnector;
import com.politrons.infrastructure.dao.PaymentDAO;
import com.politrons.infrastructure.events.PaymentAdded;
import com.politrons.infrastructure.events.PaymentDeleted;
import com.politrons.infrastructure.events.PaymentEvent;
import com.politrons.infrastructure.events.PaymentUpdated;
import io.vavr.API;
import io.vavr.concurrent.Future;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static io.vavr.API.*;
import static io.vavr.Patterns.$Failure;
import static io.vavr.Patterns.$Success;

@ApplicationScoped
public class PaymentDAOImpl implements PaymentDAO {

    private Logger logger = LoggerFactory.getLogger(PaymentDAOImpl.class);

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public Future<Either<Throwable, String>> persistPaymentAddedEvent(PaymentAdded paymentAdded) {
        return upsertPayment(paymentAdded);
    }

    @Override
    public Future<Either<Throwable, String>> persistPaymentUpdatedEvent(PaymentUpdated paymentUpdated) {
        return upsertPayment(paymentUpdated);
    }

    @Override
    public Future<Either<Throwable, String>> persistPaymentDeletedEvent(PaymentDeleted paymentAdded) {
        return upsertPayment(paymentAdded);
    }

    /**
     * Method to find the Payment event in json format and transform into [PaymentStateAggregateRoot]
     *
     * @param id of the event
     * @return PaymentStateAggregateRoot
     */
    @Override
    public Future<Either<Throwable, PaymentStateAggregateRoot>> fetchPayment(String id) {
        return Future.of(() -> CassandraConnector.fetchPayment(fetchPaymentByIdQuery(id)))
                .map(this::transformResultSetToPaymentAggregateRoot);
    }

    @Override
    public Future<Either<Throwable, List<PaymentStateAggregateRoot>>> fetchAllPayments() {
        return Future.of(() -> CassandraConnector.fetchAllPayments(fetchAllPaymentsQuery()))
                .map(this::transformResultSetToPaymentAggregateRoots);
    }

    /**
     * Function to receive the event [PaymentEvent] and we persist into Cassandra row adding the UUID, timestamp and Event in json format.
     * We are doing Event sourcing, which means the internal id the of event it will remain intact for future rehydrate, and we will create a new UUID per
     * transaction inserted in Cassandra.
     *
     * @param paymentEvent Event to be transform into json and being persisted.
     * @return the id of the row
     */
    private Future<Either<Throwable, String>> upsertPayment(PaymentEvent paymentEvent) {
        String uuid = createNewUUID();
        return Match(Try(() -> mapper.writeValueAsString(paymentEvent))
                .flatMap(event -> Try.of(() -> getAddPaymentQuery(uuid, getTimestampMillis(), event))
                        .map(query -> Future.of(() -> CassandraConnector.addPayment(query))
                                .map(maybeResultSet -> transformResultSetToId(maybeResultSet, uuid))))).of(
                Case($Success($()), future -> future),
                Case($Failure($()), throwable -> {
                    logger.error("Error in add payment DAO mapping event to json. Caused by:" + throwable.getCause());
                    return Future.of(() -> Left(throwable));
                }));
    }

    private Either<Throwable, List<PaymentStateAggregateRoot>> transformResultSetToPaymentAggregateRoots(Try<ResultSet> maybeResultSet) {
        return Match(maybeResultSet).of(
                Case($Success($()), rs -> Right(getPaymentStateAggregateRoots(rs))),
                Case($Failure($()), throwable -> {
                    logger.error("Error in fetch payment DAO transforming ResultSet. Caused by:" + throwable.getCause());
                    return Left(throwable);
                }));
    }

    private List<PaymentStateAggregateRoot> getPaymentStateAggregateRoots(ResultSet rs) {
        return rs.all().stream()
                .map(this::transformIntoPaymentStateAggregateRoot)
                .collect(Collectors.toList());
    }

    private PaymentStateAggregateRoot transformIntoPaymentStateAggregateRoot(Row row) {
        return Match(Try.of(() -> mapper.readValue(row.getString("event"), PaymentStateAggregateRoot.class))).of(
                Case($Success($()), paymentStateAggregateRoot -> paymentStateAggregateRoot),
                Case($Failure($()), throwable -> null));
    }

    /**
     * Function to transform from the Try monad of ResultSet into The id of the transaction.
     *
     * @param maybeResultSet monad with maybe the resultSet or a Throwable
     * @param id             of the row
     * @return id of the event
     */
    private Either<Throwable, String> transformResultSetToId(Try<ResultSet> maybeResultSet, String id) {
        return Match(maybeResultSet).of(
                Case($Success($()), resultSet -> Right(id)),
                Case($Failure($()), throwable -> {
                    logger.error("Error in add payment DAO transforming ResultSet. Caused by:" + throwable.getCause());
                    return Left(throwable);
                }));
    }

    /**
     * Function to transform from the Try monad of ResultSet into The PaymentStateAggregateRoot of the transaction.
     *
     * @param maybeResultSet monad with maybe the resultSet or a Throwable
     */
    private Either<Throwable, PaymentStateAggregateRoot> transformResultSetToPaymentAggregateRoot(Try<ResultSet> maybeResultSet) {
        return Match(maybeResultSet).of(
                Case($Success($()), this::processResultSet),
                Case($Failure($()), throwable -> {
                    logger.error("Error in fetch payment DAO transforming ResultSet. Caused by:" + throwable.getCause());
                    return Left(throwable);
                }));
    }

    private Either<Throwable, PaymentStateAggregateRoot> processResultSet(ResultSet resultSet) {
        return Match(Try.of(() -> mapper.readValue(resultSet.one().getString("event"), PaymentStateAggregateRoot.class))).of(
                Case($Success($()), API::Right),
                Case($Failure($()), throwable -> {
                    logger.error("Error in add payment DAO transforming ResultSet. Caused by:" + throwable.getCause());
                    return Left(throwable);
                }));
    }

    private String getAddPaymentQuery(String id, String timeStampMillis, String event) {
        if (event == null || event.isEmpty() || event.equals("null")) throw new IllegalArgumentException();
        return "INSERT INTO " + "paymentsSchema.payment" +
                "(id, timestamp, event) " +
                "VALUES (" + id + ", '" +
                timeStampMillis + "', '" +
                event + "');";
    }

    private String fetchPaymentByIdQuery(String id) {
        return "SELECT * FROM paymentsSchema.payment WHERE id=" + id + " ";
    }

    private String fetchAllPaymentsQuery() {
        return "SELECT * FROM paymentsSchema.payment";
    }


    private String getTimestampMillis() {
        Instant instant = Instant.now();
        return String.valueOf(instant.toEpochMilli());
    }

    private String createNewUUID() {
        return UUID.randomUUID().toString();
    }

}
