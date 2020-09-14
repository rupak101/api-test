package com.api;

import org.hamcrest.Matcher;

import static org.hamcrest.CoreMatchers.describedAs;
import static org.hamcrest.CoreMatchers.is;

public class RestAssuredHelper {
    private static final String REQUEST_FAILED_MESSAGE = "assertion failed. %s request failed, please check the logs. Expected the HTTP status code <%s>";

    public static Matcher<Integer> statusMatcherFor(int httpCode, String customMessage) {
        return describedAs(
                String.format(REQUEST_FAILED_MESSAGE, customMessage, httpCode),
                is(httpCode)
        );
    }
}
