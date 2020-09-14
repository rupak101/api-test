package com.api.framework;

import com.github.fge.jsonschema.SchemaVersion;
import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import io.restassured.path.json.JsonPath;
import org.json.JSONObject;

public class utils {

    //Method to get json schema factory
    public static JsonSchemaFactory getJsonSchemaFactory() {
        return JsonSchemaFactory.newBuilder()
                .setValidationConfiguration(
                        ValidationConfiguration.newBuilder()
                                .setDefaultVersion(SchemaVersion.DRAFTV4).freeze())
                .freeze();
    }

    //Method to get json payload
    public static String getJsonPayload(String key, String value) {
        JSONObject json = new JSONObject();
        json.put(key, value);
        return json.toString();
    }

    //Method to get response as json
    public static JsonPath getJsonPath(String response) {
        return new JsonPath(response);
    }
}
