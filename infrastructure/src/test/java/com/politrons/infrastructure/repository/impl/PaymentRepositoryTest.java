package com.politrons.infrastructure.repository.impl;

import com.politrons.domain.PaymentStateAggregateRoot;
import com.politrons.domain.entities.BeneficiaryParty;
import com.politrons.domain.entities.DebtorParty;
import com.politrons.domain.entities.PaymentInfo;
import com.politrons.domain.entities.SponsorParty;
import com.politrons.infrastructure.dao.PaymentDAO;
import com.politrons.infrastructure.events.PaymentAdded;
import com.politrons.infrastructure.events.PaymentDeleted;
import com.politrons.infrastructure.events.PaymentUpdated;
import com.politrons.infrastructure.repository.PaymentRepository;
import io.vavr.concurrent.Future;
import io.vavr.control.Either;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.vavr.API.Right;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentRepositoryTest {

    @Mock
    PaymentDAO paymentDAO;

    private PaymentRepository paymentRepository;

    @BeforeEach
    void setup() {
        paymentRepository = new PaymentRepositoryImpl(paymentDAO);
    }

    @Test
    void addPaymentEvent() {
        PaymentStateAggregateRoot paymentStateAggregateRoot = new PaymentStateAggregateRoot("id", "payment", 0, getPaymentInfo());
        when(paymentDAO.persistPaymentAddedEvent(any(PaymentAdded.class))).thenReturn(Future.of(() -> Right("1981")));
        Future<Either<Throwable, String>> eithers = paymentRepository.addPayment(paymentStateAggregateRoot);
        assertTrue(eithers.get().isRight());
        assertFalse(eithers.get().right().get().isEmpty());
    }

    @Test
    void updatePaymentEvent() {
        PaymentStateAggregateRoot paymentStateAggregateRoot = new PaymentStateAggregateRoot("id", "payment", 0, getPaymentInfo());
        when(paymentDAO.persistPaymentUpdatedEvent(any(PaymentUpdated.class))).thenReturn(Future.of(() -> Right("1981")));
        Future<Either<Throwable, String>> eithers = paymentRepository.updatePayment(paymentStateAggregateRoot);
        assertTrue(eithers.get().isRight());
        assertFalse(eithers.get().right().get().isEmpty());
    }

    @Test
    void deletePaymentEvent() {
        PaymentStateAggregateRoot paymentStateAggregateRoot = new PaymentStateAggregateRoot("id", "payment", 0, getPaymentInfo());
        when(paymentDAO.persistPaymentDeletedEvent(any(PaymentDeleted.class))).thenReturn(Future.of(() -> Right("1981")));
        Future<Either<Throwable, String>> eithers = paymentRepository.deletePayment(paymentStateAggregateRoot);
        assertTrue(eithers.get().isRight());
        assertFalse(eithers.get().right().get().isEmpty());
    }

    private PaymentInfo getPaymentInfo() {
        DebtorParty debtorParty = getDebtorParty();
        BeneficiaryParty beneficiaryParty = getBeneficiaryParty();
        SponsorParty sponsorParty = getSponsorParty();
        return new PaymentInfo("amount", "currency",
                "paymentId",
                "paymentPurpose", "paymentType",
                "processingDate", "reference", "schemePaymentSubType",
                "schemePaymentType", debtorParty, sponsorParty, beneficiaryParty);
    }

    private SponsorParty getSponsorParty() {
        return new SponsorParty("accountName",
                "bankId", "bankCode");
    }

    private BeneficiaryParty getBeneficiaryParty() {
        return new BeneficiaryParty("accountName",
                "accountNumber", 0, "address", "bankId", "name");
    }

    private DebtorParty getDebtorParty() {
        return new DebtorParty("accountName", "accountNumber",
                0, "address", "bankId", "name");
    }

}
