package com.politrons.application.handler.impl;

import com.politrons.application.handler.PaymentHandler;
import com.politrons.application.model.command.AddPaymentCommand;
import com.politrons.application.model.error.ErrorPayload;
import com.politrons.domain.PaymentAggregateRoot;
import com.politrons.domain.entities.BeneficiaryParty;
import com.politrons.domain.entities.DebtorParty;
import com.politrons.domain.entities.PaymentInfo;
import com.politrons.domain.entities.SponsorParty;
import com.politrons.domain.repository.PaymentRepository;
import io.vavr.API;
import io.vavr.concurrent.Future;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import static io.vavr.API.*;
import static io.vavr.Patterns.$Left;
import static io.vavr.Patterns.$Right;

@NoArgsConstructor
@AllArgsConstructor
@ApplicationScoped
public class PaymentHandlerImpl implements PaymentHandler {

    @Inject
    PaymentRepository paymentRepository;

    /**
     * Handler to receive a Command to add a payment and return the id of the operation.
     *
     * Since the operation to database it take time and it might not be part of the machine we
     * need to make this operation async. As personal level I like Vavr library for functional programing in Java.(Pretty much like Scala monads)
     *
     * Also since we have to deal with impure world, we need to control effects. So in order to control the possibility
     * of a Database problem, or network, we will catch the throwable and we will transform into Either of possible error [Left]
     * or the expected output [Right]
     *
     * @param addPaymentCommand to be transformed into Domain model to be persisted.
     * @return the id of the event to be modified or deleted in the future
     */
    @Override
    public Future<Either<ErrorPayload, String>> addPayment(AddPaymentCommand addPaymentCommand) {
        Future<Either<Throwable, String>> eithers = paymentRepository.addPayment(getPaymentAggregateRoot(addPaymentCommand));
        return eithers.map(either -> Match(either).of(
                Case($Right($()), API::Right),
                Case($Left($()), t -> Left(new ErrorPayload(500, t.getMessage())))));
    }

    private PaymentAggregateRoot getPaymentAggregateRoot(AddPaymentCommand addPaymentCommand) {
        PaymentInfo paymentInfo = getPaymentInfo(addPaymentCommand);
        return PaymentAggregateRoot.create(paymentInfo);
    }

    private PaymentInfo getPaymentInfo(AddPaymentCommand addPaymentCommand) {
        return PaymentInfo.create(addPaymentCommand.getAmount(),
                addPaymentCommand.getCurrency(),
                addPaymentCommand.getPaymentId(),
                addPaymentCommand.getPaymentPurpose(),
                addPaymentCommand.getPaymentType(),
                addPaymentCommand.getProcessingDate(),
                addPaymentCommand.getReference(),
                addPaymentCommand.getSchemePaymentSubType(),
                addPaymentCommand.getSchemePaymentType(),
                getDebtorParty(addPaymentCommand),
                getSponsorParty(addPaymentCommand),
                getBeneficiaryParty(addPaymentCommand));
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
