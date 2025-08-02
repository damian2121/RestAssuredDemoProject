import static io.restassured.RestAssured.given;

public class GraphQLScript {
    public static void main(String[] args) {
        int episodeId = 15866; // Example episode ID
        given().log().all()
                .header("Content-Type", "application/json")
                .baseUri("https://rahulshettyacademy.com")
                .body("""
                        {
                          "query": "query{\\n  episode(episodeId: %d){\\n    id\\n    name\\n    air_date\\n    episode\\n  }\\n}",
                          "variables": null
                        }""".formatted(episodeId))
                .when()
                .post("/gq/graphql")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200);
    }
}
