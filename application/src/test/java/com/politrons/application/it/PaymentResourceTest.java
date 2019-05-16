package com.politrons.application.it;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.politrons.application.JsonUtils;
import com.politrons.application.model.TestJson;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;

import java.net.URI;

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
                .when().get("/payment/version")
                .then()
                .statusCode(200)
                .body(is("Payment API V1.0"));
    }

    @Test
    void addPaymentEndpoint() throws JsonProcessingException {
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String json = mapper.writeValueAsString(new TestJson("hello"));

//        given().accept(ContentType.JSON)
//                .body(JsonUtils.paymentInfoJson())
//                .when().post("/payment/")
//                .then()
//                .statusCode(200)
//                .body(is("1981"));

        given()
                .contentType("application/json")
                .header(new Header("Content-Type", "application/json"))
                .body(json)
                .when().post("/payment/test")
                .then()
                .statusCode(200)
                .body(is("1981"));
    }

}