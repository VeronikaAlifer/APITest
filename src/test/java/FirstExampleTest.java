import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class FirstExampleTest {


    @Test
    public void firstTest() {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";


        Response response = RestAssured.
                given().
                when().
                get("/posts/1").
                then().
                extract().response();


        int id = response.jsonPath().getInt("id");
        int userId = response.jsonPath().getInt("userId");

        // обычные проверки TestNG
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(id, 1);
        Assert.assertEquals(userId, 1);
    }

    @Test
    public void test1() {

        RestAssured.baseURI = "https://jsonplaceholder.typicode.com/";

        Response response = RestAssured
                .given()
                .when().
                get("/posts/1")
                .then().extract().response();

        int status = response.getStatusCode();
        String title = response.jsonPath().getString("title");
        int userId = response.jsonPath().getInt("userId");
        int id = response.jsonPath().getInt("id");


        System.out.println(status);

        System.out.println(title);
        System.out.println(userId);
        System.out.println(id);

    }

    @Test
    public void test2() {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com/";
        Response response = RestAssured
                .when()
                .get("/posts")
                .then().extract().response();
        int status = response.getStatusCode();
        List<Object> posts = response.jsonPath().getList("");
        Assert.assertTrue(posts.size() > 0);
        Assert.assertEquals(posts.size(), 100);

        Map<String, Object> firstPost = response.jsonPath().getMap("[0]");
        Assert.assertTrue(firstPost.containsKey("userId"));
        Assert.assertTrue(firstPost.containsKey("id"));
        Assert.assertTrue(firstPost.containsKey("title"));

        System.out.println(status);

    }

    @Test
    public void test3() {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";

        Response response = RestAssured
                .given()
                .pathParam("id", 3)
                .queryParam("userId", 13)
                .when()
                .get("/posts/{id}")
                .then().extract().response();

        Assert.assertEquals(response.getStatusCode(), 200);
        String title = response.jsonPath().getString("title");
        System.out.println(title);
    }

    @Test
    public void test4() {

        String body =  """
            {
              "title": "New Post",
              "body": "Post content",
              "userId": 28
            }
        """;
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";

        Response response = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .post("/posts")
                .then().extract().response();
       int status = response.getStatusCode();


       Assert.assertEquals(status, 201);
       Assert.assertEquals(response.jsonPath().getInt("id"), 101);

    }
}
