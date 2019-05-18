package com.politrons.infrastructure.dao.impl;

import com.politrons.infrastructure.CassandraConnector;
import com.politrons.infrastructure.events.PaymentAdded;
import io.vavr.concurrent.Future;
import io.vavr.control.Either;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PaymentDAOTest extends PaymentDAOUtilsTest {

    private PaymentDAOImpl paymentDAO = new PaymentDAOImpl();

    @BeforeAll
    static void init(){
        CassandraConnector.start();
    }

    @Test
    void addPaymentEvent() {
        Future<Either<Throwable, String>> eithers = paymentDAO.addPayment(getPaymentAddedEvent());
        assertTrue(eithers.get().isRight());
        assertFalse(eithers.get().right().get().isEmpty());
    }

    @Test
    void addPaymentEventWithWrongJson() {
        Future<Either<Throwable, String>> eithers = paymentDAO.addPayment(new PaymentAdded());
        assertTrue(eithers.get().isLeft());
    }

    @AfterAll
    static void close(){
        CassandraConnector.stop();
    }

}
