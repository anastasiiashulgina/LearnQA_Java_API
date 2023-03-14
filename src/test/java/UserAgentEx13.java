import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserAgentEx13 {

    @Test
    public void checkUserAgent(){

        Map<String, String> userAgentData = new HashMap<>();
        userAgentData.put("User-Agent","Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1");


        JsonPath response = RestAssured
                .given()
                .queryParams(userAgentData)
                .when()
                .get("https://playground.learnqa.ru/ajax/api/user_agent_check")
                .jsonPath();

        response.prettyPrint();
        // System.out.println((String) response.getHeader("User Agent"));
//       System.out.println(response.getString("device"));
//       System.out.println(response.getString("browser"));
//       System.out.println(response.getString("platform"));

    }

}
