import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static java.lang.System.*;

public class LongRedirectTest {

    @Test
    public void firstLongRedirect() {

        String location = "https://playground.learnqa.ru/api/long_redirect";
        int countRedirect = 1;


        Response responseForLongRedirectTest = getResponse(location);

        location = responseForLongRedirectTest.getHeader("Location");
        int statusCode = responseForLongRedirectTest.getStatusCode();


        while (statusCode != 200) {

            responseForLongRedirectTest = getResponse(location);

            countRedirect++;
            statusCode = responseForLongRedirectTest.getStatusCode();
            location = responseForLongRedirectTest.getHeader("Location");

        }

        System.out.println("count of redirects: " + countRedirect);
    }

    private static Response getResponse(String location) {
        Response responseForLongRedirectTest = RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get(location)
                .andReturn();
        return responseForLongRedirectTest;
    }
}
