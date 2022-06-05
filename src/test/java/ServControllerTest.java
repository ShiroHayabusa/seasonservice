import io.restassured.http.ContentType;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ServControllerTest {
    private String body = "{ \"login\": \"user1\" \"password\": \"pass\"}";

    @Test
    public void getServTest() {

 /*       given()
                .baseUri("http://localhost:8080/servs/")
                .basePath("/1/")
                .contentType(ContentType.JSON)
                .when().get()
                .then()
                .statusCode(200)
                .body("name", equalTo("Выдача охотничьих билетов"));*/
    }
}
