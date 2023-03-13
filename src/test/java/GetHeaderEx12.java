import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetHeaderEx12 {


    @Test
    public void checkHeader(){

        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/homework_header")
                .andReturn();

        //System.out.println((String) response.getHeaders());

        assertEquals((String) response.getHeader("x-secret-homework-header"), "Some secret value");
    }
}
