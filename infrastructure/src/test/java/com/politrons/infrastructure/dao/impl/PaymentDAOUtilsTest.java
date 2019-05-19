package com.politrons.infrastructure.dao.impl;

import com.politrons.infrastructure.dto.BeneficiaryPartyDTO;
import com.politrons.infrastructure.dto.DebtorPartyDTO;
import com.politrons.infrastructure.dto.PaymentInfoDTO;
import com.politrons.infrastructure.dto.SponsorPartyDTO;
import com.politrons.infrastructure.events.PaymentAdded;
import com.politrons.infrastructure.events.PaymentDeleted;
import com.politrons.infrastructure.events.PaymentUpdated;

import java.util.UUID;

public class PaymentDAOUtilsTest {


    String getAddPaymentQuery(String id, String timeStampMillis, String event) {
        return "INSERT INTO " + "paymentsSchema.payment" +
                "(id, timestamp, event) " +
                "VALUES (" + id + ", '" +
                timeStampMillis + "', '" +
                event + "');";
    }

    protected PaymentAdded getPaymentAddedEvent() {
        return new PaymentAdded(UUID.randomUUID().toString(), "created", 0, getPaymentInfoDTO());
    }

    protected PaymentUpdated getPaymentUpdatedEvent() {
        return new PaymentUpdated(UUID.randomUUID().toString(), "changed", 0, getPaymentInfoDTO());
    }

    protected PaymentDeleted getPaymentDeletedEvent() {
        return new PaymentDeleted(UUID.randomUUID().toString(), "deleted", 0, getPaymentInfoDTO());
    }

    PaymentInfoDTO getPaymentInfoDTO() {
        DebtorPartyDTO debtorParty = getDebtorParty();
        BeneficiaryPartyDTO beneficiaryParty = getBeneficiaryParty();
        SponsorPartyDTO sponsorParty = getSponsorParty();
        return new PaymentInfoDTO("amount", "currency",
                "paymentId",
                "paymentPurpose", "paymentType",
                "processingDate", "reference", "schemePaymentSubType",
                "schemePaymentType", debtorParty, sponsorParty, beneficiaryParty);
    }

    private SponsorPartyDTO getSponsorParty() {
        return new SponsorPartyDTO("accountName",
                "bankId", "bankCode");
    }

    private BeneficiaryPartyDTO getBeneficiaryParty() {
        return new BeneficiaryPartyDTO("accountName",
                "accountNumber", 0, "address", "bankId", "name");
    }

    private DebtorPartyDTO getDebtorParty() {
        return new DebtorPartyDTO("accountName", "accountNumber",
                0, "address", "bankId", "name");
    }




}
