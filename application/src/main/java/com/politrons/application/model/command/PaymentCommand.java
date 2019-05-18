package com.politrons.application.model.command;

import com.politrons.application.model.payload.payload.BeneficiaryPartyPayload;
import com.politrons.application.model.payload.payload.DebtorPartyPayload;
import com.politrons.application.model.payload.payload.SponsorPartyPayload;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class PaymentCommand {
    protected String amount;
    protected String currency;
    protected String paymentId;
    protected String paymentPurpose;
    protected String paymentType;
    protected String processingDate;
    protected String reference;
    protected String schemePaymentType;
    protected String schemePaymentSubType;
    protected DebtorPartyPayload debtorParty;
    protected SponsorPartyPayload sponsorParty;
    protected BeneficiaryPartyPayload beneficiaryParty;

}
