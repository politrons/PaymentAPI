package com.politrons.application.handler;

import com.politrons.application.model.command.AddPaymentCommand;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface PaymentHandler {

    String addPayment(AddPaymentCommand addPaymentCommand);

}
