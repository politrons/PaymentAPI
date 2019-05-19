package com.politrons.infrastructure.repository.impl;

import com.politrons.domain.PaymentStateAggregateRoot;
import com.politrons.infrastructure.events.PaymentDeleted;
import com.politrons.infrastructure.events.PaymentEvent;
import com.politrons.infrastructure.events.PaymentUpdated;
import com.politrons.infrastructure.repository.PaymentRepository;
import com.politrons.infrastructure.dao.PaymentDAO;
import com.politrons.infrastructure.events.PaymentAdded;
import io.vavr.concurrent.Future;
import io.vavr.control.Either;
import lombok.NoArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@NoArgsConstructor
@ApplicationScoped
public class PaymentRepositoryImpl implements PaymentRepository {

    @Inject
    PaymentDAO paymentDAO;

    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    PaymentRepositoryImpl(PaymentDAO paymentDAO) {
        this.paymentDAO = paymentDAO;
    }

    /**
     * Method responsible for the transformation from the AggregateRoot of the domain layer
     * into the PaymentAdded Event to be persisted.
     *
     * @param paymentStateAggregateRoot domain model to be transform into event.
     * @return The id of the transaction for futures fetch.
     */
    @Override
    public Future<Either<Throwable, String>> addPayment(PaymentStateAggregateRoot paymentStateAggregateRoot) {
        PaymentAdded paymentAdded = transformAggregateRootIntoEvent(paymentStateAggregateRoot, PaymentAdded.class);
        return paymentDAO.persistPaymentAddedEvent(paymentAdded);
    }

    /**
     * Method responsible for the transformation from the AggregateRoot of the domain layer
     * into the PaymentUpdated Event to be persisted.
     *
     * @param paymentStateAggregateRoot domain model to be transform into event.
     * @return The id of the transaction for futures fetch.
     */
    @Override
    public Future<Either<Throwable, String>> updatePayment(PaymentStateAggregateRoot paymentStateAggregateRoot) {
        PaymentUpdated paymentUpdated = transformAggregateRootIntoEvent(paymentStateAggregateRoot, PaymentUpdated.class);
        return paymentDAO.persistPaymentUpdatedEvent(paymentUpdated);
    }

    /**
     * Method responsible for the transformation from the AggregateRoot of the domain layer
     * into the PaymentDeleted Event to be persisted.
     *
     * @param paymentStateAggregateRoot domain model to be transform into event.
     * @return The id of the transaction for futures fetch.
     */
    @Override
    public Future<Either<Throwable, String>> deletePayment(PaymentStateAggregateRoot paymentStateAggregateRoot) {
        PaymentDeleted paymentDeleted = transformAggregateRootIntoEvent(paymentStateAggregateRoot, PaymentDeleted.class);
        return paymentDAO.persistPaymentDeletedEvent(paymentDeleted);
    }

    /**
     * Proxy method to be used from handler by delete Payment to get the payment first from the Database.
     *
     * @param id of the eventId
     * @return The PaymentStateAggregateRoot to change state as deleted and create a new Event.
     */
    @Override
    public Future<Either<Throwable, PaymentStateAggregateRoot>> fetchPayment(String id) {
        return paymentDAO.fetchPayment(id);
    }

    private <T extends PaymentEvent> T transformAggregateRootIntoEvent(PaymentStateAggregateRoot paymentStateAggregateRoot,
                                                                       Class<? extends PaymentEvent> paymentEventClass) {
        mapperFactory.classMap(PaymentStateAggregateRoot.class, paymentEventClass);
        MapperFacade mapper = mapperFactory.getMapperFacade();
        return (T) mapper.map(paymentStateAggregateRoot, paymentEventClass);

    }

}
