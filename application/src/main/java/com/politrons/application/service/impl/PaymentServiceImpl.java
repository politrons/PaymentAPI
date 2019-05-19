package com.politrons.application.service.impl;


import com.politrons.application.model.error.ErrorPayload;
import com.politrons.application.model.payload.payload.PaymentStatePayload;
import com.politrons.application.service.PaymentService;
import com.politrons.domain.PaymentStateAggregateRoot;
import com.politrons.infrastructure.dao.PaymentDAO;
import com.politrons.infrastructure.events.PaymentAdded;
import io.vavr.API;
import io.vavr.collection.Stream;
import io.vavr.concurrent.Future;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ma.glasnost.orika.MapperFacade;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;

import static io.vavr.API.*;
import static io.vavr.Patterns.*;

/**
 * Service to make Queries into the DAO.
 * Doing DDD + CQRS since we need to be fast in queries since 90% of traffic are queries, and there's no transformation need it here
 * we go though directly to the source instead to pass through the repository.
 */
@AllArgsConstructor
@NoArgsConstructor
@ApplicationScoped
public class PaymentServiceImpl implements PaymentService {

    @Inject
    PaymentDAO paymentDAO;

    /**
     * Method to fetch a payment previously created/updated/deleted.
     *
     * @param id of the payment
     * @return the Domain model PaymentStateAggregateRoot
     */
    public Future<Either<ErrorPayload, PaymentStatePayload>> fetchPayment(String id) {
        return paymentDAO.fetchPayment(id)
                .map(either -> Match(either).of(
                        Case($Right($()), paymentStateAggregateRoot -> Right(transformPaymentStateAggregateRootToPayload(paymentStateAggregateRoot))),
                        Case($Left($()), throwable -> {
                            logger.error("Error in fetch payment Service. Caused by:" + throwable.getCause());
                            return Left(new ErrorPayload(500, throwable.getMessage()));
                        })));
    }

    /**
     * Method to fetch all payments made on the system
     * @return List of [PaymentStatePayload]
     */
    @Override
    public Future<Either<ErrorPayload, List<PaymentStatePayload>>> fetchAllPayments() {
        return paymentDAO.fetchAllPayments()
                .map(either -> Match(either).of(
                        Case($Right($()), paymentStateAggregateRootList -> Right(transformPaymentStateAggregateRootListToPayload(paymentStateAggregateRootList))),
                        Case($Left($()), throwable -> {
                            logger.error("Error in fetch payment Service. Caused by:" + throwable.getCause());
                            return Left(new ErrorPayload(500, throwable.getMessage()));
                        })));
    }

    /**
     * Get a list of [PaymentStateAggregateRoot] and transform into a list of [PaymentStatePayload]
     */
    private List<PaymentStatePayload> transformPaymentStateAggregateRootListToPayload(List<PaymentStateAggregateRoot> paymentStateAggregateRootList) {
        return paymentStateAggregateRootList.stream()
                .map(this::transformPaymentStateAggregateRootToPayload)
                .collect(Collectors.toList());
    }

    /**
     * Function to transform from the domain model [PaymentStateAggregateRoot] into payload type [PaymentStatePayload]
     */
    private PaymentStatePayload transformPaymentStateAggregateRootToPayload(PaymentStateAggregateRoot paymentStateAggregateRoot) {
        mapperFactory.classMap(PaymentStateAggregateRoot.class, PaymentStatePayload.class);
        MapperFacade mapper = mapperFactory.getMapperFacade();
        return mapper.map(paymentStateAggregateRoot, PaymentStatePayload.class);
    }


}
