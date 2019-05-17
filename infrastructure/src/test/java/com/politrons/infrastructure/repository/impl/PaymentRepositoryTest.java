package com.politrons.infrastructure.repository.impl;

import com.politrons.domain.PaymentAggregateRoot;
import com.politrons.domain.entities.BeneficiaryParty;
import com.politrons.domain.entities.DebtorParty;
import com.politrons.domain.entities.PaymentInfo;
import com.politrons.domain.entities.SponsorParty;
import com.politrons.domain.repository.PaymentRepository;
import com.politrons.infrastructure.dao.PaymentDAO;
import com.politrons.infrastructure.events.PaymentAdded;
import io.vavr.concurrent.Future;
import io.vavr.control.Either;
import ma.glasnost.orika.impl.DefaultMapperFactory;
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
        PaymentAggregateRoot paymentAggregateRoot = new PaymentAggregateRoot("id", "payment", 0, getPaymentInfo());
        when(paymentDAO.addPayment(any(PaymentAdded.class))).thenReturn(Future.of(() -> Right("1981")));
        Future<Either<Throwable, String>> eithers = paymentRepository.addPayment(paymentAggregateRoot);
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
