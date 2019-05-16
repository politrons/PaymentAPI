package com.politrons.application.service;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public interface PaymentService {

    CompletionStage<String> getUserAsync(Long id);
}
