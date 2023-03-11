import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LengthCheckTest {

    @Test
    public void CheckLength(){
        String string = "Something";

        assertTrue (string.length() > 15, "The length ot string is less than 15 symbols");
    }

}
