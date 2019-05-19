package com.politrons.application.performance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.politrons.application.utils.PaymentResourceTestUtils;
import com.politrons.infrastructure.CassandraConnector;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;

@QuarkusTest
class PaymentResourcePerformanceTest extends PaymentResourceTestUtils {

    private final int numOfRequest = 100;//It should be at least 1000

    @BeforeAll
    static void init() {
        CassandraConnector.start();
    }

    @AfterAll
    static void stop() {
        CassandraConnector.stop();
    }

    @RepeatedTest(value = numOfRequest, name = RepeatedTest.LONG_DISPLAY_NAME)
    void performanceTest() throws JsonProcessingException {
        addPayment();
        updatePayment();
        deletePaymentEndpoint();
        fetchPaymentEndpoint();
        fetchAllPaymentEndpoint();
    }

}