package com.politrons.domain.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.politrons.domain.PaymentAggregateRoot;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class EntitiesTest {


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

    private PaymentInfo getPaymentInfo() {
        DebtorParty debtorParty = new DebtorParty("accountName", "accountNumber",
                0, "address", "bankId", "name");
        BeneficiaryParty beneficiaryParty = new BeneficiaryParty("accountName",
                "accountNumber", 0, "address", "bankId", "name");
        SponsorParty sponsorParty = new SponsorParty("accountName",
                "bankId", "bankCode");
        return new PaymentInfo("amount", "currency",
                "paymentId", debtorParty, sponsorParty, beneficiaryParty,
                "paymentPurpose", "paymentType",
                "processingDate", "reference", "schemePaymentSubType",
                "schemePaymentType");
    }
}
