package com.politrons.application.handler.impl;

import com.politrons.application.handler.PaymentHandler;
import com.politrons.application.model.command.AddPaymentCommand;
import com.politrons.application.model.command.PaymentCommand;
import com.politrons.application.model.command.UpdatePaymentCommand;
import com.politrons.application.model.error.ErrorPayload;
import com.politrons.domain.PaymentStateAggregateRoot;
import com.politrons.domain.entities.BeneficiaryParty;
import com.politrons.domain.entities.DebtorParty;
import com.politrons.domain.entities.PaymentInfo;
import com.politrons.domain.entities.SponsorParty;
import com.politrons.infrastructure.repository.PaymentRepository;
import io.vavr.API;
import io.vavr.Function1;
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
     * <p>
     * Since the operation to database it take time and it might not be part of the machine we
     * need to make this operation async. As personal level I like Vavr library for functional programing in Java.(Pretty much like Scala monads)
     * <p>
     * Transform the [AddPaymentCommand] in a new [PaymentStateAggregateRoot] with [type] state as "created"
     * <p>
     * Also since we have to deal with impure world, we need to control effects. So in order to control the possibility
     * of a Database problem, or network, we will catch the throwable and we will transform into Either of possible error [Left]
     * or the expected output [Right]
     *
     * @param addPaymentCommand to be transformed into Domain model to be persisted.
     * @return the id of the event to be modified or deleted in the future
     */
    @Override
    public Future<Either<ErrorPayload, String>> addPayment(AddPaymentCommand addPaymentCommand) {
        return upsertPayment(addPaymentCommand, PaymentStateAggregateRoot::create);
    }

    /**
     * Handler to transform the [UpdatePaymentCommand] in a new [PaymentStateAggregateRoot] with [type] state as "changed"
     *
     * @param updatePaymentCommand command to be transformed into Domain model with state "changed"
     * @return a new id of the row of the new Event with the new state.
     */
    @Override
    public Future<Either<ErrorPayload, String>> updatePayment(UpdatePaymentCommand updatePaymentCommand) {
        return upsertPayment(updatePaymentCommand, PaymentStateAggregateRoot::update);
    }

    /**
     * Genetic method to receive the command and a function to be transformed into domain model and being persisted using the
     * repository of infrastructure layer.
     * @param paymentCommand generic command for create/update
     * @param func generic to apply and return the [PaymentStateAggregateRoot]
     * @return
     */
    private Future<Either<ErrorPayload, String>> upsertPayment(PaymentCommand paymentCommand,
                                                               Function1<PaymentInfo, PaymentStateAggregateRoot> func) {
        return paymentRepository.persistPayment(getPaymentAggregateRoot(paymentCommand, func))
                .map(either -> Match(either).of(
                        Case($Right($()), API::Right),
                        Case($Left($()), t -> Left(new ErrorPayload(500, t.getMessage())))));
    }

    private PaymentStateAggregateRoot getPaymentAggregateRoot(PaymentCommand updatePaymentCommand,
                                                              Function1<PaymentInfo, PaymentStateAggregateRoot> changeStateFunc) {
        PaymentInfo paymentInfo = getPaymentInfo(updatePaymentCommand);
        return changeStateFunc.apply(paymentInfo);
    }

    private PaymentInfo getPaymentInfo(PaymentCommand addPaymentCommand) {
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

    private SponsorParty getSponsorParty(PaymentCommand addPaymentCommand) {
        return SponsorParty.create(addPaymentCommand.getSponsorParty().getAccountNumber(),
                addPaymentCommand.getSponsorParty().getBankId(),
                addPaymentCommand.getSponsorParty().getBankIdCode());
    }

    private DebtorParty getDebtorParty(PaymentCommand addPaymentCommand) {
        return DebtorParty.create(addPaymentCommand.getDebtorParty().getAccountName(),
                addPaymentCommand.getDebtorParty().getAccountNumber(),
                addPaymentCommand.getDebtorParty().getAccountType(),
                addPaymentCommand.getDebtorParty().getAddress(),
                addPaymentCommand.getDebtorParty().getBankId(),
                addPaymentCommand.getDebtorParty().getName());
    }

    private BeneficiaryParty getBeneficiaryParty(PaymentCommand addPaymentCommand) {
        return BeneficiaryParty.create(addPaymentCommand.getBeneficiaryParty().getAccountName(),
                addPaymentCommand.getBeneficiaryParty().getAccountNumber(),
                addPaymentCommand.getBeneficiaryParty().getAccountType(),
                addPaymentCommand.getBeneficiaryParty().getAddress(),
                addPaymentCommand.getBeneficiaryParty().getBankId(),
                addPaymentCommand.getBeneficiaryParty().getName());
    }
}
