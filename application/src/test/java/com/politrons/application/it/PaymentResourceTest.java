package com.politrons.application.it;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.politrons.application.JsonUtils;
import com.politrons.application.model.payload.payload.PaymentStatePayload;
import com.politrons.application.model.payload.response.PaymentResponse;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class PaymentResourceTest {

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeAll
    static void init() {
        CassandraConnector.start();
    }

    @AfterAll
    static void stop() {
        CassandraConnector.stop();
    }

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
        PaymentResponse response = given()
                .contentType("application/json")
                .header(new Header("Content-Type", "application/json"))
                .body(JsonUtils.paymentRequest())
                .when().post("/v1/payment/")
                .then()
                .statusCode(200)
                .extract()
                .as(PaymentResponse.class);
        assertEquals(response.getCode(), 200);
        assertTrue(response.getPayload() instanceof String);
        assertFalse(((String) response.getPayload()).isEmpty());

    }

    @Test
    void updatePaymentEndpoint() throws JsonProcessingException {
        String uuid = addMockPayment();
        PaymentResponse response = given()
                .contentType("application/json")
                .header(new Header("Content-Type", "application/json"))
                .body(JsonUtils.paymentRequest())
                .when().put("/v1/payment/" + uuid)
                .then()
                .statusCode(200)
                .extract()
                .as(PaymentResponse.class);
        assertEquals(response.getCode(), 200);
        assertTrue(response.getPayload() instanceof String);
        assertFalse(((String) response.getPayload()).isEmpty());
    }

    @Test
    void deletePaymentEndpoint() throws JsonProcessingException {
        String uuid = addMockPayment();
        PaymentResponse response = given()
                .contentType("application/json")
                .header(new Header("Content-Type", "application/json"))
                .when().delete("/v1/payment/" + uuid)
                .then()
                .statusCode(200)
                .extract()
                .as(PaymentResponse.class);
        assertEquals(response.getCode(), 200);
        assertTrue(response.getPayload() instanceof String);
        assertFalse(((String) response.getPayload()).isEmpty());
    }

    @Test
    void fetchPaymentEndpoint() throws JsonProcessingException {
        String uuid = addMockPayment();
        PaymentResponse response = given()
                .contentType("application/json")
                .header(new Header("Content-Type", "application/json"))
                .when().get("/v1/payment/" + uuid)
                .then()
                .statusCode(200)
                .extract()
                .as(PaymentResponse.class);
        assertEquals(response.getCode(), 200);
        PaymentStatePayload paymentStatePayload = mapper.convertValue(response.getPayload(), PaymentStatePayload.class);
        assertEquals(paymentStatePayload.getType(), "created");
    }

    @Test
    void fetchPaymentEndpointWithWrongEventId() {
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
    void fetchAllPaymentEndpoint() throws JsonProcessingException {
        addMockPayment();
        addMockPayment();
        addMockPayment();
        PaymentResponse response = given()
                .contentType("application/json")
                .header(new Header("Content-Type", "application/json"))
                .when().get("/v1/payment/all")
                .then()
                .statusCode(200)
                .extract()
                .as(PaymentResponse.class);
        assertEquals(response.getCode(), 200);
        List<PaymentStatePayload> paymentStatePayloadList = mapper.convertValue(response.getPayload(), new TypeReference<ArrayList<PaymentStatePayload>>() {
        });
        assertEquals(paymentStatePayloadList.size(), 3);
        assertNotNull(paymentStatePayloadList.get(0).getPaymentInfo());
    }

    private String addMockPayment() throws JsonProcessingException {
        String uuid = UUID.randomUUID().toString();
        PaymentAdded paymentAddedEvent = getPaymentAddedEvent();
        paymentAddedEvent.setId(uuid);
        String event = mapper.writeValueAsString(paymentAddedEvent);
        CassandraConnector.addPayment(getAddPaymentQuery(paymentAddedEvent, "12345", event));
        return uuid;
    }


    String getAddPaymentQuery(PaymentAdded paymentAdded, String timeStampMillis, String event) {
        return "INSERT INTO " + "paymentsSchema.payment" +
                "(id, timestamp, event) " +
                "VALUES (" + paymentAdded.getId() + ", '" +
                timeStampMillis + "', '" +
                event + "');";
    }

    PaymentAdded getPaymentAddedEvent() {
        return new PaymentAdded(UUID.randomUUID().toString(), "created", 0, getPaymentInfoDTO());
    }

    PaymentInfoDTO getPaymentInfoDTO() {
        DebtorPartyDTO debtorParty = getDebtorParty();
        BeneficiaryPartyDTO beneficiaryParty = getBeneficiaryParty();
        SponsorPartyDTO sponsorParty = getSponsorParty();
        return new PaymentInfoDTO("amount", "currency",
                "paymentId",
                "paymentPurpose", "paymentType",
                "processingDate", "reference", "schemePaymentSubType",
                "schemePaymentType", debtorParty, sponsorParty, beneficiaryParty);
    }

    private SponsorPartyDTO getSponsorParty() {
        return new SponsorPartyDTO("accountName",
                "bankId", "bankCode");
    }

    private BeneficiaryPartyDTO getBeneficiaryParty() {
        return new BeneficiaryPartyDTO("accountName",
                "accountNumber", 0, "address", "bankId", "name");
    }

    private DebtorPartyDTO getDebtorParty() {
        return new DebtorPartyDTO("accountName", "accountNumber",
                0, "address", "bankId", "name");
    }

}