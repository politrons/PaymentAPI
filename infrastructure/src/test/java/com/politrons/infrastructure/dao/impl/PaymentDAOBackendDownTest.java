package com.politrons.infrastructure.dao.impl;

import com.politrons.infrastructure.events.PaymentAdded;
import io.vavr.concurrent.Future;
import io.vavr.control.Either;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PaymentDAOBackendDownTest extends PaymentDAOUtilsTest{

    private PaymentDAOImpl paymentDAO = new PaymentDAOImpl();

    @Test
    void addPaymentEvenWithDatabaseDown() {
        Future<Either<Throwable, String>> eithers = paymentDAO.addPayment(getPaymentAddedEvent());
        assertTrue(eithers.get().isLeft());
    }

    protected PaymentAdded getPaymentAddedEvent() {
        return new PaymentAdded(UUID.randomUUID().toString(), "payment", 0, getPaymentInfoDTO());
    }


}
