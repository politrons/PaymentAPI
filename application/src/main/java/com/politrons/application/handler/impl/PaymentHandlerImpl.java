package com.politrons.application.handler.impl;

import com.politrons.application.handler.PaymentHandler;
import com.politrons.application.model.command.AddPaymentCommand;
import com.politrons.domain.PaymentAggregateRoot;
import com.politrons.domain.entities.BeneficiaryParty;
import com.politrons.domain.entities.DebtorParty;
import com.politrons.domain.entities.PaymentInfo;
import com.politrons.domain.entities.SponsorParty;
import com.politrons.domain.repository.PaymentRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class PaymentHandlerImpl implements PaymentHandler {

    @Inject
    PaymentRepository paymentRepository;

    @Override
    public String addPayment(AddPaymentCommand addPaymentCommand) {
        DebtorParty debtorParty = getDebtorParty(addPaymentCommand);
        SponsorParty sponsorParty = getSponsorParty(addPaymentCommand);
        BeneficiaryParty beneficiaryParty = getBeneficiaryParty(addPaymentCommand);
        PaymentInfo paymentInfo = getPaymentInfo(addPaymentCommand, debtorParty, sponsorParty, beneficiaryParty);
        return PaymentAggregateRoot.create(paymentInfo);
    }

    private PaymentInfo getPaymentInfo(AddPaymentCommand addPaymentCommand, DebtorParty debtorParty, SponsorParty sponsorParty, BeneficiaryParty beneficiaryParty) {
        return PaymentInfo.create(addPaymentCommand.getAmount(),
                addPaymentCommand.getCurrency(),
                addPaymentCommand.getPaymentId(),
                addPaymentCommand.getPaymentPurpose(),
                addPaymentCommand.getPaymentType(),
                addPaymentCommand.getProcessingDate(),
                addPaymentCommand.getReference(),
                addPaymentCommand.getSchemePaymentSubType(),
                addPaymentCommand.getSchemePaymentType(),
                debtorParty,
                sponsorParty,
                beneficiaryParty);
    }

    private SponsorParty getSponsorParty(AddPaymentCommand addPaymentCommand) {
        return SponsorParty.create(addPaymentCommand.getSponsorParty().getAccountNumber(),
                addPaymentCommand.getSponsorParty().getBankId(),
                addPaymentCommand.getSponsorParty().getBankIdCode());
    }

    private DebtorParty getDebtorParty(AddPaymentCommand addPaymentCommand) {
        return DebtorParty.create(addPaymentCommand.getDebtorParty().getAccountName(),
                addPaymentCommand.getDebtorParty().getAccountNumber(),
                addPaymentCommand.getDebtorParty().getAccountType(),
                addPaymentCommand.getDebtorParty().getAddress(),
                addPaymentCommand.getDebtorParty().getBankId(),
                addPaymentCommand.getDebtorParty().getName());
    }

    private BeneficiaryParty getBeneficiaryParty(AddPaymentCommand addPaymentCommand) {
        return BeneficiaryParty.create(addPaymentCommand.getBeneficiaryParty().getAccountName(),
                addPaymentCommand.getBeneficiaryParty().getAccountNumber(),
                addPaymentCommand.getBeneficiaryParty().getAccountType(),
                addPaymentCommand.getBeneficiaryParty().getAddress(),
                addPaymentCommand.getBeneficiaryParty().getBankId(),
                addPaymentCommand.getBeneficiaryParty().getName());
    }
}
