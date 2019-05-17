package com.politrons.application.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.politrons.application.JsonUtils;
import com.politrons.application.model.command.AddPaymentCommand;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CommandsTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    void transformJsonToAddPaymentCommand() throws IOException {
        AddPaymentCommand paymentInfo = mapper.readValue(JsonUtils.addPaymentCommand(), AddPaymentCommand.class);
        assertNotNull(paymentInfo);
    }

}
