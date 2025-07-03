package api_reqres.test;

import api_reqres.config.BaseTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {
    private static final String URL_LOGIN = "/api/login";
    private static final String MISSING_EMAIL_MESSAGE = "Missing email or username";
    private static final String MISSING_PASSWORD_MESSAGE = "Missing password";
    private static final String USER_NOT_FOUND_MESSAGE = "user not found";
    private static final int BAD_REQUEST_CODE = 400;


    @Test(description = "TC001 - Verifies that a user can successfully log in with valid credentials and receive a token.")
    public void testSuccessfulLogin() {
        String body = """
                 { 
                "email": "eve.holt@reqres.in",
                 "password": "cityslicka"
                  } """;

        Response response = sendLoginRequest(body);

        String token = response.jsonPath().getString("token");
        Assert.assertNotNull(token, "Token is null!");
        Assert.assertFalse(token.isEmpty(), "Token is empty!");
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(description = "TC002 - Validates that the API returns an error when the email field is missing from the request.")
    public void testMissingEmail() {
        String invalidBody = """
                {"password": "cityslicka"}
                 """;

        Response response = sendLoginRequest(invalidBody);

        String error = response.jsonPath().getString("error");
        Assert.assertEquals(error, MISSING_EMAIL_MESSAGE, "Unexpected error message");
        Assert.assertEquals(response.getStatusCode(), BAD_REQUEST_CODE);

    }

    @Test(description = "TC003 - Checks that the login fails if the password field is missing.")
    public void testMissingPassword() {
        String invalidBody = """
                {"email": "eve.holt@reqres.in"}""";

        Response response = sendLoginRequest(invalidBody);

        String error = response.jsonPath().getString("error");
        Assert.assertEquals(error, MISSING_PASSWORD_MESSAGE, "Unexpected error message");
        Assert.assertEquals(response.getStatusCode(), BAD_REQUEST_CODE);
    }

    @Test(description = "TC004 - Ensures login fails when using a non-existent or incorrect email.")
    public void testInvalidEmail() {
        String invalidBody = """
                {"email":"wrong@email.com",
                 "password": "cityslicka"
                }
                """;

        Response response = sendLoginRequest(invalidBody);

        String error = response.jsonPath().getString("error");
        Assert.assertEquals(error, USER_NOT_FOUND_MESSAGE, "Unexpected error message");
        Assert.assertEquals(response.getStatusCode(), BAD_REQUEST_CODE);

    }

    @Test(description = "TC005 — Verifies that login fails if the password is incorrect even if the email is valid.")
    public void testInvalidPassword() {
        String invalidBody = """
                {"email": "eve.holt@reqres.in",
                 "password": "wrongPassword"
                }
                """;

        Response response = sendLoginRequest(invalidBody);

        String error = response.jsonPath().getString("error");
        Assert.assertEquals(error, USER_NOT_FOUND_MESSAGE, "Unexpected error message");
        Assert.assertEquals(response.getStatusCode(), BAD_REQUEST_CODE);
    }

    @Test(description = "TC006 — Verifies error response when the request body is empty.")
    public void testEmptyRequestBody() {

        String emptyBody = """
                {}""";

        Response response = sendLoginRequest(emptyBody);

        String error = response.jsonPath().getString("error");
        Assert.assertEquals(error, MISSING_EMAIL_MESSAGE);
        Assert.assertEquals(response.getStatusCode(), BAD_REQUEST_CODE);
    }

    @Test(description = "TC007 —  Validates that the endpoint does not allow methods other than POST (e.g., GET)")
    public void testWrongHTTPMethod() {
        Response response = RestAssured
                .given()
                .header("X-Api-Key", "reqres-free-v1")
                .contentType(ContentType.JSON)
                .when()
                .get(URL_LOGIN)
                .then().statusCode(BAD_REQUEST_CODE)
                .extract().response();
    }

    private Response sendLoginRequest (String body) {
        return  RestAssured
                .given()
                .header("X-Api-Key", "reqres-free-v1")
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(URL_LOGIN)
                .then()
                .extract().response();
    }
}
