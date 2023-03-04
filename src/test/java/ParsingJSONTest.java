import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class ParsingJSONTest {

    @Test
    public void FirstParsingJSON() {

        JsonPath responseForFirstJSONTest = (JsonPath) RestAssured
                .given()
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();

        System.out.println ((String) responseForFirstJSONTest.get("messages[1].message"));

    }
}
