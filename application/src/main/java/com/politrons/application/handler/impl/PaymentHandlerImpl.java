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
        return paymentRepository.addPayment(getPaymentAggregateRoot(addPaymentCommand, PaymentStateAggregateRoot::create))
                .map(this::processRepositoryResponse);
    }

    /**
     * Handler to transform the [UpdatePaymentCommand] in a new [PaymentStateAggregateRoot] with [type] state as "changed"
     *
     * @param updatePaymentCommand command to be transformed into Domain model with state "changed"
     * @return a new id of the row of the new Event with the new state.
     */
    @Override
    public Future<Either<ErrorPayload, String>> updatePayment(UpdatePaymentCommand updatePaymentCommand) {
        return paymentRepository.updatePayment(getPaymentAggregateRoot(updatePaymentCommand, PaymentStateAggregateRoot::update))
                .map(this::processRepositoryResponse);
    }

    /**
     * Handler to get the the previous event created using the id, then we change the state as [deleted] and
     * finally we delegate the creation of the new event into the infrastructure layer."
     *
     * @param eventId from the previous event to fetch and change state.
     * @return a new id of the row of the new Event with the new state.
     */
    @Override
    public Future<Either<ErrorPayload, String>> deletePayment(String eventId) {
        return paymentRepository.fetchPayment(eventId)
                .recover(API::Left)
                .flatMap(either -> Match(either).of(
                        Case($Right($()), paymentStateAggregateRoot ->
                                paymentRepository.deletePayment(PaymentStateAggregateRoot.delete(paymentStateAggregateRoot))
                                        .map(this::processRepositoryResponse)),
                        Case($Left($()), t -> Future.of(() -> Left(new ErrorPayload(500, t.getMessage()))))
                ));
    }

    private Either<ErrorPayload, String> processRepositoryResponse(Either<Throwable, String> either) {
        return Match(either).of(
                Case($Right($()), API::Right),
                Case($Left($()), t -> Left(new ErrorPayload(500, t.getMessage()))));
    }

    //#############################//
    //       DOMAIN FACTORY        //
    //#############################//

    private PaymentStateAggregateRoot getPaymentAggregateRoot(PaymentCommand paymentCommand,
                                                              Function1<PaymentInfo, PaymentStateAggregateRoot> changeStateFunc) {
        PaymentInfo paymentInfo = getPaymentInfo(paymentCommand);
        return changeStateFunc.apply(paymentInfo);
    }

    private PaymentInfo getPaymentInfo(PaymentCommand paymentCommand) {
        return PaymentInfo.create(paymentCommand.getAmount(),
                paymentCommand.getCurrency(),
                paymentCommand.getPaymentId(),
                paymentCommand.getPaymentPurpose(),
                paymentCommand.getPaymentType(),
                paymentCommand.getProcessingDate(),
                paymentCommand.getReference(),
                paymentCommand.getSchemePaymentSubType(),
                paymentCommand.getSchemePaymentType(),
                getDebtorParty(paymentCommand),
                getSponsorParty(paymentCommand),
                getBeneficiaryParty(paymentCommand));
    }

    private SponsorParty getSponsorParty(PaymentCommand paymentCommand) {
        return SponsorParty.create(paymentCommand.getSponsorParty().getAccountNumber(),
                paymentCommand.getSponsorParty().getBankId(),
                paymentCommand.getSponsorParty().getBankIdCode());
    }

    private DebtorParty getDebtorParty(PaymentCommand paymentCommand) {
        return DebtorParty.create(paymentCommand.getDebtorParty().getAccountName(),
                paymentCommand.getDebtorParty().getAccountNumber(),
                paymentCommand.getDebtorParty().getAccountType(),
                paymentCommand.getDebtorParty().getAddress(),
                paymentCommand.getDebtorParty().getBankId(),
                paymentCommand.getDebtorParty().getName());
    }

    private BeneficiaryParty getBeneficiaryParty(PaymentCommand paymentCommand) {
        return BeneficiaryParty.create(paymentCommand.getBeneficiaryParty().getAccountName(),
                paymentCommand.getBeneficiaryParty().getAccountNumber(),
                paymentCommand.getBeneficiaryParty().getAccountType(),
                paymentCommand.getBeneficiaryParty().getAddress(),
                paymentCommand.getBeneficiaryParty().getBankId(),
                paymentCommand.getBeneficiaryParty().getName());
    }
}
