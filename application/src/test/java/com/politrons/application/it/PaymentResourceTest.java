package com.politrons.application.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.politrons.application.JsonUtils;
import com.politrons.application.model.payload.response.AddPaymentResponse;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.Header;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class PaymentResourceTest {

    ObjectMapper mapper = new ObjectMapper();

    //####################//
    //     HAPPY PATH     //
    //####################//

    @Test
    void versionEndpoint() {
        given()
                .when().get("/v1/payment/version")
                .then()
                .statusCode(200)
                .body(is("Payment API V1.0"));
    }

    @Test
    void addPaymentEndpoint() {
        given()
                .contentType("application/json")
                .header(new Header("Content-Type", "application/json"))
                .body(JsonUtils.addPaymentCommand())
                .when().post("/v1/payment/")
                .then()
                .statusCode(200)
                .body(is("{\"code\":200,\"payload\":\"1981\"}"));
    }

}