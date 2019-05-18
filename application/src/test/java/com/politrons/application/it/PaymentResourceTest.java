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
import io.restassured.response.ResponseBody;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

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
        given()
                .contentType("application/json")
                .header(new Header("Content-Type", "application/json"))
                .body(JsonUtils.addPaymentCommand())
                .when().post("/v1/payment/")
                .then()
                .statusCode(200)
                .body(containsString("\"code\":200,\""));
    }

    @Test
    void fetchPaymentEndpoint() throws JsonProcessingException {

        String uuid = UUID.randomUUID().toString();
        PaymentAdded paymentAddedEvent = getPaymentAddedEvent();
        paymentAddedEvent.setId(uuid);
        String event = mapper.writeValueAsString(paymentAddedEvent);
        CassandraConnector.addPayment(getAddPaymentQuery(paymentAddedEvent, "12345", event));

        given()
                .contentType("application/json")
                .header(new Header("Content-Type", "application/json"))
                .when().get("/v1/payment/" + uuid)
                .then()
                .statusCode(200)
                .body(containsString("\"code\":200,\""));
    }


    String getAddPaymentQuery(PaymentAdded paymentAdded, String timeStampMillis, String event) {
        return "INSERT INTO " + "paymentsSchema.payment" +
                "(id, timestamp, event) " +
                "VALUES (" + paymentAdded.getId() + ", '" +
                timeStampMillis + "', '" +
                event + "');";
    }

    protected PaymentAdded getPaymentAddedEvent() {
        return new PaymentAdded(UUID.randomUUID().toString(), "payment", 0, getPaymentInfoDTO());
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