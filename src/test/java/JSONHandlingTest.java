import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

public class JSONHandlingTest {

    /*
    * Test Cases to be covered up
    * 1. Print No of courses returned by API
    * 2.Print Purchase Amount
    * 3. Print Title of the first course
    * 4. Print All course titles and their respective Prices
    * 5. Print no of copies sold by Each Course
    * 6. Verify if Sum of all Course prices matches with Purchase Amount
    * */
    static JsonPath jp = new JsonPath(Payloads.coursesJson());


    //1. Print No of courses returned by API
    @Test(priority = 0)
    public void getNoOfCourse(){
        System.out.println("------Print No of courses returned by API-----");
        int courseSize = jp.getInt("courses.size()");
        System.out.println(courseSize);
        Assert.assertEquals(3,courseSize);
    }

    // 2.Print Purchase Amount
    @Test(priority = 1)
    public void getPurchaseAmount(){
        System.out.println("------Print Purchase Amount-----");
        int totalPurchaseAmt = jp.getInt("dashboard.purchaseAmount");
        System.out.println(totalPurchaseAmt);
        Assert.assertEquals(910,totalPurchaseAmt);
    }

    // 3. Print Title of the first course
    @Test(priority = 2)
    public void getFirstCourseTitle(){
        System.out.println("------Print Title of the first course-----");
        String courseTitle = jp.getString("courses[0].title");
        System.out.println(courseTitle);
        Assert.assertEquals("Selenium Python",courseTitle);
    }

    //4. Print All course titles and their respective Prices
    @Test(priority = 4)
    public void getAllCourseTitle(){
        System.out.println("------Print All course titles and their respective Prices-----");
        int courseCount = jp.getInt("courses.size()");
        String actualCourse[] = {"Selenium Python","Cypress","RPA"};
        for (int ite=0; ite<courseCount;ite++){
            System.out.println(jp.getString("courses["+ite+"].title"));
            Assert.assertEquals(actualCourse[ite],jp.getString("courses["+ite+"].title"));
        }
    }

    //Print no of copies sold by Each Course
    @Test(priority = 3)
    public void noOfCopiesSoldByEachCourse(){
        System.out.println("------Print no of copies sold by Each Course-----");
        int courseCount = jp.get("courses.size()");
        int noCopiesSold[] = {6,4,10};
        for (int ite=0; ite<courseCount;ite++){
            System.out.println(jp.get("courses["+ite+"].title")+" Copies sold: "+jp.get("courses["+ite+"].copies"));
            Assert.assertEquals(noCopiesSold[ite],jp.getInt("courses["+ite+"].copies"));
        }
    }

    // Verify if Sum of all Course prices matches with Purchase Amount
    @Test(priority = 5)
    public void verify_CoursePricesMatchesWithPurchaseAmount(){
        System.out.println("------Verify if Sum of all Course prices matches with Purchase Amount-----");
        int courseCount = jp.getInt("courses.size()");
        int expectedTotalPrice = jp.getInt("dashboard.purchaseAmount");
        System.out.println("Expected Total Price: "+expectedTotalPrice);
        int calculatedPrice=0;
        for (int ite=0; ite<courseCount; ite++){
            int copiesSold = jp.getInt("courses["+ite+"].copies");
            System.out.println("Copies sold: "+copiesSold);
            int pricePerCopy = jp.getInt("courses["+ite+"].price");
            System.out.println("Price per Copy: "+pricePerCopy);
            calculatedPrice = calculatedPrice + (copiesSold*pricePerCopy);
        }
        System.out.println("Calculated Actual price: "+calculatedPrice);
        Assert.assertEquals(expectedTotalPrice,calculatedPrice);
    }

}
