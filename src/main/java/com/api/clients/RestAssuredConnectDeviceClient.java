package com.api.clients;

import io.qameta.allure.Step;
import lombok.Builder;
import org.apache.http.HttpStatus;

import static com.api.RestAssuredHelper.statusMatcherFor;
import static com.api.framework.utils.getJsonSchemaFactory;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class RestAssuredConnectDeviceClient extends RestAssuredClient {

    private static final String CONNECT_DEVICE_PATH = "/connect";
    private static final String GET_CONNECT_DEVICES_ERROR_MESSAGE = "Get device connection error message";


    @Builder
    public RestAssuredConnectDeviceClient(boolean loggingEnabled) {
        this.loggingEnabled = loggingEnabled;
    }

    //Method to get the connect device client without login
    public static RestAssuredConnectDeviceClient getClientForConnectDevice() {
        return RestAssuredConnectDeviceClient.builder()
                .loggingEnabled(false)
                .build();
    }

    @Step
    public String connectDevice(String body) {
        return given()
                .spec(getBaseSpec())
                .body(body)
                .when()
                .post(CONNECT_DEVICE_PATH)
                .then()
                .assertThat()
                //validate success schema
                .body(matchesJsonSchemaInClasspath("successStatusSchema.json")
                        .using(getJsonSchemaFactory()))
                .statusCode(
                        statusMatcherFor(HttpStatus.SC_OK, GET_CONNECT_DEVICES_ERROR_MESSAGE)
                )
                .extract()
                .asString();
    }
}
