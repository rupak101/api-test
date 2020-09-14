package com.api.clients;

import io.qameta.allure.Step;
import lombok.Builder;
import org.apache.http.HttpStatus;

import static com.api.RestAssuredHelper.statusMatcherFor;
import static com.api.framework.utils.getJsonSchemaFactory;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class RestAssuredDeviceListClient extends RestAssuredClient {

    private static final String DEVICES_PATH = "/devices";
    private static final String GET_DEVICES_ERROR_MESSAGE = "Get device error message";

    @Builder
    public RestAssuredDeviceListClient(boolean loggingEnabled) {
        this.loggingEnabled = loggingEnabled;
    }

    //Method to get the user client without login
    public static RestAssuredDeviceListClient getClientForDevices() {
        return RestAssuredDeviceListClient.builder()
                .loggingEnabled(false)
                .build();
    }

    @Step
    public String getDevices() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(DEVICES_PATH)
                .then()
                .assertThat()
                //validate device list schema
                .body(matchesJsonSchemaInClasspath("deviceList.json")
                        .using(getJsonSchemaFactory()))
                .statusCode(
                        statusMatcherFor(HttpStatus.SC_OK, GET_DEVICES_ERROR_MESSAGE)
                )
                .extract()
                .asString();
    }
}
