package com.politrons.application.it;

import com.politrons.application.JsonUtils;
import com.politrons.application.model.payload.response.PaymentResponse;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.Header;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class PaymentResourceBackendDownTest {

    @Test
    void addPaymentEndpoint() {
        PaymentResponse response = given()
                .contentType("application/json")
                .header(new Header("Content-Type", "application/json"))
                .body(JsonUtils.paymentRequest())
                .when().post("/v1/payment/")
                .then()
                .statusCode(200)
                .extract()
                .as(PaymentResponse.class);
        assertEquals(response.getCode(), 500);
    }

    @Test
    void updatePaymentEndpoint() {
        PaymentResponse response = given()
                .contentType("application/json")
                .header(new Header("Content-Type", "application/json"))
                .body(JsonUtils.paymentRequest())
                .when().put("/v1/payment/" + "foo")
                .then()
                .statusCode(200)
                .extract()
                .as(PaymentResponse.class);
        assertEquals(response.getCode(), 500);
    }

    @Test
    void deletePaymentEndpoint() {
        PaymentResponse response = given()
                .contentType("application/json")
                .header(new Header("Content-Type", "application/json"))
                .when().delete("/v1/payment/" + "bla")
                .then()
                .statusCode(200)
                .extract()
                .as(PaymentResponse.class);
        assertEquals(response.getCode(), 500);
    }

    @Test
    void fetchPaymentEndpoint() {
        PaymentResponse response = given()
                .contentType("application/json")
                .header(new Header("Content-Type", "application/json"))
                .when().get("/v1/payment/" + "foo")
                .then()
                .statusCode(200)
                .extract()
                .as(PaymentResponse.class);
        assertEquals(response.getCode(), 500);
    }

    @Test
    void fetchAllPaymentEndpoint() {
        PaymentResponse response = given()
                .contentType("application/json")
                .header(new Header("Content-Type", "application/json"))
                .when().get("/v1/payment/all")
                .then()
                .statusCode(200)
                .extract()
                .as(PaymentResponse.class);
        assertEquals(response.getCode(), 500);
    }


}