package com.politrons.domain.repository;

import com.politrons.domain.PaymentAggregateRoot;
import io.vavr.concurrent.Future;
import io.vavr.control.Either;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface PaymentRepository {

    Future<Either<Throwable, String>> addPayment(PaymentAggregateRoot paymentAggregateRoot);

}
