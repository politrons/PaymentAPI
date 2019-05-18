package com.politrons.application.resources;

import com.politrons.application.handler.PaymentHandler;
import com.politrons.application.model.command.AddPaymentCommand;
import com.politrons.application.model.payload.response.PaymentResponse;
import com.politrons.application.service.PaymentService;
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

    @GET
    @Path("/{paymentId}")
    public CompletionStage<String> getPaymentById(@PathParam("paymentId") String id) {
        logger.debug("Request to get Payment with id " + id);
        return null;
    }

    @GET
    @Path("/all")
    public CompletionStage<String> getAllPayment() {
        logger.debug("Request to get all Payment for user id ");
        return null;
    }

    /**
     * Endpoint to persist a payment. We receive a AddPaymentCommand which after being passed into the domain layer
     * it's persisted using the infra layer.
     * @param addPaymentCommand that contains the information of the payment to be created
     * @return a Future of the PaymentResponse with the operation code and the payload
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/")
    public CompletionStage<PaymentResponse<String>> addPayment(AddPaymentCommand addPaymentCommand) {
        return handler.addPayment(addPaymentCommand)
                .map(either -> Match(either).of(
                        Case($Right($()), id -> new PaymentResponse<>(200, id)),
                        Case($Left($()), errorPayload -> new PaymentResponse<>(errorPayload.code, errorPayload.cause))))
                .toCompletableFuture();
    }

    @PUT
    @Path("/")
    public CompletionStage<String> updatePayment() {
        return null;
    }

    @DELETE
    @Path("/")
    public CompletionStage<String> deletePayment() {
        return null;
    }

}