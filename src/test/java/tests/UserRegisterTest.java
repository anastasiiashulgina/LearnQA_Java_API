package tests;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenetator;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import lib.ApiCoreRequests;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

public class UserRegisterTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    private Map<String, String> userData = new HashMap<>();


//    @BeforeEach
//
//    public void setData() {
//        String email = DataGenetator.getRandomEmail();
//        userData.put("email", email);
//        userData.put("password", "1234");
//        userData.put("username", "learnqa");
//        userData.put("firstName", "learnqa");
//        userData.put("lastName", "learnqa");
//    }

    @Description("This test checks create user with existing email")
    @DisplayName("Test negative auth user")
    @Test
    public void testCreateUserWithExistingEmail() {
        String email = "vinkotov@example.com";
        userData.put("email", email);
        userData = DataGenetator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '" + email + "' already exists");
    }

    @Description("This test checks create user with correct parameters")
    @DisplayName("Test positive auth user")
    @Test
    public void testCreateUserSuccesfully() {


        Map <String, String> userData = DataGenetator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 200);
        Assertions.assertJsonHasField(responseCreateAuth, "id");
    }

    @Description("This test checks create user with incorrect email without @")
    @DisplayName("Test negative auth user")
    @Test
    public void testCreateUserWithInCorrectEmail() {
        String email = DataGenetator.getEmailWithoutAt();

        Map <String, String> userData = DataGenetator.getRegistrationData();
        userData.put("email", email);
        userData = DataGenetator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Invalid email format");
    }


    @Description("This test checks create user without one of parameters")
    @DisplayName("Test negative auth user")
    @ParameterizedTest
    @ValueSource(strings = {"email", "password", "username", "firstName", "lastName"})
    public void testCreateUserWithoutOneOfParametrs(String condition) {
        Map <String, String> userData = DataGenetator.getRegistrationData();
        userData = DataGenetator.getRegistrationData(userData);
        userData.remove(condition);

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The following required params are missed: " + condition);
    }

    @Description("This test checks create user with username in one symbol")
    @DisplayName("Test negative auth user")
    @Test
    public void testCreateUserWithUsernameOneSymbol() {

        Map <String, String> userData = DataGenetator.getRegistrationData();
        userData.put("username", "q");
        userData = DataGenetator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The value of 'username' field is too short");
    }


    @Description("This test checks create user with username with name more then 250 symbols")
    @DisplayName("Test negative auth user")
    @Test
    public void testCreateUserWithLongUsername() {

        String username = RandomStringUtils.randomAlphabetic(251);
        Map <String, String> userData = DataGenetator.getRegistrationData();
        userData.put("username", username);
        userData = DataGenetator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The value of 'username' field is too long");
    }

}
