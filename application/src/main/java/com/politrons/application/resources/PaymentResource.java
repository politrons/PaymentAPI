package com.politrons.application.resources;

import com.politrons.application.handler.PaymentHandler;
import com.politrons.application.model.command.AddPaymentCommand;
import com.politrons.application.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Path("/v1/payment")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PaymentResource {

    Logger logger = LoggerFactory.getLogger(PaymentResource.class);

    @Inject
    PaymentService service;

    @Inject
    PaymentHandler handler;

    @GET
    @Path("/version")
    public String version() {
        logger.info("Request to Version endpoint");
        return "Payment API V1.0";
    }


    @GET
    @Path("/{paymentId}")
    public CompletionStage<String> getPaymentById(@PathParam("paymentId") String id) {
        logger.info("Request to get Payment with id " + id);
        return service.getUserAsync(Long.valueOf(id));
    }

    @GET
    @Path("/all")
    public CompletionStage<String> getAllPayment() {
        logger.info("Request to get all Payment for user id ");
        return null;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/")
    public CompletionStage<String> addPayment(AddPaymentCommand addPaymentCommand) {
        return CompletableFuture.supplyAsync(() -> "1981");
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