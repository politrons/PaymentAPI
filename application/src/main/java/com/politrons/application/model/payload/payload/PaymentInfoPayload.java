package com.politrons.application.model.payload.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInfoPayload {
    private String amount;
    private String currency;
    private String paymentId;
    private String paymentPurpose;
    private String paymentType;
    private String processingDate;
    private String reference;
    private String schemePaymentSubType;
    private String schemePaymentType;
    DebtorPartyPayload debtorParty;
    SponsorPartyPayload sponsorParty;
    BeneficiaryPartyPayload beneficiaryParty;

    public static PaymentInfoPayload create(String amount,
                                            String currency,
                                            String paymentId,
                                            String paymentPurpose,
                                            String paymentType,
                                            String processingDate,
                                            String reference,
                                            String schemePaymentSubType,
                                            String schemePaymentType,
                                            DebtorPartyPayload debtorParty,
                                            SponsorPartyPayload sponsorParty,
                                            BeneficiaryPartyPayload beneficiaryParty) {
        return new PaymentInfoPayload(amount, currency, paymentId, paymentPurpose,
                paymentType, processingDate, reference, schemePaymentSubType,
                schemePaymentType, debtorParty, sponsorParty, beneficiaryParty);

    }
}
