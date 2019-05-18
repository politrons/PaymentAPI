package com.politrons.infrastructure.dao;

import com.politrons.domain.PaymentStateAggregateRoot;
import com.politrons.infrastructure.events.PaymentAdded;
import io.vavr.concurrent.Future;
import io.vavr.control.Either;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface PaymentDAO {

    Future<Either<Throwable,String>> addPayment(PaymentAdded paymentAdded);

    Future<Either<Throwable, PaymentStateAggregateRoot>> fetchPayment(String id);
}
