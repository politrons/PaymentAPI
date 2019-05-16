package com.politrons.infrastructure.dao.impl;

import com.politrons.infrastructure.dao.PaymentDAO;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.CompletableFuture;

@ApplicationScoped
public class PaymentDAOImpl implements PaymentDAO {

    public CompletableFuture<String> searchUserById(Long userId) {
        return CompletableFuture.supplyAsync(() -> "politrons with id:" + userId);
    }

}
