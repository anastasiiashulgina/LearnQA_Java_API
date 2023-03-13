import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ClassForMistakes {

    @Test
    public void GetPasswordTest() {

        Map<String, String> params = new HashMap<>();
        params.put("login", "super_admin");
        Map<String, String> cookies = new HashMap<>();

            cookies.put("auth_cookie", "Football");

            JsonPath responseToCheckAuthCookie = RestAssured
                    .given()
                    .body(params)
                    .cookies(cookies)
                    .when()
                    .post("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                    .jsonPath();

        responseToCheckAuthCookie.prettyPrint();

           // System.out.println ((String) responseToCheckAuthCookie.get());
        }
}
