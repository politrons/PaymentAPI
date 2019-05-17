package com.politrons.infrastructure.dao.impl;

import com.politrons.infrastructure.CassandraConnector;
import com.politrons.infrastructure.dao.PaymentDAO;
import io.vavr.concurrent.Future;
import io.vavr.control.Either;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class PaymentDAOTest {

    private PaymentDAO paymentDAO;

    @BeforeEach
    void setup() {
        CassandraConnector.start();
    }

//    @Test
    void addPaymentEvent() {
        Future<Either<Throwable, String>> eithers = paymentDAO.addPayment(null);
        assertTrue(eithers.get().isRight());
        assertFalse(eithers.get().right().get().isEmpty());
    }



}
