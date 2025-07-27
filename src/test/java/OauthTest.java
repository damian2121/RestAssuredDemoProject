import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;
import pojo.GetCourse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.*;

public class OauthTest {

    @Test
    public void oauthTest() {
        String clientId = "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com"; // Replace with your OAuth client ID
        String clientSecret = "erZOWM9g3UtwNRj340YYaK_W"; // Replace with your OAuth client secret
        RestAssured.baseURI = "https://rahulshettyacademy.com"; // Replace with your OAuth provider's base URI

        String[] expectedWebAutomationCourseTitles = {
                "Selenium Webdriver Java",
                "Cypress",
                "Protractor",
        };

        String response = given().log().all()
                .formParam("client_id", clientId)
                .formParam("client_secret", clientSecret)
                .formParam("grant_type", "client_credentials")
                .formParam("scope", "trust")
                .when()
                .post("/oauthapi/oauth2/resourceOwner/token") // Replace with the correct endpoint for token retrieval
                .then()
                .log().all()
                .assertThat()
                .statusCode(200).extract().response().asString(); // Check for successful response

        JsonPath js = new JsonPath(response);
        String accessToken = js.getString("access_token"); // Extract the access token from the

        GetCourse gc = given().log().all()
                .queryParam("access_token", accessToken) // Use the extracted access token
                .when()
                .get("/oauthapi/getCourseDetails")
                .then()
                .log().all().extract().response().as(GetCourse.class);//

        //Get price of SoapUI Webservices testing course
        int coursesSize = gc.getCourses().getApi().size();
        for (int i = 0; i < coursesSize; i++) {
            if (gc.getCourses().getApi().get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing")){
                System.out.println("Price of SoapUi course: " + gc.getCourses().getApi().get(i).getPrice());
                break;
            }
        }

        //Print all titles in webAutomation
        int webAutomationSize = gc.getCourses().getWebAutomation().size();
        ArrayList<String> webAutomationTitles = new ArrayList<>();
        for (int i = 0; i < webAutomationSize; i++) {
            //Assert.assertEquals(expectedWebAutomationCourseTitles[i], gc.getCourses().getWebAutomation().get(i).getCourseTitle());
            webAutomationTitles.add(gc.getCourses().getWebAutomation().get(i).getCourseTitle());
        }
        //System.out.println("Web Automation Course Titles: " + webAutomationTitles);
        List<String> a =  Arrays.asList(expectedWebAutomationCourseTitles);
        Assert.assertEquals(a, webAutomationTitles, "Web Automation course titles do not match expected values.");

    }

}
