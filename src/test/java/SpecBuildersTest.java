import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.Test;
import pojo.AddPlace;
import pojo.Location;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class SpecBuildersTest {

    @Test
    public void specBuilderTest() {

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

        RequestSpecification reqspec = new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com")
                .addQueryParam("key", "qaclick123")
                .setContentType("application/json")
                .build();


        ResponseSpecification resspec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectHeader("Server", "Apache/2.4.52 (Ubuntu)")
                .expectContentType("application/json")
                .build();

        RequestSpecification request = given()
                .log().all().spec(reqspec).body(ap);

        Response response = request.when()
                .post("/maps/api/place/add/json")
                .then()
                .log().all()
                .spec(resspec).extract().response();
    }
}
