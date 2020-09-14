package com.api.clients;

import io.qameta.allure.Step;
import lombok.Builder;
import org.apache.http.HttpStatus;

import static com.api.RestAssuredHelper.statusMatcherFor;
import static com.api.framework.utils.getJsonSchemaFactory;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class RestAssuredDeviceStateClient extends RestAssuredClient {

    private static final String DEVICE_STATE_PATH = "/state";
    private static final String GET_DEVICE_STATE_ERROR_MESSAGE = "Get device state error message";

    @Builder
    public RestAssuredDeviceStateClient(boolean loggingEnabled) {
        this.loggingEnabled = loggingEnabled;
    }

    //Method to get the user client without login
    public static RestAssuredDeviceStateClient getClientForDeviceState() {
        return RestAssuredDeviceStateClient.builder()
                .loggingEnabled(false)
                .build();
    }

    @Step
    public String getDeviceState() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(DEVICE_STATE_PATH)
                .then()
                .assertThat()
                //validate device state schema
                .body(matchesJsonSchemaInClasspath("deviceState.json")
                        .using(getJsonSchemaFactory()))
                .statusCode(
                        statusMatcherFor(HttpStatus.SC_OK, GET_DEVICE_STATE_ERROR_MESSAGE)
                )
                .extract()
                .asString();
    }
}
