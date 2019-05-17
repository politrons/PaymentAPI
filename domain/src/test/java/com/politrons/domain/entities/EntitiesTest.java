package com.politrons.domain.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.politrons.domain.PaymentAggregateRoot;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class EntitiesTest {


    ObjectMapper mapper = new ObjectMapper();

    @Test
    void createPaymentAggregateRoot() {
        PaymentInfo paymentInfo = getPaymentInfo();
        PaymentAggregateRoot paymentAggregateRoot = new PaymentAggregateRoot("id", "type", 1, paymentInfo);
        assertEquals(paymentAggregateRoot.getId(), "id");
    }

    @Test
    void transformDomainToJson() throws JsonProcessingException {
        PaymentInfo paymentInfo = getPaymentInfo();
        PaymentAggregateRoot paymentAggregateRoot = new PaymentAggregateRoot("id", "type", 1, paymentInfo);
        String json = mapper.writeValueAsString(paymentAggregateRoot);
        assertNotNull(json);
    }

    @Test
    void transformJsonToDomain() throws IOException {
        PaymentInfo paymentInfo = mapper.readValue(JsonUtils.paymentInfoJson(), PaymentInfo.class);
        assertNotNull(paymentInfo);
    }

    @Test
    void debtorPartyFactory() {
        DebtorParty debtorParty = DebtorParty.create("accountName", "accountNumber",
                0, "address", "bankId", "name");
        assertNotNull(debtorParty);
    }

    @Test
    void beneficiaryPartyFactory() {
        BeneficiaryParty beneficiaryParty = BeneficiaryParty.create("accountName",
                "accountNumber", 0, "address", "bankId", "name");
        assertNotNull(beneficiaryParty);

    }

    @Test
    void sponsorPartyFactory() {
        SponsorParty sponsorParty = SponsorParty.create("accountName",
                "bankId", "bankCode");
        assertNotNull(sponsorParty);
    }

    @Test
    void paymentInfoFactory() {
        PaymentInfo paymentInfo = PaymentInfo.create("amount", "currency",
                "paymentId",
                "paymentPurpose", "paymentType",
                "processingDate", "reference", "schemePaymentSubType",
                "schemePaymentType", getDebtorParty(), getSponsorParty(), getBeneficiaryParty());
        assertNotNull(paymentInfo);
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
