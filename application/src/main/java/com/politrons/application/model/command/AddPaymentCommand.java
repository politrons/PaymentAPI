package com.politrons.application.model.command;

import com.politrons.application.model.payload.BeneficiaryPartyPayload;
import com.politrons.application.model.payload.DebtorPartyPayload;
import com.politrons.application.model.payload.SponsorPartyPayload;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddPaymentCommand {
    private String amount;
    private String currency;
    private String paymentId;
    private String paymentPurpose;
    private String paymentType;
    private String processingDate;
    private String reference;
    private String schemePaymentType;
    private String schemePaymentSubType;
    DebtorPartyPayload debtorParty;
    SponsorPartyPayload sponsorParty;
    BeneficiaryPartyPayload beneficiaryParty;

}
