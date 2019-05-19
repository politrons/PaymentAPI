package com.politrons.infrastructure.dao;

import com.politrons.domain.PaymentStateAggregateRoot;
import com.politrons.infrastructure.events.PaymentAdded;
import com.politrons.infrastructure.events.PaymentDeleted;
import com.politrons.infrastructure.events.PaymentUpdated;
import io.vavr.concurrent.Future;
import io.vavr.control.Either;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public interface PaymentDAO {

    Future<Either<Throwable,String>> persistPaymentAddedEvent(PaymentAdded paymentAdded);

    Future<Either<Throwable, String>> persistPaymentUpdatedEvent(PaymentUpdated paymentUpdated);

    Future<Either<Throwable, String>> persistPaymentDeletedEvent(PaymentDeleted paymentAdded);

    Future<Either<Throwable, PaymentStateAggregateRoot>> fetchPayment(String id);

    Future<Either<Throwable, List<PaymentStateAggregateRoot>>> fetchAllPayments();
}
