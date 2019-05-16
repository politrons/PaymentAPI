package com.politrons.infrastructure.dao;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.CompletableFuture;

@ApplicationScoped
public interface PaymentDAO {

    CompletableFuture<String> searchUserById(Long userId);
}
