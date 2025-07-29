import io.restassured.builder.RequestSpecBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.Test;
import pojo.Login;
import pojo.LoginResponse;
import pojo.OrderDetail;
import pojo.Orders;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class ECommerceAPITest {

    //Spec builder for request
    RequestSpecification reqspec = new RequestSpecBuilder()
            .setBaseUri("https://rahulshettyacademy.com") // Replace with your e-commerce API base URI
            .setContentType("application/json") // Set the content type to JSON
            .build();

    @Test
    public void e2eTest(){
        //POJO for login new object
        Login login = new Login();
        login.setUserEmail("forsen@gmail.com");
        login.setUserPassword("Sebastianforsen789"); // Replace with your login credentials

        RequestSpecification reqLogin = given().log().all().spec(reqspec) // Use the request specification
                .body(login); // Replace with your login credentials

        LoginResponse lr = reqLogin.when().post("/api/ecom/auth/login") // Replace with the correct endpoint for login
                .then().log().all()
                .assertThat().statusCode(200) // Check for successful response
                .extract().response().as(LoginResponse.class);

        String token = lr.getToken(); // Extract the token from the response
        System.out.println("Token: " + token); // Print the token for debugging
        String userId = lr.getUserId(); // Extract the user ID from the response
        System.out.println("User ID: " + userId); // Print the user ID for debugging
        String message = lr.getMessage(); // Extract the message from the response
        Assert.assertEquals(message, "Login Successfully"); // Assert that the login was successful

        //Add product to cart
        RequestSpecification addProductBaseReq = new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com") // Replace with your e-commerce API base URI
                .addHeader("Authorization", token) // Set the Authorization header with the token
                .build();

        RequestSpecification reqAddProduct = given().log().all().spec(addProductBaseReq) // Use the request specification with the token
                .param("productName", "Forsen NEW NEW").param("productAddedBy", userId) // Use the extracted user ID
                .param("productCategory", "Games").param("productSubCategory", "Games")
                .param("productPrice", "1000").param("productDescription", "Forsen is a popular streamer")
                .param("productFor","GAMERS")
                .multiPart("productImage", new File("C:\\Users\\DAMIAN\\Desktop\\forsen.jpg"));


        String response = reqAddProduct.when()
                .post("/api/ecom/product/add-product") // Replace with the correct endpoint for adding to cart
                .then().log().all()
                .assertThat().statusCode(201).extract().response().asString(); //

        //Store productId from response with JsonPath
        JsonPath jsonPath = new JsonPath(response);
        String productId = jsonPath.getString("productId"); // Extract the product ID from the response
        System.out.println("Product ID: " + productId); // Print the product

        //Create Order
        RequestSpecification createOrderBaseReq = new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com")
                .setContentType("application/json")// Replace with your e-commerce API base URI
                .addHeader("Authorization", token) // Set the Authorization header
                .build();

        Orders orders = new Orders();
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setCountry("Poland");
        orderDetail.setProductOrderedId(productId);

        List<OrderDetail> orderDetailList = new ArrayList<>();
        orderDetailList.add(orderDetail); // Add the order detail to the list
        orders.setOrders(orderDetailList);// Use the extracted product ID


        RequestSpecification createOrderReq =  given().log().all().spec(createOrderBaseReq)
                .body(orders);

        String response2 = createOrderReq.when()
                .post("/api/ecom/order/create-order") // Replace with the correct endpoint for creating an order
                .then().log().all()
                .assertThat().statusCode(201) // Check for successful response
                .extract().response().asString(); // Extract the response as a string

        // Delete Product
        RequestSpecification deleteProductBaseReq = new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com") // Replace with your e-commerce API base URI
                .addHeader("Authorization", token) // Set the Authorization header
                .build();

        RequestSpecification deleteProductReq = given().log().all().spec(deleteProductBaseReq)
                .pathParam("productId", productId); // Use the extracted product ID

        String deleteResponse = deleteProductReq.when()
                .delete("/api/ecom/product/delete-product/{productId}") // Replace with the correct endpoint for deleting a product
                .then().log().all()
                .assertThat().statusCode(200) // Check for successful response
                .extract().response().asString(); // Extract the response as a string

        JsonPath deleteJsonPath = new JsonPath(deleteResponse);
        String deleteMessage = deleteJsonPath.getString("message");
        Assert.assertEquals(deleteMessage, "Product Deleted Successfully"); // Assert that the product was deleted successfully

    }
}
