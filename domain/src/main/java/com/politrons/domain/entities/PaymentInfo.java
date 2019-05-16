package com.politrons.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInfo {
    private String amount;
    private String currency;
    private String paymentId;
    DebtorParty debtorParty;
    SponsorParty sponsorParty;
    BeneficiaryParty beneficiaryParty;
    private String paymentPurpose;
    private String paymentType;
    private String processingDate;
    private String reference;
    private String schemePaymentSubType;
    private String schemePaymentType;
}
