package com.api.clients;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import static com.api.RestAssuredConstants.BASIC_REQUEST_SPECIFICATION;
import static io.restassured.http.ContentType.JSON;

public class RestAssuredClient {

    protected static String baseUrl = "http://localhost";
    protected static boolean loggingEnabled = false;

    /**
     * @return RequestSpecification containing
     * base URL,
     * JSON content type,
     * default configs(logging, objectMapping)
     * and other inside
     */
    protected static RequestSpecification getBaseSpec() {
        return new RequestSpecBuilder()
                .addRequestSpecification(BASIC_REQUEST_SPECIFICATION)
                .setContentType(JSON)
                .setBaseUri(baseUrl)
                .setPort(8080)
                .build();
    }
}