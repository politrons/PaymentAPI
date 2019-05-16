package com.politrons.application;

import com.politrons.application.service.impl.PaymentServiceImpl;
import org.junit.jupiter.api.Test;

public class ServiceTest {

    @Test
    public void testService(){
        PaymentServiceImpl paymentServiceImpl = new PaymentServiceImpl();
        assert(paymentServiceImpl.hashCode() > 0);
    }

}
