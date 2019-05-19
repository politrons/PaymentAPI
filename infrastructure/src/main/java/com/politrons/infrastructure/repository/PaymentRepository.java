package com.politrons.infrastructure.repository;

import com.politrons.domain.PaymentStateAggregateRoot;
import io.vavr.concurrent.Future;
import io.vavr.control.Either;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface PaymentRepository {

    Future<Either<Throwable, String>> addPayment(PaymentStateAggregateRoot paymentStateAggregateRoot);

    Future<Either<Throwable, String>> updatePayment(PaymentStateAggregateRoot paymentStateAggregateRoot);

    Future<Either<Throwable, String>> deletePayment(PaymentStateAggregateRoot paymentStateAggregateRoot);

    Future<Either<Throwable, PaymentStateAggregateRoot>> fetchPayment(String id);

}
