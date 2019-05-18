package com.politrons.application.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.politrons.application.JsonUtils;
import com.politrons.application.model.command.AddPaymentCommand;
import com.politrons.application.model.command.UpdatePaymentCommand;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CommandsTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    void transformJsonToAddPaymentCommand() throws IOException {
        AddPaymentCommand command = mapper.readValue(JsonUtils.paymentRequest(), AddPaymentCommand.class);
        assertNotNull(command);
    }

    @Test
    void transformJsonToUpdatePaymentCommand() throws IOException {
        UpdatePaymentCommand command = mapper.readValue(JsonUtils.paymentRequest(), UpdatePaymentCommand.class);
        assertNotNull(command);
    }

}
