import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CheckCookieEx11 {

    @Test
    public void checkCookie(){

        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();
        //Map <String,String> cookieData = response.getCookies();
        //System.out.println(cookieData);
        assertEquals(response.cookie("HomeWork"), "hw_value");
    }
}
