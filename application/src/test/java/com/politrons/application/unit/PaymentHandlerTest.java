package com.politrons.application.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.politrons.application.JsonUtils;
import com.politrons.application.handler.PaymentHandler;
import com.politrons.application.handler.impl.PaymentHandlerImpl;
import com.politrons.application.model.command.AddPaymentCommand;
import com.politrons.application.model.error.ErrorPayload;
import com.politrons.application.service.PaymentService;
import com.politrons.application.service.impl.PaymentServiceImpl;
import io.vavr.concurrent.Future;
import io.vavr.control.Either;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PaymentHandlerTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    void addPaymentHandler() throws IOException {
        AddPaymentCommand addPaymentCommand = mapper.readValue(JsonUtils.addPaymentCommand(), AddPaymentCommand.class);
        PaymentHandler paymentHandler = new PaymentHandlerImpl();
        Future<Either<ErrorPayload, String>> eithers = paymentHandler.addPayment(addPaymentCommand);
        assertTrue(eithers.get().isRight());
        assertFalse(eithers.get().right().get().isEmpty());
    }

}
