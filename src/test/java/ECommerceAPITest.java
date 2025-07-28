import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.Test;
import pojo.Login;
import pojo.LoginResponse;

import static io.restassured.RestAssured.given;

public class ECommerceAPITest {

    //Spec builder for request
    RequestSpecification reqspec = new RequestSpecBuilder()
            .setBaseUri("https://rahulshettyacademy.com") // Replace with your e-commerce API base URI
            .setContentType("application/json") // Set the content type to JSON
            .build();

    @Test
    public void loginTest(){
        //POJO for login new object
        Login login = new Login();
        login.setUserEmail("forsen@gmail.com");
        login.setUserPassword("Sebastianforsen789"); // Replace with your login credentials

        RequestSpecification reqLogin = given().log().all().spec(reqspec) // Use the request specification
                .body(login); // Replace with your login credentials

        LoginResponse lr = reqLogin.when().post("/api/ecom/auth/login") // Replace with the correct endpoint for login
                .then().log().all()
                .assertThat().statusCode(200) // Check for successful response
                .extract().response().as(LoginResponse.class);

        String token = lr.getToken(); // Extract the token from the response
        System.out.println("Token: " + token); // Print the token for debugging
        String userId = lr.getUserId(); // Extract the user ID from the response
        System.out.println("User ID: " + userId); // Print the user ID for debugging
        String message = lr.getMessage(); // Extract the message from the response
        Assert.assertEquals(message, "Login Successfully"); // Assert that the login was successful
    }
}
