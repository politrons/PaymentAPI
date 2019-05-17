package com.politrons.infrastructure.repository.impl;

import com.politrons.domain.PaymentAggregateRoot;
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

    public PaymentRepositoryImpl(PaymentDAO paymentDAO) {
        this.paymentDAO =paymentDAO;
    }

    @Override
    public Future<Either<Throwable, String>> addPayment(PaymentAggregateRoot paymentAggregateRoot) {
        mapperFactory.classMap(PaymentAggregateRoot.class, PaymentAdded.class);
        MapperFacade mapper = mapperFactory.getMapperFacade();
        PaymentAdded paymentAdded = mapper.map(paymentAggregateRoot, PaymentAdded.class);
        return paymentDAO.addPayment(paymentAdded);
    }

}
