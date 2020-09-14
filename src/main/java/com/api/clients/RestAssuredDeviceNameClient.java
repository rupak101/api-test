package com.api.clients;

import io.qameta.allure.Step;
import lombok.Builder;
import org.apache.http.HttpStatus;

import static com.api.RestAssuredHelper.statusMatcherFor;
import static com.api.framework.utils.getJsonSchemaFactory;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class RestAssuredDeviceNameClient extends RestAssuredClient {

    private static final String DEVICE_NAME_PATH = "/name";
    private static final String GET_DEVICE_NAME_ERROR_MESSAGE = "Get device name error message";

    @Builder
    public RestAssuredDeviceNameClient(boolean loggingEnabled) {
        this.loggingEnabled = loggingEnabled;
    }

    //Method to get the device name client without login
    public static RestAssuredDeviceNameClient getClientForDeviceName() {
        return RestAssuredDeviceNameClient.builder()
                .loggingEnabled(false)
                .build();
    }

    @Step
    public String deviceName(String payload) {
        return given()
                .spec(getBaseSpec())
                .body(payload)
                .when()
                .post(DEVICE_NAME_PATH)
                .then()
                .assertThat()
                //validate success schema
                .body(matchesJsonSchemaInClasspath("successStatusSchema.json")
                        .using(getJsonSchemaFactory()))
                .statusCode(
                        statusMatcherFor(HttpStatus.SC_OK, GET_DEVICE_NAME_ERROR_MESSAGE)
                )
                .extract()
                .asString();
    }
}
