package com.politrons.application.service;

import com.politrons.application.model.error.ErrorPayload;
import com.politrons.application.model.payload.payload.PaymentStatePayload;
import com.politrons.application.service.impl.PaymentServiceImpl;
import io.vavr.concurrent.Future;
import io.vavr.control.Either;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public interface PaymentService {

    Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    Future<Either<ErrorPayload, PaymentStatePayload>>  fetchPayment(String id);

    Future<Either<ErrorPayload, List<PaymentStatePayload>>>  fetchAllPayments();
}
