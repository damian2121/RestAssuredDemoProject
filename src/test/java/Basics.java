import files.payload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

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
        String updatedAddress = "70 winter walk, USA";

        //Update place with new address
        given()
            .log().all()
            .queryParam("key", "qaclick123")
            .header("Content-Type", "application/json")
            .body("""
                    {
                    "place_id": "%s",
                    "address":"%s",
                    "key":"qaclick123"
                    }
                    """.formatted(placeId, updatedAddress))
        .when()
            .put("/maps/api/place/update/json")
        .then()
                .log().all().assertThat().statusCode(200).body("msg", equalTo("Address successfully updated"));

        //Get place to validate the updated address
        String getAddressResponse = given()
            .log().all()
            .queryParam("key", "qaclick123")
            .queryParam("place_id", placeId)
        .when()
            .get("/maps/api/place/get/json")
        .then()
                .log().all().assertThat().statusCode(200).extract().response().asString();

        JsonPath jsGet = new JsonPath(getAddressResponse);
        String actualAddress = jsGet.getString("address");
        Assert.assertEquals(actualAddress, updatedAddress, "Address not updated correctly");
    }
}
