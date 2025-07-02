package api.tests.posts;

import api.config.BaseTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DeletePostsTest extends BaseTest {

    @Test(description = "TC08 — Удаление поста с ID = 1")
    public void deletePostById() {

        Response response = RestAssured
                .given()
                .contentType("application/json")
                .pathParam("id", 1)
                .when()
                .delete("/posts/{id}")
                .then().statusCode(200)
                .extract().response();

        String responseBody = response.getBody().asString();
        Assert.assertEquals(responseBody.trim(), "{}", "Response body should be an empty JSON object");
    }

    @Test(description = "TC09 — Delete a post that does not exist (ID = 102)")
    public void deletePostByNonExistentId() {

        int nonExistentId  = 102;
        Response response = RestAssured
                .given()
                .contentType("application/json")
                .pathParam("id", nonExistentId)
                .when()
                .delete("/posts/{id}")
                .then().statusCode(200)
                .extract().response();

        String responseBody = response.getBody().asString();
        Assert.assertEquals(responseBody.trim(), "{}", "Response body should be an empty JSON object");
    }
}
