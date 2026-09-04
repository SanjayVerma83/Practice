package qumu;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class APIBase {

    private static final String BASE_URL = LoadProp.getproperty("BaseURL");
    private static final String API_KEY = LoadProp.getproperty("api.key");

    public static RequestSpecification request() {

        RestAssured.baseURI = BASE_URL;

        return RestAssured
                .given()
                .header("x-api-key", API_KEY)
                .header("Content-Type", "application/json");
    }
}