import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class RedirectTest {


    @Test
    public void firstRedirect() {

        Response responseForFirstRedirect =  RestAssured
               .given()
                .redirects()
                .follow(false)
                .when()
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();

        String locationHeaders = responseForFirstRedirect.getHeader("Location");
        System.out.println(locationHeaders);

    }
}



