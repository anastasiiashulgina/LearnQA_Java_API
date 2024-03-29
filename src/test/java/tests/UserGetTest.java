package tests;

import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenetator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;


@Epic("User get info cases")
@Feature("Get User Info")
public class UserGetTest  extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    String cookie;
    String header;

    @Description("This test checks get information about user without auth")
    @DisplayName("Test negative get info")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void testGetUserDataNotAuth(){

        Response responseUserData = apiCoreRequests
                .makeGetRequestWithoutData("https://playground.learnqa.ru/api/user/2");

        Assertions.assertJsonHasField(responseUserData, "username");
        Assertions.assertJsonHasNotFields(responseUserData, "firstName");
        Assertions.assertJsonHasNotFields(responseUserData, "lastName");
        Assertions.assertJsonHasNotFields(responseUserData, "emailName");
    }

    @Description("This test checks get information about user with auth")
    @DisplayName("Test positive get info")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testGetUserDetailsAuthAsSameUser() {

        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        this.cookie = this.getCookie(responseGetAuth, "auth_sid");
        this.header = this.getHeader(responseGetAuth, "x-csrf-token");

        Response responseUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/2", this.header, this.cookie);

        String [] expectedFields = {"username", "firstName", "lastName", "email"};
        Assertions.assertJsonHasFields(responseUserData, expectedFields);
      //System.out.println(responseUserData.asString());
    }

    @Description("This test checks get information about another user with auth ")
    @DisplayName("Test negative get info")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testGetUserDetailsAuthAsAnotherUser(){

        //GENERATE NEW USER
        Map<String, String> userData = DataGenetator.getRegistrationData();


        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        String userId = responseCreateAuth.jsonPath().get("id");

        //AUTHORIZE EXIST USER
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        this.cookie = this.getCookie(responseGetAuth, "auth_sid");
        this.header = this.getHeader(responseGetAuth, "x-csrf-token");

        //GET INFORMATION ABOUT NEW USER
        String url = "https://playground.learnqa.ru/api/user/" + userId;

        Response responseUserData = apiCoreRequests
                .makeGetRequest(url, this.header, this.cookie);

        Assertions.assertJsonHasField(responseUserData, "username");
        Assertions.assertJsonHasNotFields(responseUserData, "firstName");
        Assertions.assertJsonHasNotFields(responseUserData, "lastName");
        Assertions.assertJsonHasNotFields(responseUserData, "emailName");

    }
}
