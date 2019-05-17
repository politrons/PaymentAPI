package com.politrons.application.unit;

import com.politrons.application.service.impl.PaymentServiceImpl;
import org.junit.jupiter.api.Test;

class PaymentServiceTest {

    @Test
    void testService(){
        PaymentServiceImpl paymentServiceImpl = new PaymentServiceImpl();
        assert(paymentServiceImpl.hashCode() > 0);
    }

}
