package com.politrons.infrastructure.dao.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.politrons.domain.PaymentStateAggregateRoot;
import com.politrons.infrastructure.CassandraConnector;
import com.politrons.infrastructure.events.PaymentAdded;
import io.vavr.concurrent.Future;
import io.vavr.control.Either;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentDAOTest extends PaymentDAOUtilsTest {

    private PaymentDAOImpl paymentDAO = new PaymentDAOImpl();

    private ObjectMapper mapper = new ObjectMapper();


    @BeforeAll
    static void init() {
        CassandraConnector.start();
    }

    //####################//
    //     Add payment    //
    //####################//
    @Test
    void addPaymentEvent() {
        Future<Either<Throwable, String>> eithers = paymentDAO.upsertPayment(getPaymentAddedEvent());
        assertTrue(eithers.get().isRight());
        assertFalse(eithers.get().right().get().isEmpty());
    }

    @Test
    void addPaymentEventWithWrongJson() {
        Future<Either<Throwable, String>> eithers = paymentDAO.upsertPayment(null);
        assertTrue(eithers.get().isLeft());
    }

    //####################//
    //    Fetch payment   //
    //####################//
    @Test
    void fetchPayment() throws JsonProcessingException {
        String uuid = UUID.randomUUID().toString();
        PaymentAdded paymentAddedEvent = getPaymentAddedEvent();
        paymentAddedEvent.setId(uuid);
        String event = mapper.writeValueAsString(paymentAddedEvent);
        CassandraConnector.addPayment(getAddPaymentQuery(paymentAddedEvent, "12345", event));
        Future<Either<Throwable, PaymentStateAggregateRoot>> eithers = paymentDAO.fetchPayment(uuid);
        assertTrue(eithers.get().isRight());
        assertEquals(eithers.get().right().get().getId(), uuid);
    }

    @Test
    void fetchPaymentWithWrongId() {
        Future<Either<Throwable, PaymentStateAggregateRoot>> eithers = paymentDAO.fetchPayment("foo");
        assertTrue(eithers.get().isLeft());
    }

    @AfterAll
    static void close() {
        CassandraConnector.stop();
    }

}
