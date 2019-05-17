package com.politrons.application.service;

import io.vavr.concurrent.Future;
import io.vavr.control.Either;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface PaymentService {

    Future<Either<Throwable, String>> findPayment(String id);
}
