package api.tests.posts;

import api.config.BaseTest;
import api.models.Post;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PostPostsTest extends BaseTest {

    @Test
    public void createPostAsString() {
        String newPost = """
                    {
                      "title": "Auto test post",
                      "body": "original content",
                      "userId": 88
                    }
                """;

        Response response = RestAssured
                .given()
                .contentType("application/json")
                .body(newPost)
                .when()
                .post("/posts")
                .then()
                .statusCode(201)
                .extract().response();

        Assert.assertEquals(response.jsonPath().getInt("id"), 101, "Invalid id.");
    }

    @Test
    public void createPostAsPOJO(){
        Post newPost = new Post();
        newPost.setTitle("title for test");
        newPost.setBody("new Body");
        newPost.setUserId(66);

        Response response = RestAssured
                .given()
                .contentType("application/json")
                .body(newPost)
                .when().post("/posts")
                .then().statusCode(201)
                .extract().response();

        Post createdPost = response.as(Post.class);

        Assert.assertEquals(createdPost.getId(), 101, "Invalid Id! Id should be 101.");
        Assert.assertEquals(createdPost.getUserId(), newPost.getUserId(), "Invalid userId.");
        Assert.assertEquals(createdPost.getTitle(), newPost.getTitle(), "Invalid title.");
    }
}
