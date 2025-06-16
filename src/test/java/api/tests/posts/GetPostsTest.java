package api.tests.posts;

import api.config.BaseTest;
import api.models.Post;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class GetPostsTest extends BaseTest {

    @Test
    public void getAllPosts() {
        Response response = RestAssured
                .given()
                .when()
                .get("/posts")
                .then().extract().response();
        List<Object> posts = response.jsonPath().getList("");

        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code 200");
        Assert.assertFalse(posts.isEmpty(), "The posts list should not be empty.");
        Assert.assertEquals(posts.size(), 100, "Expected 100 posts, but got: " + posts.size());
    }

    @Test
    public void getPostById() {
        Response response = RestAssured
                .given()
                .pathParam("id", 1)
                .when()
                .get("/posts/{id}")
                .then()
                .statusCode(200)
                .extract().response();

        Post post = response.as(Post.class);

        Assert.assertEquals(post.getId(), 1);
        Assert.assertEquals(post.getUserId(), 1);
        Assert.assertEquals(post.getTitle(), "sunt aut facere repellat provident occaecati excepturi optio reprehenderit");
        Assert.assertNotNull(post.getBody(), "Body should not be null.");
    }

    @Test
    public void getPostByInvalidId() {

        Response response = RestAssured
                .given()
                .pathParam("id", 999)
                .when()
                .get("/posts/{id}")
                .then()
                .statusCode(404)
                .extract().response();

        Assert.assertEquals(response.getBody().toString(), "{}", "The body should be empty.");
    }

    @Test
    public void getPostByNegativeId() {

    Response response = RestAssured
            .given()
            .pathParam("id", -1)
            .when()
            .get("/posts/{id}")
            .then()
            .statusCode(404)
            .extract().response();

    String body = response.getBody().asString();
    Assert.assertEquals(body, "{}", "The body should be empty.");
}



}
