package testscripts;

import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class DemoTest {
	
	@Test
	
	public void phoneNumbersTypeTest() {
		RestAssured.baseURI = "https://0e686aed-6e36-4047-bcb4-a2417455c2d7.mock.pstmn.io";
		
		Response res = RestAssured.given()
			.headers("Accept", "application/json")
			.when()
			.get("/test");
		
		System.out.println(res.asPrettyString());
		List<String> listOfType = res.jsonPath().getList("phoneNumbers.type");
		System.out.println(listOfType);
	}

    @Test
	
	public void phoneNumbers() {
		RestAssured.baseURI = "https://0e686aed-6e36-4047-bcb4-a2417455c2d7.mock.pstmn.io";
		
		Response res = RestAssured.given()
			.headers("Accept", "application/json")
			.when()
			.get("/test");
		
		System.out.println(res.asPrettyString());
		List<String> listOfPhoneNumber = res.jsonPath().getList("phoneNumbers");
		System.out.println(listOfPhoneNumber.size());
		System.out.println(listOfPhoneNumber);
		
		Map<String,String> mapOfPhonenumber = (Map<String,String>)listOfPhoneNumber.get(0);
		System.out.println(mapOfPhonenumber.get("type") + "---" + mapOfPhonenumber.get("number"));
		if (mapOfPhonenumber.get("type").equals("iphone"))
			Assert.assertEquals(false, null);
	}
}
