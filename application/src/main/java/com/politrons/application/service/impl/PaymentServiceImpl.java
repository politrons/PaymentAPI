package com.politrons.application.service.impl;


import com.politrons.application.model.error.ErrorPayload;
import com.politrons.application.model.payload.response.PaymentResponse;
import com.politrons.application.service.PaymentService;
import com.politrons.domain.PaymentAggregateRoot;
import com.politrons.infrastructure.dao.PaymentDAO;
import com.politrons.infrastructure.dao.impl.PaymentDAOImpl;
import com.politrons.infrastructure.repository.PaymentRepository;
import io.quarkus.vertx.ConsumeEvent;
import io.vavr.concurrent.Future;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

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
     * Function to fetch a payment previously created/updated/deleted.
     *
     * @param id of the payment
     * @return the Domain model PaymentAggregateRoot
     */
    public Future<Either<ErrorPayload, PaymentResponse<PaymentAggregateRoot>>> fetchPayment(String id) {
        return paymentDAO.fetchPayment(id)
                .map(either -> Match(either).of(
                        Case($Right($()), paymentAggregateRoot -> Right(new PaymentResponse<>(200, paymentAggregateRoot))),
                        Case($Left($()), throwable -> {
                            logger.error("Error in fetch payment Service. Caused by:" + throwable.getCause());
                            return Left(new ErrorPayload(500, throwable.getMessage()));
                        })));
    }


}
