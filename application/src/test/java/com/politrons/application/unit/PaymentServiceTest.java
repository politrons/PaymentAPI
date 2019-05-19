package com.politrons.application.unit;

import com.politrons.application.model.error.ErrorPayload;
import com.politrons.application.model.payload.payload.PaymentStatePayload;
import com.politrons.application.model.payload.response.PaymentResponse;
import com.politrons.application.service.PaymentService;
import com.politrons.application.service.impl.PaymentServiceImpl;
import com.politrons.domain.PaymentStateAggregateRoot;
import com.politrons.infrastructure.dao.PaymentDAO;
import io.vavr.concurrent.Future;
import io.vavr.control.Either;
import jodd.util.ArraysUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static io.vavr.API.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    PaymentDAO paymentDAO;

    private PaymentService paymentService;

    @BeforeEach
    void setup() {
        paymentService = new PaymentServiceImpl(paymentDAO);
    }

    @Test
    void fetchPaymentService() {
        PaymentStateAggregateRoot paymentStateAggregateRoot = new PaymentStateAggregateRoot("myCustomUUID", "payment", 0, null);
        when(paymentDAO.fetchPayment(any(String.class))).thenReturn(Future.of(() -> Right(paymentStateAggregateRoot)));
        Future<Either<ErrorPayload, PaymentStatePayload>> eithers = paymentService.fetchPayment("myCustomUUID");
        assertTrue(eithers.get().isRight());
        assertEquals(eithers.get().right().get().getId(), "myCustomUUID");
    }

    @Test
    void fetchPaymentServiceError() {
        when(paymentDAO.fetchPayment(any(String.class))).thenReturn(Future.of(() -> Left(new IllegalArgumentException())));
        Future<Either<ErrorPayload, PaymentStatePayload>> eithers = paymentService.fetchPayment("1981");
        assertTrue(eithers.get().isLeft());
        assertTrue(eithers.get().left().get() instanceof ErrorPayload);
    }


    @Test
    void fetchAllPaymentService() {
        PaymentStateAggregateRoot paymentStateAggregateRoot = new PaymentStateAggregateRoot("myCustomUUID", "payment", 0, null);
        List paymentStateAggregateRootList = new ArrayList();
        paymentStateAggregateRootList.add(paymentStateAggregateRoot);
        when(paymentDAO.fetchAllPayments()).thenReturn(Future.of(() -> Right(paymentStateAggregateRootList)));
        Future<Either<ErrorPayload, List<PaymentStatePayload>>> eithers = paymentService.fetchAllPayments();
        assertTrue(eithers.get().isRight());
        assertEquals(eithers.get().right().get().size(), 1);
        assertEquals(eithers.get().right().get().get(0).getId(), "myCustomUUID");

    }

    @Test
    void fetchAllPaymentServiceError() {
        when(paymentDAO.fetchAllPayments()).thenReturn(Future.of(() -> Left(new IllegalArgumentException())));
        Future<Either<ErrorPayload, List<PaymentStatePayload>>> eithers = paymentService.fetchAllPayments();
        assertTrue(eithers.get().isLeft());
        assertTrue(eithers.get().left().get() instanceof ErrorPayload);
    }

}
