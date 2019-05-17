package com.politrons.application.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.politrons.application.JsonUtils;
import com.politrons.application.handler.PaymentHandler;
import com.politrons.application.handler.impl.PaymentHandlerImpl;
import com.politrons.application.model.command.AddPaymentCommand;
import com.politrons.application.service.PaymentService;
import com.politrons.application.service.impl.PaymentServiceImpl;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class PaymentHandlerTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    void addPaymentHandler() throws IOException {
        AddPaymentCommand addPaymentCommand = mapper.readValue(JsonUtils.addPaymentCommand(), AddPaymentCommand.class);
        PaymentHandler paymentHandler = new PaymentHandlerImpl();
        String id = paymentHandler.addPayment(addPaymentCommand);
        assertNotNull(id);
    }

}
