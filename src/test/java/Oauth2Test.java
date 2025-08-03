import io.restassured.path.json.JsonPath;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import static io.restassured.RestAssured.given;

public class Oauth2Test {

    public static void main(String[] args) throws InterruptedException {

        String url = "https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email" +
                "&auth_url=https://accounts.google.com/o/oauth2/v2/auth" +
                "&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com" +
                "&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php";
        WebDriver driver = new ChromeDriver();
        driver.get(url);

        WebElement emailField = driver.findElement(By.cssSelector("input[type='email']"));
        emailField.sendKeys("your_email");
        emailField.sendKeys(Keys.ENTER);
        Thread.sleep(2000);
        WebElement passwordField = driver.findElement(By.cssSelector("input[type='password']"));
        passwordField.sendKeys("your_password");
        passwordField.sendKeys(Keys.ENTER);
        Thread.sleep(2000);
        String currentUrl = driver.getCurrentUrl();
        String code = currentUrl.split("code=")[1].split("&scope")[0]; // Extract the authorization code from the URL


        String accessTokenResponse = given().urlEncodingEnabled(false).log().all()
                .queryParam("code", code)
                .queryParam("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
                .queryParam("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
                .queryParam("grant_type", "authorization_code")
                .queryParam("scope", "https://www.googleapis.com/auth/userinfo.email")
                .queryParam("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
                .when()
                .post("https://www.googleapis.com/oauth2/v4/token") //access token url to get our access token
                .then().log().all().assertThat().statusCode(200).extract().response().asString();

        JsonPath js = new JsonPath(accessTokenResponse);
        String accessToken = js.getString("access_token"); // Extract the access token from the


        String response = given()
                .queryParam("access_token", accessToken)
                .when()
                .get("https://rahulshettyacademy.com/getCourse.php")
                .then().log().all().assertThat().statusCode(200).extract().response().asString();
    }
}
