package com.politrons.application.resource;

import com.politrons.application.service.PaymentService;
import com.politrons.application.service.impl.PaymentServiceImpl;
import io.vertx.axle.core.eventbus.EventBus;
import io.vertx.axle.core.eventbus.Message;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.CompletionStage;

@Path("/info")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PaymentResource {

    @Inject
    PaymentService service;

    @GET
    public String first_resource() {
        return "Version 1.0 of Quarkus in Politrons system";
    }


    /**
     * Quarkus allow return directly the CompletionStage, making the service [Serverless] since the transport it's agnostic.
     */
    @GET
    @Path("/user/async/{userId}")
    public CompletionStage<String> getUsersAsyncResource(@PathParam("userId") String id) {
        return service.getUserAsync(Long.valueOf(id));
    }

}