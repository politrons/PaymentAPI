package com.politrons.infrastructure.repository.impl;

import com.politrons.domain.PaymentStateAggregateRoot;
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
     * Function responsible from the transformation from the AggregateRoot of the domain layer
     * into the Event to be persisted.
     *
     * @param paymentStateAggregateRoot domain model to be transform into event.
     * @return The id of the transaction for futures fetch.
     */
    @Override
    public Future<Either<Throwable, String>> persistPayment(PaymentStateAggregateRoot paymentStateAggregateRoot) {
        mapperFactory.classMap(PaymentStateAggregateRoot.class, PaymentAdded.class);
        MapperFacade mapper = mapperFactory.getMapperFacade();
        PaymentAdded paymentAdded = mapper.map(paymentStateAggregateRoot, PaymentAdded.class);
        return paymentDAO.upsertPayment(paymentAdded);
    }

    /**
     * Proxy method to be used from handler by delete Payment to get the payment first from the Database.
     * @param id of the eventId
     * @return The PaymentStateAggregateRoot to change state as deleted and create a new Event.
     */
    @Override
    public Future<Either<Throwable, PaymentStateAggregateRoot>> fetchPayment(String id) {
        return paymentDAO.fetchPayment(id);
    }

}
