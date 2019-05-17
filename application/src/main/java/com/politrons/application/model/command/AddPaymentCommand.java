package com.politrons.application.model.command;

import com.politrons.application.model.payload.request.BeneficiaryPartyRequest;
import com.politrons.application.model.payload.request.DebtorPartyRequest;
import com.politrons.application.model.payload.request.SponsorPartyRequest;
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
    DebtorPartyRequest debtorParty;
    SponsorPartyRequest sponsorParty;
    BeneficiaryPartyRequest beneficiaryParty;

}
