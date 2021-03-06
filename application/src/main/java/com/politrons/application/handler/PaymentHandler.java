package com.politrons.application.handler;

import com.politrons.application.model.command.AddPaymentCommand;
import com.politrons.application.model.command.UpdatePaymentCommand;
import com.politrons.application.model.error.ErrorPayload;
import io.vavr.concurrent.Future;
import io.vavr.control.Either;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface PaymentHandler {

    Future<Either<ErrorPayload,String>> addPayment(AddPaymentCommand addPaymentCommand);

    Future<Either<ErrorPayload,String>> updatePayment(UpdatePaymentCommand addPaymentCommand);

    Future<Either<ErrorPayload,String>> deletePayment(String paymentId);
}
