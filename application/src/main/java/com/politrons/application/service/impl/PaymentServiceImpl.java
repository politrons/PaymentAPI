package com.politrons.application.service.impl;


import com.politrons.application.service.PaymentService;
import com.politrons.infrastructure.dao.PaymentDAO;
import com.politrons.infrastructure.dao.impl.PaymentDAOImpl;
import io.quarkus.vertx.ConsumeEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

/**
 * To use CDI and use bean-discovery, we have to use the @ApplicationScoped instead of @Singleton.
 * The differences are that Singleton cannot be injected in compilation time of the Jar.
 */
@ApplicationScoped
public class PaymentServiceImpl implements PaymentService {

    @Inject
    PaymentDAO dao;

    public CompletionStage<String> getUserAsync(Long id) {
        return dao.searchUserById(id)
                .thenApply(value -> value + " Using Async")
                .thenApply(String::toUpperCase);
    }


}
