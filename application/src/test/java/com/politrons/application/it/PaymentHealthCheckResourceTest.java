package com.politrons.application.it;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.politrons.application.JsonUtils;
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
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class PaymentHealthCheckResourceTest {

    @BeforeAll
    static void init() {
        CassandraConnector.start();
    }

    @AfterAll
    static void stop() {
        CassandraConnector.stop();
    }

    @Test
    void healthCheckEndpoint() {
        given()
                .when().get("/health")
                .then()
                .statusCode(200)
                .body(containsString("\"Cassandra database running:\": true"));
    }


}