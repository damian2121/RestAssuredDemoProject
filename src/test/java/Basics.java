import files.payload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class Basics {

    public static void main(String[] args) {

        //validate if Add Place API is working as expected
        RestAssured.baseURI = "https://rahulshettyacademy.com";
        String response = given()
            .log().all()
            .queryParam("key", "qaclick123")
            .header("Content-Type", "application/json")
            .body(payload.AddPlace())
        .when()
            .post("/maps/api/place/add/json")
        .then()
                .log().all().assertThat().statusCode(200).body("scope", equalTo("APP"))
                .header("Server", "Apache/2.4.52 (Ubuntu)")
                .extract().response().asString();

        JsonPath js = new JsonPath(response);
        String placeId = js.getString("place_id");
        
    }
}
