package com.politrons.application.resources;

import com.politrons.application.handler.PaymentHandler;
import com.politrons.application.service.PaymentService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.CompletionStage;

@Path("/payment")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PaymentResource {

    @Inject
    PaymentService service;

    @Inject
    PaymentHandler handler;

    @GET
    @Path("/version")
    public String version() {
        return "Payment API V1.0";
    }


    @GET
    @Path("/{paymentId}")
    public CompletionStage<String> getPaymentById(@PathParam("paymentId") String id) {
        return service.getUserAsync(Long.valueOf(id));
    }

    @GET
    @Path("/all")
    public CompletionStage<String> getAllPayment() {
        return null;
    }

    @POST
    public CompletionStage<String> createPayment() {
        return null;
    }

    @PUT
    public CompletionStage<String> updatePayment() {
        return null;
    }

    @DELETE
    public CompletionStage<String> deletePayment() {
        return null;
    }

}