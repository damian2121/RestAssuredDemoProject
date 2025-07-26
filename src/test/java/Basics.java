import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;

public class Basics {

    public static void main(String[] args) {

        //validate if Add Place API is working as expected
        RestAssured.baseURI = "https://rahulshettyacademy.com";
        given()
            .log().all()
            .queryParam("key", "qaclick123")
            .header("Content-Type", "application/json")
            .body("""
                    {
                      "location": {
                        "lat": -38.383494,
                        "lng": 33.427362
                      },
                      "accuracy": 50,
                      "name": "Frontline house",
                      "phone_number": "(+91) 983 893 3937",
                      "address": "29, side layout, cohen 09",
                      "types": [
                        "shoe park",
                        "shop"
                      ],
                      "website": "http://google.com",
                      "language": "French-IN"
                    }
                    """)
        .when()
            .post("/maps/api/place/add/json")
        .then()
                .log().all().assertThat().statusCode(200);

    }
}
