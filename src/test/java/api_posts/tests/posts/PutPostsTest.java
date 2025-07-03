package api_posts.tests.posts;

import api_posts.config.BaseTest;
import api_posts.models.Post;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PutPostsTest extends BaseTest {

    @Test(description = "✅ TC06 — Обновление поста с ID = 1 с новыми данными")
    public void updatePostWithNewData() {

        // // Получение текущего поста
        int postId = 1;
        Response getResponse = RestAssured
                .given()
                .pathParam("id", postId)
                .when()
                .get("/posts/{id}")
                .then().statusCode(200)
                .extract().response();
        Post originalPost = getResponse.as(Post.class);

        // Обновлённый пост
        Post updatedPost = new Post();
        updatedPost.setUserId(1);
        updatedPost.setTitle("Updated Title");
        updatedPost.setBody("Updated body content");

        // Отправка PUT-запроса
        Response putResponse = RestAssured
                .given()
                .contentType("application/json")
                .pathParam("id", postId)
                .body(updatedPost)
                .when()
                .put("/posts/{id}")
                .then().statusCode(200)
                .extract().response();

        Post actualPost = putResponse.as(Post.class);

        Assert.assertEquals(actualPost.getId(), postId);
        Assert.assertEquals(actualPost.getUserId(), updatedPost.getUserId());
        Assert.assertEquals(actualPost.getTitle(), updatedPost.getTitle());
        Assert.assertEquals(actualPost.getBody(), updatedPost.getBody());
    }

    @Test(description = "❌ TC07 —  Update a non-existent post with ID = 9999")
    public void updatePostsWithInvalidId() {
        final int invalidId = 9999;
        final int expectedStatusCode = 500;

        // Prepare updated post body
        Post updatedPost = new Post();
        updatedPost.setUserId(1);
        updatedPost.setTitle("Updated Title");
        updatedPost.setBody("Updated body content");

        // Send PUT request
        Response putResponse = RestAssured
                .given()
                .contentType("application/json")
                .pathParam("id", invalidId)
                .body(updatedPost)
                .when()
                .put("/posts/{id}")
                .then().statusCode(expectedStatusCode)
                .extract().response();

        Assert.assertTrue(putResponse.asString().contains("Cannot read properties of undefined (reading 'id')"));
    }

}
