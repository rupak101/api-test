package com.api.clients;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import lombok.Builder;
import org.apache.http.HttpStatus;

import static com.api.RestAssuredHelper.statusMatcherFor;
import static com.api.framework.utils.getJsonSchemaFactory;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class RestAssuredDisconnectDeviceClient extends RestAssuredClient {

    private static final String DEVICE_DISCONNECT_PATH = "/disconnect";
    private static final String GET_DEVICE_DISCONNECT_ERROR_MESSAGE = "Get device disconnect error message";

    @Builder
    public RestAssuredDisconnectDeviceClient(boolean loggingEnabled) {
        this.loggingEnabled = loggingEnabled;
    }

    //Method to get the device disconnect client without login
    public static RestAssuredDisconnectDeviceClient getClientForDisconnectDevice() {
        return RestAssuredDisconnectDeviceClient.builder()
                .loggingEnabled(false)
                .build();
    }

    @Step
    public ValidatableResponse deviceDisconnect() {
        return given()
                .spec(getBaseSpec())
                .when()
                .post(DEVICE_DISCONNECT_PATH)
                .then();
    }

    @Step
    public String disconnectDeviceResponse() {
        return deviceDisconnect()
                .assertThat()
                //validate success schema
                .body(matchesJsonSchemaInClasspath("successStatusSchema.json")
                        .using(getJsonSchemaFactory()))
                .statusCode(
                        statusMatcherFor(HttpStatus.SC_OK, GET_DEVICE_DISCONNECT_ERROR_MESSAGE)
                )
                .contentType("text/html; charset=UTF-8")
                .extract()
                .asString();
    }
}
