package com.politrons.application.resources;

import com.politrons.application.handler.PaymentHandler;
import com.politrons.application.model.command.AddPaymentCommand;
import com.politrons.application.model.command.UpdatePaymentCommand;
import com.politrons.application.model.error.ErrorPayload;
import com.politrons.application.model.payload.response.PaymentResponse;
import com.politrons.application.service.PaymentService;
import io.vavr.control.Either;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.CompletionStage;

import static io.vavr.API.*;
import static io.vavr.Patterns.$Left;
import static io.vavr.Patterns.$Right;

@Path("/v1/payment")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PaymentResource {

    private Logger logger = LoggerFactory.getLogger(PaymentResource.class);

    @Inject
    PaymentService service;

    @Inject
    PaymentHandler handler;

    @GET
    @Path("/version")
    public String version() {
        logger.debug("Request to Version endpoint");
        return "Payment API V1.0";
    }

    /**
     * Endpoint to fetch a previous payment created.
     * We receive the id and we use to create the query to be passed into the service.
     * No transformation from Command into Domain into Event need it in Queries.
     * Only from domain into Payload to make our application not too much couple with domain.
     *
     * @param id of the payment to fetch
     * @return a PaymentResponse with the operation code and the payload as [PaymentStatePayload]
     */
    @GET
    @Path("/{paymentId}")
    public CompletionStage<PaymentResponse<?>> fetchPaymentById(@PathParam("paymentId") String id) {
        logger.debug("Request to get Payment with id " + id);
        return service.fetchPayment(id)
                .map(either -> Match(either).of(
                        Case($Right($()), paymentStatePayload -> new PaymentResponse<>(200, paymentStatePayload)),
                        Case($Left($()), errorPayload -> new PaymentResponse<>(errorPayload.code, errorPayload.cause))))
                .toCompletableFuture();
    }

    @GET
    @Path("/all")
    public CompletionStage<String> fetchAllPayment() {
        logger.debug("Request to get all Payments");
        service.fetchAllPayments();
        return null;
    }

    /**
     * Endpoint to persist a payment. We receive a AddPaymentCommand which after being passed into the domain layer
     * it's persisted using the infra layer.
     *
     * @param addPaymentCommand that contains the information of the payment to be created
     * @return a PaymentResponse with the operation code and the payload as eventId
     */
    @POST
    @Path("/")
    public CompletionStage<PaymentResponse<String>> addPayment(AddPaymentCommand addPaymentCommand) {
        logger.debug("Request to add Payment with command " + addPaymentCommand);
        return handler.addPayment(addPaymentCommand)
                .map(this::matchResponse)
                .toCompletableFuture();
    }

    /**
     * Endpoint where er receive the paymentId from the previous event as query param, and in the body of the request
     * the [UpdatePaymentCommand] to create a new Event with the new data updated.
     *
     * @param paymentId            of the previous event created
     * @param updatePaymentCommand the new data to create a new event.
     * @return a PaymentResponse with the operation code and the payload as eventId
     */
    @PUT
    @Path("/{paymentId}")
    public CompletionStage<PaymentResponse<String>> updatePayment(@PathParam("paymentId") String paymentId,
                                                                  UpdatePaymentCommand updatePaymentCommand) {
        logger.debug("Request to create update Payment event for paymentId " + paymentId);
        updatePaymentCommand.setPaymentId(paymentId);
        return handler.updatePayment(updatePaymentCommand)
                .map(this::matchResponse)
                .toCompletableFuture();
    }

    /**
     * Endpoint where er receive the paymentId from the previous event as query param to find the previous event
     * change the state to [deleted] and persist as a new Event.
     * @param paymentId of the previous event created
     * @return a PaymentResponse with the operation code and the payload as eventId
     */
    @DELETE
    @Path("/{paymentId}")
    public CompletionStage<PaymentResponse<String>> deletePayment(@PathParam("paymentId") String paymentId) {
        logger.debug("Request to create delete Payment event for paymentId " + paymentId);
        return handler.deletePayment(paymentId)
                .map(this::matchResponse)
                .toCompletableFuture();
    }


    private PaymentResponse<String> matchResponse(Either<ErrorPayload, String> either) {
        return Match(either).of(
                Case($Right($()), eventId -> new PaymentResponse<>(200, eventId)),
                Case($Left($()), errorPayload -> new PaymentResponse<>(errorPayload.code, errorPayload.cause)));
    }

}