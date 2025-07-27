import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;

import java.io.File;

import static io.restassured.RestAssured.*;

public class JiraCreateIssue {

    @Test
    public void createJiraIssueWithAttachment(){
        RestAssured.baseURI = "https://replace_with_your_jira_board.atlassian.net";

        String response = given()
            .log().all()
            .header("Content-Type", "application/json")
            .header("Authorization", "Basic base64_encoded_email_and_token") // Replace with your base64 encoded email and API token
            .body("""
                {
                    "fields": {
                        "project": {
                            "key": "SCRUM"
                        },
                        "summary": "Issue created via API with attachemntment",
                        "description": "Creating an issue using the Jira REST API",
                        "issuetype": {
                            "name": "Bug"
                        }
                    }
                }""")
        .when()
            .post("/rest/api/2/issue")
        .then()
            .log().all()
            .assertThat().statusCode(201)
            .extract().response().asString();

        System.out.println("Response: " + response);

        JsonPath js = new JsonPath(response);
        int issueId = js.getInt("id");

        //add attachment to the created issue
        given()
            .log().all()
            .header("X-Atlassian-Token", "no-check")
                .header("Authorization", "Basic base64_encoded_email_and_token")
            .multiPart("file", new File("your_file_ath_here")) // Replace with the path to your file
            .pathParam("issueId", issueId)
        .when()
            .post("/rest/api/2/issue/{issueId}/attachments")
        .then()
            .log().all()
            .assertThat().statusCode(200);
    }
}
