import files.payload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class DynamicJson {

    @Test(dataProvider = "BooksData")
    public void addBook(String aisle, String isbn) {
        RestAssured.baseURI = "http://216.10.245.166";
        String response = given()
            .log().all()
            .header("Content-Type", "application/json")
            .body(payload.AddBook(aisle, isbn))
        .when()
            .post("/Library/Addbook.php")
        .then()
            .log().all()
            .assertThat().statusCode(200)
            .body("Msg", equalTo("successfully added")).extract().response().asString();

        JsonPath js = new JsonPath(response);
        String msg = js.getString("Msg");
        String bookId = js.getString("ID");
        Assert.assertEquals(msg, "successfully added", "Book not added successfully");
        System.out.println("Book ID: " + bookId);
    }

    @Test(dataProvider = "BooksData")
    public void deleteBook(String aisle, String isbn) {
        RestAssured.baseURI = "http://216.10.245.166";
        String response = given()
                .log().all()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"ID\": \"" + isbn + aisle + "\"\n" +
                        "}")
                .when()
                .post("/Library/DeleteBook.php")
                .then()
                .log().all()
                .assertThat().statusCode(200)
                .body("msg", equalTo("book is successfully deleted")).extract().response().asString();
        String msg = new JsonPath(response).getString("msg");
        Assert.assertEquals(msg, "book is successfully deleted", "Book not deleted successfully");
    }

    @Test(dataProvider = "BooksData")
    public void getBook(String aisle, String isbn) {
        RestAssured.baseURI = "http://216.10.245.166";
        String response = given()
                .log().all()
                .when()
                .get("/Library/GetBook.php?ID=" + isbn + aisle)
                .then()
                .log().all()
                .assertThat().statusCode(200)
                .body("aisle", equalTo(aisle))
                .body("isbn", equalTo(isbn))
                .extract().response().asString();

        System.out.println("Response: " + response);
    }

    @DataProvider(name = "BooksData")
    public Object[][] getData() {
        return new Object[][] {
            {"abc123xd", "lol"},
            {"xyz456xd", "testBook"},
            {"pqr789xd", "sampleBook"}
        };
    }
}
