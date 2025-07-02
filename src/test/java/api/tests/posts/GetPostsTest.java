package api.tests.posts;

import api.config.BaseTest;
import api.models.Post;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class GetPostsTest extends BaseTest {


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
                .contentType("application/json")
                .when()
                .get("/posts/99999")
                .then().statusCode(404)
                .extract().response();

        String body = response.getBody().asString();
        Assert.assertEquals(body,"{}", "Body should be empty.");


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

    @Test
    public void getAllPosts() {
        Response response = RestAssured
                .given()
                .contentType("application/json")
                .when()
                .get("/posts")
                .then().statusCode(200)
                .extract().response();

        List<Map<String, Object>> posts = response.jsonPath().getList("$");
        Assert.assertEquals(posts.size(), 100, "Должно быть 100 постов");

        for (Map<String, Object> post : posts) {
            Assert.assertTrue(post.containsKey("userId"), "Нет поля userId");
            Assert.assertTrue(post.containsKey("id"), "Нет поля id");
            Assert.assertTrue(post.containsKey("title"), "Нет поля title");
            Assert.assertTrue(post.containsKey("body"), "Нет поля body");

            Assert.assertEquals(posts.size(), 100, "Expected 100 post, but got: " + posts.size());
        }
    }
}