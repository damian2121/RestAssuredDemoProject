import files.payload;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

import java.sql.SQLOutput;

public class ComplexJsonParse {
    public static void main(String[] args) {
        // This class is a placeholder for complex JSON parsing examples.
        // You can implement your complex JSON parsing logic here.

        // Example: Parsing a complex JSON structure
        // Use libraries like Jackson or Gson for advanced JSON parsing if needed.

        JsonPath js = new JsonPath(payload.CoursePrice());
        //Print number of courses returned by API
        int count = js.getInt("courses.size()");
        System.out.println("Number of courses: " + count);

        //Print Purchase Amount
        int purchaseAmount = js.getInt("dashboard.purchaseAmount");
        System.out.println("Purchase Amount: " + purchaseAmount);

        //print Title of the first course
        String firstCourseTitle = js.getString("courses[0].title");
        System.out.println("First Course Title: " + firstCourseTitle);

        //Print All course titles and their respective prices
        for (int i = 0; i < count; i++) {
            String courseTitle = js.getString("courses[" + i + "].title");
            int coursePrice = js.getInt("courses[" + i + "].price");
            System.out.println("Course Title: " + courseTitle + ", Price: " + coursePrice);
        }

        //Print number of copies sold by RPA course
        for (int i = 0; i < count; i++) {
            String courseTitle = js.getString("courses[" + i + "].title");
            if (courseTitle.equalsIgnoreCase("RPA")) {
                int copiesSold = js.getInt("courses[" + i + "].copies");
                System.out.println("Copies sold by RPA course: " + copiesSold);
                break;

            }
        }

        //Verify if Sum of all Course prices matches Purchase Amount
        int sum = 0;
        for (int i = 0; i < count; i++) {
            int coursePrice = js.getInt("courses[" + i + "].price");
            int courseCopies = js.getInt("courses[" + i + "].copies");
            sum += coursePrice * courseCopies;
        }
        System.out.println("Calculated Sum of all course prices: " + sum);
        Assert.assertEquals(sum, purchaseAmount, "Sum of all course prices does not match Purchase Amount");
    }

}
