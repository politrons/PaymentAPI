package com.politrons.domain.entities;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PaymentInfo {
    private String amount;
    BeneficiaryParty beneficiaryParty;
    DebtorParty debtorParty;
    SponsorParty sponsorParty;
    private String currency;
    private String paymentId;
    private String paymentPurpose;
    private String paymentType;
    private String processingDate;
    private String reference;
    private String schemePaymentSubType;
    private String schemePaymentType;
}