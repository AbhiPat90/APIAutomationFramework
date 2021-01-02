import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
// Written this DummyTest class for the new bee/layman person to API Automation
public class DummyTest {
    public static void main(String[] args) {
        // Very basic test cases, without use of testNG Annotation
        // This will help you to understand that why we need a Test Automation framework to automate our test cases
        // Also helps you to know why we need to streamline the execution if we do not bring Automation framework into the picture

        RestAssured.baseURI = "https://rahulshettyacademy.com";
        String key = "qaclick123";
        //POST
       String responsePost =  given().log().all().header("Content-Type","application/json")
                .queryParam("key",key)
                .body("{\n" +
                        "  \"location\": {\n" +
                        "    \"lat\": -43.383494,\n" +
                        "    \"lng\": 56.427362\n" +
                        "  },\n" +
                        "  \"accuracy\": 55,\n" +
                        "  \"name\": \"AutomationTest\",\n" +
                        "  \"phone_number\": \"(+91) 123 893 4656\",\n" +
                        "  \"address\": \"13, side layout, India\",\n" +
                        "  \"types\": [\n" +
                        "    \"shoe park\",\n" +
                        "    \"practice\"\n" +
                        "  ],\n" +
                        "  \"website\": \"http://google.com\",\n" +
                        "  \"language\": \"English-EN\"\n" +
                        "}")
                .when().post("/maps/api/place/add/json")
                .then().log().all().assertThat().statusCode(200).header("Connection", "Keep-Alive")
                    .extract().body().asString();

        JsonPath jp1 = new JsonPath(responsePost);
        String place_id = jp1.getString("place_id");

        //PUT --> Update Address
        String expectedAddress = "New Delhi, India";
        given().log().all()
                .header("Content-Type","application/json")
                .queryParam("key",key)
                .body("{\n" +
                        "\"place_id\":\""+place_id+"\",\n" +
                        "\"address\":\""+expectedAddress+"\",\n" +
                        "\"key\":\""+key+"\"\n" +
                        "}")
                .when().put("/maps/api/place/update/json")
                .then().log().all().assertThat().statusCode(200).body("msg",equalTo("Address successfully updated"));

        //GET --> Retrieve data to verify
        given().log().all()
                .queryParam("key",key).queryParam("place_id",place_id)
                .when().get("/maps/api/place/get/json")
                .then().log().all().assertThat().statusCode(200).body("address",equalTo(expectedAddress));

        //DELETE --> To remove the entry of the record from the server
        given().log().all()
                .header("Content-Type","application/json")
                .queryParam("key",key)
                .body("{\n" +
                        "    \"place_id\":\""+place_id+"\"\n" +
                        "}")
                .when().delete("/maps/api/place/delete/json")
                .then().log().all().assertThat().statusCode(200).body("status",equalTo("OK"));

        //GET --> to verify the entry actually got removed
        given().log().all()
                .queryParam("key",key)
                .queryParam("place_id", place_id)
                .when().get("/maps/api/place/get/json")
                .then().log().all().assertThat().statusCode(404).body("msg",equalTo("Get operation failed, looks like place_id  doesn't exists"));

        //404 = Request resource could not be found but may get available in future
    }
}
