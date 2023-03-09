import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import io.restassured.path.json.JsonPath;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;

public class TokenTest {

    @Test
    public void checkToken() throws InterruptedException {


        JsonPath response = (JsonPath) RestAssured
                .given()
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();
        System.out.println("Task is ready: \n");
       response.prettyPrint();

        String tokenAnswer = response.get("token");
       // System.out.println(tokenAnswer);
        int secondsAnswer = response.get("seconds");
       // System.out.println(secondsAnswer);

        Map<String, String> token = new HashMap<>();
        token.put("token", tokenAnswer);
        JsonPath responseFast = RestAssured
                .given()
                .queryParams(token)
                .when()
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();
        System.out.println("Answer for the first request: \n");
       responseFast.prettyPrint();

        String answerForResponseFast = responseFast.get("status");

        if (answerForResponseFast.equals("Job is NOT ready")){
            System.out.println("Fast request for the task works correctly");
        } else {System.out.println("Fast request for the task doesn't work correctly");};

        Thread.sleep(secondsAnswer*1000+1);

        JsonPath responseLong = RestAssured
                .given()
                .queryParams(token)
                .when()
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();
        System.out.println("Answer for the second request: \n");
        responseLong.prettyPrint();

        String answerForResponseLong = responseLong.get("status");

        if (answerForResponseLong.equals("Job is ready") & responseLong.get("result") !=null){
            System.out.println("Long request for the task works correctly");
        } else {System.out.println("Long request for the task doesn't work correctly");};

  }

}
