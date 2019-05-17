package com.politrons.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInfoDTO {
    private String amount;
    private String currency;
    private String paymentId;
    private String paymentPurpose;
    private String paymentType;
    private String processingDate;
    private String reference;
    private String schemePaymentSubType;
    private String schemePaymentType;
    DebtorPartyDTO debtorParty;
    SponsorPartyDTO sponsorParty;
    BeneficiaryPartyDTO beneficiaryParty;

}
