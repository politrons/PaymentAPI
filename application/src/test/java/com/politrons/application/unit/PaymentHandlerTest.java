package com.politrons.application.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.politrons.application.JsonUtils;
import com.politrons.application.handler.PaymentHandler;
import com.politrons.application.handler.impl.PaymentHandlerImpl;
import com.politrons.application.model.command.AddPaymentCommand;
import com.politrons.application.model.command.UpdatePaymentCommand;
import com.politrons.application.model.error.ErrorPayload;
import com.politrons.domain.PaymentStateAggregateRoot;
import com.politrons.infrastructure.repository.PaymentRepository;
import io.vavr.concurrent.Future;
import io.vavr.control.Either;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static io.vavr.API.Right;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentHandlerTest {

    @Mock
    PaymentRepository paymentRepository;

    private PaymentHandler paymentHandler;

    @BeforeEach
    void setup() {
        paymentHandler = new PaymentHandlerImpl(paymentRepository);
    }

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    void addPaymentHandler() throws IOException {
        when(paymentRepository.persistPayment(any(PaymentStateAggregateRoot.class))).thenReturn(Future.of(() -> Right("1981")));
        AddPaymentCommand addPaymentCommand = mapper.readValue(JsonUtils.paymentRequest(), AddPaymentCommand.class);
        Future<Either<ErrorPayload, String>> eithers = paymentHandler.addPayment(addPaymentCommand);
        assertTrue(eithers.get().isRight());
        assertFalse(eithers.get().right().get().isEmpty());
    }

    @Test
    void updatePaymentHandler() throws IOException {
        when(paymentRepository.persistPayment(any(PaymentStateAggregateRoot.class))).thenReturn(Future.of(() -> Right("1981")));
        UpdatePaymentCommand updatePaymentCommand = mapper.readValue(JsonUtils.paymentRequest(), UpdatePaymentCommand.class);
        Future<Either<ErrorPayload, String>> eithers = paymentHandler.updatePayment(updatePaymentCommand);
        assertTrue(eithers.get().isRight());
        assertFalse(eithers.get().right().get().isEmpty());
    }

    @Test
    void deletePaymentHandler() {
        when(paymentRepository.fetchPayment(any(String.class))).thenReturn(Future.of(() -> Right(new PaymentStateAggregateRoot())));
        when(paymentRepository.persistPayment(any(PaymentStateAggregateRoot.class))).thenReturn(Future.of(() -> Right("1981")));
        Future<Either<ErrorPayload, String>> eithers = paymentHandler.deletePayment("123");
        assertTrue(eithers.get().isRight());
        assertFalse(eithers.get().right().get().isEmpty());
    }

    @Test
    void deletePaymentHandlerWithWrongEventId() {
        when(paymentRepository.fetchPayment(any(String.class))).thenReturn(Future.failed(new IllegalArgumentException()));
        Future<Either<ErrorPayload, String>> eithers = paymentHandler.deletePayment("123");
        assertTrue(eithers.get().isLeft());
    }

}
