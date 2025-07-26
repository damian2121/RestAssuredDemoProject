import files.payload;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SumValidatiion {

    JsonPath js = new JsonPath(payload.CoursePrice());
    //Print number of courses returned by API
    int count = js.getInt("courses.size()");


    int purchaseAmount = js.getInt("dashboard.purchaseAmount");

    @Test
    public void sumOfCoursesValidation(){
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
