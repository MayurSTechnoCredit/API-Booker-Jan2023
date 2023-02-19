package testscripts;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import constants.Status_Code;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import pojo.request.createbooking.Bookingdates;
import pojo.request.createbooking.CreateBookingRequest;

public class CreateBookingTest {
	
	CreateBookingRequest payload = new CreateBookingRequest();

	@BeforeMethod
	public void getToken() {

		RestAssured.baseURI = "https://restful-booker.herokuapp.com";

		Response res = RestAssured.given().log().all().headers("Content-Type", "application/json")
				.body("{\r\n" + "    \"username\" : \"admin\",\r\n" + "    \"password\" : \"password123\"\r\n" + "}")
				.when().post("/auth")
//				.then().assertThat().statuscode(200)
//				.extract()
//				.response()
		;

		// System.out.println(res.statusCode());
		Assert.assertEquals(res.statusCode(), 200);
//		System.out.println(res.asPrettyString());
		String token = res.jsonPath().get("token");
		System.out.println(token);
	}

	@Test
	public void createBookingTest() {
		
		createBooking payload = new createBookingTest();

		Response res = RestAssured.given().header("Content-Type", "application/json")
				.header("Accept", "application/json")
				.body("{\r\n" + "    \"firstname\" : \"Mayur\",\r\n" + "    \"lastname\" : \"Shende\",\r\n"
						+ "    \"totalprice\" : 123,\r\n" + "    \"depositpaid\" : true,\r\n"
						+ "    \"bookingdates\" : {\r\n" + "        \"checkin\" : \"2023-05-02\",\r\n"
						+ "        \"checkout\" : \"2023-07-02\"\r\n" + "    },\r\n"
						+ "    \"additionalneeds\" : \"Breakfast\"\r\n" + "}")
				.when().post("/booking");

		System.out.println(res.getStatusCode());
		System.out.println(res.getStatusLine());
		Assert.assertEquals(res.getStatusCode(), 200);
	}

	@Test
	public void createBookingTestWithPOJO() {

		Bookingdates bookingDates = new Bookingdates();
		bookingDates.setCheckin("2023-05-02");
		bookingDates.setCheckout("2023-05-02");

		CreateBookingRequest payload = new CreateBookingRequest();
		payload.setFirstname("Mayur");
		payload.setLastname("Shende");
		payload.setTotalprice(123);
		payload.setDepositpaid(true);
		payload.setAdditionalneeds("breakfast");
		payload.setBookingdates(bookingDates);

		Response res = RestAssured.given().header("Content-Type", "application/json")
				.header("Accept", "application/json").body(payload).log().all().when().post("/booking");

//		System.out.println(res.getStatusCode());
//		System.out.println(res.getStatusLine());
		Assert.assertEquals(res.getStatusCode(), Status_Code.OK);
		// Assert.assertTrue(Integer.valueOf(res.jsonPath().getInt("bookingid"))
		// instanceof Integer);
		int bookingId = res.jsonPath().getInt("bookingid"); //
		System.out.println(bookingId);
		Assert.assertTrue(bookingId > 0);

		Assert.assertEquals(res.jsonPath().getString("booking.firstname"), payload.getFirstname());
		Assert.assertEquals(res.jsonPath().getString("booking.lastname"), payload.getLastname());
		Assert.assertEquals(res.jsonPath().getInt("booking.totalprice"), payload.getTotalprice());
		Assert.assertEquals(res.jsonPath().getBoolean("booking.depositpaid"), payload.isDepositpaid());
		Assert.assertEquals(res.jsonPath().getString("booking.Bookingdates.checkin"),
				payload.getBookingdates().getCheckin());
		Assert.assertEquals(res.jsonPath().getString("booking.Bookingdates.checkout"),
				payload.getBookingdates().checkout);
		Assert.assertEquals(res.jsonPath().getString("booking.additionalneeds"), payload.getAdditionalneeds());

	}

