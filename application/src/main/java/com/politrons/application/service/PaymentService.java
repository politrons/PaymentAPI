package com.politrons.application.service;

import com.politrons.application.model.error.ErrorPayload;
import com.politrons.application.model.payload.response.PaymentResponse;
import com.politrons.application.service.impl.PaymentServiceImpl;
import com.politrons.domain.PaymentAggregateRoot;
import io.vavr.concurrent.Future;
import io.vavr.control.Either;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface PaymentService {

    Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    Future<Either<ErrorPayload, PaymentResponse<PaymentAggregateRoot>>>  fetchPayment(String id);
}
