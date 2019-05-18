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
public class UpdatePaymentCommand extends PaymentCommand{

    String paymentId;

}
