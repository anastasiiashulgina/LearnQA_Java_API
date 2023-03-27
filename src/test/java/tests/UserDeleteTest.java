package tests;

import io.qameta.allure.*;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.DataGenetator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;


@Epic("User Delete cases")
@Feature("User delete")
public class UserDeleteTest {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Description("This test checks delete non-removable user")
    @DisplayName("Test positive delete info")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void testDeleteNonRemovableUser() {

        //LOGIN WITH non-removable user
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseLogUser = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        String userCookie = responseLogUser.getCookie("auth_sid");
        String userToken = responseLogUser.getHeader("x-csrf-token");
        //  int userId = responseGetAuth.jsonPath().get("user_id");
        //   System.out.println(userId);

        //DELETE USER
        Response responseDeleteAuthUser = apiCoreRequests
                .makeDeleteRequest("https://playground.learnqa.ru/api/user/2", userToken, userCookie);


        //GET ID non-removable user (to check it was not deleted)

        Response responseCheckAuth = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/2", userToken, userCookie);

        Assertions.assertResponseCodeEquals(responseDeleteAuthUser, 400);
        Assertions.assertResponseCodeEquals(responseCheckAuth, 200);
        Assertions.assertJsonByName(responseCheckAuth, "id", 2);

    }

    @Description("This test checks delete created user")
    @DisplayName("Test positive delete info")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testDeleteUser() {
        //GENERATE USER
        Map<String, String> userData = DataGenetator.getRegistrationData();


        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        String userId = responseCreateAuth.jsonPath().get("id");

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseLogUser = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);
        String userCookie = responseLogUser.getCookie("auth_sid");
        String userToken = responseLogUser.getHeader("x-csrf-token");

        //DELETE USER
        Response responseDeleteAuthUser = apiCoreRequests
                .makeDeleteRequest("https://playground.learnqa.ru/api/user/" + userId, userToken, userCookie);

        //GET ID non-removable user (to check it was not deleted)
        Response responseCheckAuth = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/" + userId, userToken, userCookie);

        Assertions.assertResponseCodeEquals(responseDeleteAuthUser, 200);
        Assertions.assertResponseCodeEquals(responseCheckAuth, 404);
        Assertions.assertResponseTextEquals(responseCheckAuth, "User not found");

    }

    @Description("This test checks delete user with another user auth")
    @DisplayName("Test negative delete info")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void testDeleteJustCreatedWithAnotherUserAuthTest() {
        //GENERATE USER 1 (for delete)
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
        String userCookie2 = responseCreateAuth2.getCookie("auth_sid");
        String userToken2 = responseCreateAuth2.getHeader("x-csrf-token");

        //DELETE USER 1
        Response responseDeleteAuthUser1 = apiCoreRequests
                .makeDeleteRequest("https://playground.learnqa.ru/api/user/" + userId1, userToken2, userCookie2);

        //GET info logged user 2 info Again
        Response responseCheckAuthUser2 = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/" + userId2, userToken2, userCookie2);

        //LOGIN with user 1
        Response responseGetAuth1 = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", userData1);

        //GET logged user 1 info
        Response responseUser1DataAfterDelete = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/"+userId1, responseGetAuth1.getHeader("x-csrf-token"), responseGetAuth1.getCookie("auth_sid"));

        Assertions.assertResponseCodeEquals(responseDeleteAuthUser1, 400);
        Assertions.assertResponseTextEquals(responseDeleteAuthUser1, "Auth token not supplied");
        Assertions.assertResponseCodeEquals(responseUser1DataAfterDelete, 200);
        Assertions.assertJsonByName(responseUser1DataAfterDelete, "id", userId1);
        Assertions.assertJsonByName(responseCheckAuthUser2, "id", userId2);



    }
}
