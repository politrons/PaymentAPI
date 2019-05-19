package com.politrons.application.volume;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.politrons.application.JsonUtils;
import com.politrons.application.model.payload.payload.PaymentStatePayload;
import com.politrons.application.model.payload.response.PaymentResponse;
import com.politrons.application.utils.PaymentResourceTestUtils;
import com.politrons.infrastructure.CassandraConnector;
import com.politrons.infrastructure.dto.BeneficiaryPartyDTO;
import com.politrons.infrastructure.dto.DebtorPartyDTO;
import com.politrons.infrastructure.dto.PaymentInfoDTO;
import com.politrons.infrastructure.dto.SponsorPartyDTO;
import com.politrons.infrastructure.events.PaymentAdded;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.Header;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Volume test are meant to be used to have a load of traffic for a big period of time to detect
 * some possible memory leaks in your application that, it might provoke that your application get
 * out of memory and die.
 */
@QuarkusTest
class PaymentResourceVolumeTest extends PaymentResourceTestUtils {

    private final int numOfRequest = 100;//This it should be at least 10k

    @BeforeAll
    static void init() {
        CassandraConnector.start();
    }

    @AfterAll
    static void stop() {
        CassandraConnector.stop();
    }

    @RepeatedTest(value = numOfRequest, name = RepeatedTest.LONG_DISPLAY_NAME)
    void volumeTest() throws JsonProcessingException {
        addPayment();
        updatePayment();
        deletePaymentEndpoint();
        fetchPaymentEndpoint();
        fetchAllPaymentEndpoint();
    }

}