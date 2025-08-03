import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class HashMapExcelTest {
    public static void main(String[] args) {

        HashMap<String, String> jsonAsMap = new HashMap<>();
        jsonAsMap.put("name", "Learn how to automate with RestAssured");
        jsonAsMap.put("isbn", "1234567890");
        jsonAsMap.put("aisle", "123");
        jsonAsMap.put("author", "Jane Doe");

        given()
            .log().all()
                .baseUri("https://rahulshettyacademy.com")
            .header("Content-Type", "application/json")
            .body(jsonAsMap)
        .when()
            .post("/Library/Addbook.php")
        .then()
                .log().all()
                .assertThat()
                .statusCode(200);
    }
}
