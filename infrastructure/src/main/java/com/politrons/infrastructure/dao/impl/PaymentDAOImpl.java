package com.politrons.infrastructure.dao.impl;

import com.datastax.driver.core.ResultSet;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.politrons.infrastructure.CassandraConnector;
import com.politrons.infrastructure.dao.PaymentDAO;
import com.politrons.infrastructure.events.PaymentAdded;
import io.vavr.API;
import io.vavr.concurrent.Future;
import io.vavr.control.Either;
import io.vavr.control.Try;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.time.Instant;

import static io.vavr.API.*;
import static io.vavr.Patterns.$Failure;
import static io.vavr.Patterns.$Success;

@ApplicationScoped
public class PaymentDAOImpl implements PaymentDAO {

    private ObjectMapper mapper = new ObjectMapper();

    @PostConstruct
    public void initConnector() {
        CassandraConnector.start();
    }

    @Override
    public Future<Either<Throwable, String>> addPayment(PaymentAdded paymentAdded) {
        return Match(Try(() -> mapper.writeValueAsString(paymentAdded))
                .map(event -> Future.of(() -> CassandraConnector.addPayment(getAddPaymentQuery(paymentAdded, getTimestampMillis(), event)))
                        .map(maybeResultSet -> transformResultSet(maybeResultSet, paymentAdded.getId())))).of(
                Case($Success($()), future -> future),
                Case($Failure($()), throwable -> Future.of(() -> Left(throwable))));
    }

    private String getTimestampMillis() {
        Instant instant = Instant.now();
        return String.valueOf(instant.toEpochMilli());
    }

    private Either<Throwable, String> transformResultSet(Try<ResultSet> maybeResultSet, String id) {
        return Match(maybeResultSet).of(
                Case($Success($()), resultSet -> Right(id)),
                Case($Failure($()), API::Left));
    }

    private String getAddPaymentQuery(PaymentAdded paymentAdded, String timeStampMillis, String event) {
        return "INSERT INTO " + "paymentsSchema.payment" +
                "(id, timestamp, event) " +
                "VALUES (" + paymentAdded.getId() + ", '" +
                timeStampMillis + "', '" +
                event + "');";
    }

}
