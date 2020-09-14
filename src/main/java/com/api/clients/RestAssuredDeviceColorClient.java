package com.api.clients;

import io.qameta.allure.Step;
import lombok.Builder;
import org.apache.http.HttpStatus;

import static com.api.RestAssuredHelper.statusMatcherFor;
import static com.api.framework.utils.getJsonSchemaFactory;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class RestAssuredDeviceColorClient extends RestAssuredClient {

    private static final String DEVICE_COLOR_PATH = "/color";
    private static final String GET_DEVICE_COLOR_ERROR_MESSAGE = "Get device color error message";

    @Builder
    public RestAssuredDeviceColorClient(boolean loggingEnabled) {
        this.loggingEnabled = loggingEnabled;
    }

    //Method to get the device color client without login
    public static RestAssuredDeviceColorClient getClientForDeviceColor() {
        return RestAssuredDeviceColorClient.builder()
                .loggingEnabled(false)
                .build();
    }

    @Step
    public String deviceColor(String payload) {
        return given()
                .spec(getBaseSpec())
                .body(payload)
                .when()
                .post(DEVICE_COLOR_PATH)
                .then()
                .assertThat()
                //validate update status schema
                .body(matchesJsonSchemaInClasspath("successStatusSchema.json")
                        .using(getJsonSchemaFactory()))
                .statusCode(
                        statusMatcherFor(HttpStatus.SC_OK, GET_DEVICE_COLOR_ERROR_MESSAGE)
                )
                .extract()
                .asString();
    }
}
