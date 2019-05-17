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
    private String paymentPurpose;
    private String paymentType;
    private String processingDate;
    private String reference;
    private String schemePaymentSubType;
    private String schemePaymentType;
    DebtorParty debtorParty;
    SponsorParty sponsorParty;
    BeneficiaryParty beneficiaryParty;

    static PaymentInfo create(String amount,
                              String currency,
                              String paymentId,
                              String paymentPurpose,
                              String paymentType,
                              String processingDate,
                              String reference,
                              String schemePaymentSubType,
                              String schemePaymentType,
                              DebtorParty debtorParty,
                              SponsorParty sponsorParty,
                              BeneficiaryParty beneficiaryParty) {
        return new PaymentInfo(amount, currency, paymentId, paymentPurpose,
                paymentType, processingDate, reference, schemePaymentSubType,
                schemePaymentType, debtorParty, sponsorParty, beneficiaryParty);

    }
}
