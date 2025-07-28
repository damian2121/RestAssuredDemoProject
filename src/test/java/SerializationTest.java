import io.restassured.RestAssured;
import org.testng.annotations.Test;
import pojo.AddPlace;
import pojo.Location;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class SerializationTest {

    @Test
    public void serializationTest() {

        AddPlace ap = new AddPlace();
        ap.setAccuracy(50);
        ap.setName("Frontline house");
        ap.setPhone_number("(+91) 983 893 3937");
        ap.setAddress("29, side layout, cohen 09");
        ap.setWebsite("http://google.com");
        ap.setLanguage("French-IN");
        List<String> myList = new ArrayList<>();
        myList.add("shoe park");
        myList.add("shop");
        ap.setTypes(myList);

        Location loc = new Location();
        loc.setLat(-38.383494);
        loc.setLng(33.427362);
        ap.setLocation(loc);


        RestAssured.baseURI = "https://rahulshettyacademy.com";
        String response = given()
                .log().all()
                .queryParam("key", "qaclick123")
                .header("Content-Type", "application/json")
                .body(ap)
                .when()
                .post("/maps/api/place/add/json")
                .then()
                .log().all()
                .assertThat().statusCode(200).extract().response().asString();
    }
}
