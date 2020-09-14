package com.api.clients;

import io.qameta.allure.Step;
import lombok.Builder;
import org.apache.http.HttpStatus;

import static com.api.RestAssuredHelper.statusMatcherFor;
import static com.api.framework.utils.getJsonSchemaFactory;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class RestAssuredDeviceBrightnessClient extends RestAssuredClient {

    private static final String DEVICE_BRIGHTNESS_PATH = "/brightness";
    private static final String GET_DEVICE_BRIGHTNESS_ERROR_MESSAGE = "Get device brightness error message";

    @Builder
    public RestAssuredDeviceBrightnessClient(boolean loggingEnabled) {
        this.loggingEnabled = loggingEnabled;
    }

    //Method to get the device brightness client without login
    public static RestAssuredDeviceBrightnessClient getClientForDeviceBrightness() {
        return RestAssuredDeviceBrightnessClient.builder()
                .loggingEnabled(false)
                .build();
    }

    @Step
    public String deviceBrightness(String payload) {
        return given()
                .spec(getBaseSpec())
                .body(payload)
                .when()
                .post(DEVICE_BRIGHTNESS_PATH)
                .then()
                .assertThat()
                //validate update status schema
                .body(matchesJsonSchemaInClasspath("successStatusSchema.json")
                        .using(getJsonSchemaFactory()))
                .statusCode(
                        statusMatcherFor(HttpStatus.SC_OK, GET_DEVICE_BRIGHTNESS_ERROR_MESSAGE)
                )
                .extract()
                .asString();
    }
}
