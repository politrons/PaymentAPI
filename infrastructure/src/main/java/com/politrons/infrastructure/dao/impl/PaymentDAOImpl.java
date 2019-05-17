package com.politrons.infrastructure.dao.impl;

import com.politrons.infrastructure.dao.PaymentDAO;
import com.politrons.infrastructure.events.PaymentAdded;
import io.vavr.concurrent.Future;
import io.vavr.control.Either;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.CompletableFuture;

import static io.vavr.API.Right;

@ApplicationScoped
public class PaymentDAOImpl implements PaymentDAO {


    @Override
    public Future<Either<Throwable, String>> addPayment(PaymentAdded paymentAdded) {
        return Future.of(() -> Right("1981"));
    }
}
