package tests;

import io.qameta.allure.*;
import io.restassured.response.Response;
import lib.DataGenetator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenetator;

import java.util.HashMap;
import java.util.Map;

@Epic("User edit cases")
@Feature("Edit User Info")
public class UserEditTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    String cookie;
    String header;

    @Description("This test checks edit information about user with auth")
    @DisplayName("Test positive put info")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testEditJustCreatedTest() {
        //GENERATE USER
        Map<String, String> userData = DataGenetator.getRegistrationData();


        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        String userId = responseCreateAuth.jsonPath().get("id");

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);
        this.cookie = this.getCookie(responseGetAuth, "auth_sid");
        this.header = this.getHeader(responseGetAuth, "x-csrf-token");

        //EDIT
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);
        Response responseEditData = apiCoreRequests
                .makePutRequest("https://playground.learnqa.ru/api/user/"+userId, this.header, this.cookie, editData);

        System.out.println(responseEditData.asString());

        //GET
        Response responseUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/"+userId, this.header, this.cookie);

        Assertions.assertJsonByName(responseUserData,"firstName", newName);

        System.out.println(responseUserData.asString());

    }

    @Description("This test checks edit information about user without auth")
    @DisplayName("Test negative put info")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void testEditJustCreatedWithoutAuthTest(){
        //GENERATE USER 1
        Map<String, String> userData = DataGenetator.getRegistrationData();


        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        String userId = responseCreateAuth.jsonPath().get("id");

        //EDIT
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);
        Response responseEditData = apiCoreRequests
                .makePutRequestWithoutTokenCookie("https://playground.learnqa.ru/api/user/"+userId, editData);

        //GET
        Response responseUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/"+userId, this.header, this.cookie);

        Assertions.assertResponseCodeEquals(responseEditData, 400);
        Assertions.assertResponseTextEquals(responseEditData, "Auth token not supplied");
        Assertions.assertJsonHasNotFields(responseUserData, "firstName");

    }

    @Description("This test checks edit information about user with another user auth")
    @DisplayName("Test negative put info")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void testEditJustCreatedWithAnotherUserAuthTest() {
        //GENERATE USER 1 (for edit)
        Map<String, String> userData1 = DataGenetator.getRegistrationData();

        Response responseCreateAuth1 = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData1);
        String userId1 = responseCreateAuth1.jsonPath().get("id");

        //GENERATE USER 2 (for auth)
        Map<String, String> userData2 = DataGenetator.getRegistrationData();

        Response responseCreateAuth2 = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData2);
        String userId2 = responseCreateAuth2.jsonPath().get("id");


        //LOGIN with user 2
        Response responseGetAuthUser2 = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", userData2);
        this.cookie = this.getCookie(responseGetAuthUser2, "auth_sid");
        this.header = this.getHeader(responseGetAuthUser2, "x-csrf-token");

        //EDIT USER 1 with auth 2 user
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);
        Response responseEditData = apiCoreRequests
                .makePutRequest("https://playground.learnqa.ru/api/user/"+userId1, this.header, this.cookie, editData);

        //GET logged user 2 info
        Response responseUser2Data = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/"+userId2, this.header, this.cookie);
       // System.out.println("Info about NOTedited user "+responseUser2Data.asString());


        //LOGIN with user 1
        Response responseGetAuth1 = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", userData1);

        //GET logged user 1 info
        Response responseUser1DataAfterEdit = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/"+userId1, responseGetAuth1.getHeader("x-csrf-token"), responseGetAuth1.getCookie("auth_sid"));
       // System.out.println( "Info about edited user "+responseUser1DataAfterEdit.asString());

        Assertions.assertResponseCodeEquals(responseEditData, 400);
        Assertions.assertJsonByName(responseUser2Data,"firstName", userData2.get("email"));
        Assertions.assertJsonByName(responseUser1DataAfterEdit,"firstName", userData1.get("firstName"));

    }

    @Description("This test checks edit information about user with auth, but with incorrect email")
    @DisplayName("Test negative put info")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void testEditJustCreatedWithIncorrectEmailTest() {
        //GENERATE USER
        Map<String, String> userData = DataGenetator.getRegistrationData();


        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        String userId = responseCreateAuth.jsonPath().get("id");

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);
        this.cookie = this.getCookie(responseGetAuth, "auth_sid");
        this.header = this.getHeader(responseGetAuth, "x-csrf-token");

        //EDIT
        String email = DataGenetator.getEmailWithoutAt();
        Map<String, String> editData = new HashMap<>();
        editData.put("email", email);
        Response responseEditData = apiCoreRequests
                .makePutRequest("https://playground.learnqa.ru/api/user/"+userId, this.header, this.cookie, editData);

        //GET
        Response responseUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/"+userId, this.header, this.cookie);



        Assertions.assertResponseCodeEquals(responseEditData, 400);
        Assertions.assertResponseTextEquals(responseEditData, "Invalid email format");
        Assertions.assertJsonByName(responseUserData,"email", userData.get("email"));
    }

    @Description("This test checks edit information about user with auth, but with short first name")
    @DisplayName("Test negative put info")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void testEditJustCreatedWithShortFirstNameTest() {
        //GENERATE USER
        Map<String, String> userData = DataGenetator.getRegistrationData();


        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        String userId = responseCreateAuth.jsonPath().get("id");

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);
        this.cookie = this.getCookie(responseGetAuth, "auth_sid");
        this.header = this.getHeader(responseGetAuth, "x-csrf-token");

        //EDIT
        String firstName = "q";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", "q");
        Response responseEditData = apiCoreRequests
                .makePutRequest("https://playground.learnqa.ru/api/user/"+userId, this.header, this.cookie, editData);

        //GET
        Response responseUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/"+userId, this.header, this.cookie);

        Assertions.assertResponseCodeEquals(responseEditData, 400);
        Assertions.assertJsonByName(responseEditData,"error", "Too short value for field firstName");
        Assertions.assertJsonByName(responseUserData,"firstName", userData.get("firstName"));
    }


}