	@Test(priority = 1 , enabled = false)
	public void getAllBookingTest() {

		int bookingId = 2522;
		Response res = RestAssured.given()
				.header("Accept", "application/json")
				.log().all()
				.when()
				.get("/booking");
		Assert.assertEquals(res.getStatusCode(), Status_Code.OK);
		List<Integer> listOfBookingIds = res.jsonPath().getList("bookingid");
		System.out.println(listOfBookingIds.size());
		Assert.assertTrue(listOfBookingIds.contains(bookingId));
	}
	
	
	@Test(priority = 2)
	public void getBookingTest() {

		int bookingId = 2522;
		Response res = RestAssured.given()
				.header("Accept", "application/json")
				.log().all()
				.when()
				.get("/booking/"+bookingId);
		Assert.assertEquals(res.getStatusCode(), Status_Code.OK);
		System.out.println(res.asPrettyString());
		validateResponse(res, payload, "");
	
	}
	
	@Test(priority = 2)
	public void getBookingIdDeserializedTest() {

		int bookingId = 2522;
		Response res = RestAssured.given()
				.header("Accept", "application/json")
				.log().all()
				.when()
				.get("/booking/"+bookingId);
		Assert.assertEquals(res.getStatusCode(), Status_Code.OK);
		System.out.println(res.asPrettyString());
		
		CreateBookingRequest responseBody = res.as(CreateBookingRequest.class);
		System.out.println(responseBody);
		
		//payload : all detail of request
		//resposeBody : all details from getBooking
		
//		Assert.assertEquals(payload.firstname, responseBody.firstname);
//		Assert.assertEquals(payload.lastname, responseBody.lastname);
		
		Assert.assertTrue(responseBody.equals(payload) );
	
	}
	
	
	@Test(priority = 3)
	public void updateBookingIdTest() {

		//int bookingId = 2522;
		payload.setFirstname("Damini");
		Response res = RestAssured.given()
				.header("Content-Type", "application/json")
				.header("Accept", "application/json")
				.header("Cookie","token=<token_value>")
				.log().all()
				.when()
				.put("/booking/"+bookingId);
		Assert.assertEquals(res.getStatusCode(), Status_Code.OK);
		System.out.println(res.asPrettyString());
		
		CreateBookingRequest responseBody = res.as(CreateBookingRequest.class);
		System.out.println(responseBody.equals(payload));
		
		//payload : all detail of request
		//resposeBody : all details from getBooking
		
//		Assert.assertEquals(payload.firstname, responseBody.firstname);
//		Assert.assertEquals(payload.lastname, responseBody.lastname);
		
		Assert.assertTrue(responseBody.equals(payload) );
	
	}
	
	


	@Test(enabled = false)

	public void createBookingTestInPlanMode() {

		String payload = "{\r\n" + "   "
				+ " \"username\" : \"admin\",\r\n" 
				+ "    \"password\" : \"password123\"\r\n"
				+ "}";
		
		RequestSpecification reqSpec = RestAssured.given();
		reqSpec.baseUri("https://restful-booker.herokuapp.com/");
		reqSpec.headers("Content-Type", "application/json");
		reqSpec.body(payload);
		Response res = reqSpec.post("/auth");

		Assert.assertEquals(res.statusCode(), 200);
		System.out.println(res.asPrettyString());
		
		private void validateResponse(Respose res, CreateBookingRequest payload, String object) {
			
			Assert.assertEquals(res.jsonPath().getString(object+"firstname"), payload.getFirstname());
			Assert.assertEquals(res.jsonPath().getString(object+"lastname"), payload.getLastname());
			Assert.assertEquals(res.jsonPath().getInt(object+"totalprice"), payload.getTotalprice());
			Assert.assertEquals(res.jsonPath().getBoolean(object+"depositpaid"), payload.isDepositpaid());
			Assert.assertEquals(res.jsonPath().getString(object+"Bookingdates.checkin"),
					payload.getBookingdates().getCheckin());
			Assert.assertEquals(res.jsonPath().getString(object+"Bookingdates.checkout"),
					payload.getBookingdates().checkout);
			Assert.assertEquals(res.jsonPath().getString(object+"additionalneeds"), payload.getAdditionalneeds());
			
		}

	}

}
