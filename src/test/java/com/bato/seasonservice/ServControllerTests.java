package com.bato.seasonservice;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;


@SpringBootTest
class ServControllerTests {

    private static final String requestBody = "{\"name\": \"Поступление в первый класс\",\"servLimit\": 500}";

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8080/api";
    }


    @Test
    public void getServsTest() {
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/servs")
                .then()
                .extract().response();
        Assertions.assertEquals(200, response.getStatusCode());
    }

    @Test
    public void getServsWithQueryParam() {
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/servs/3")
                .then()
                .extract().response();
        Assertions.assertEquals(200, response.getStatusCode());
    }

    @Test
    public void postServs() {
        Response response = given()
                .contentType(ContentType.JSON)
                .and()
                .body(requestBody)
                .when()
                .post("/servs")
                .then()
                .extract().response();
        Assertions.assertEquals(201, response.getStatusCode());
    }

    @Test
    public void putServs() {
        Response response = given()
                .contentType(ContentType.JSON)
                .and()
                .body(requestBody)
                .when()
                .put("/servs/1")
                .then()
                .extract().response();
        Assertions.assertEquals(200, response.getStatusCode());
    }

    @Test
    public void deleteServs() {
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/servs/1")
                .then()
                .extract().response();
        Assertions.assertEquals(204, response.getStatusCode());
    }
}

