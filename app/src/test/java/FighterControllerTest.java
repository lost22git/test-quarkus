import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class FighterControllerTest {

    @Test
    void findAll() {
        given()
            .when()
            .get("/fighter")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("code", equalTo(0))
            .body("data.name", contains("隆", "肯"));
    }

    @Test
    void findByName() {
        given()
            .when()
            .get("/fighter/{name}", "隆")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("code", equalTo(0))
            .body("data.name", equalTo("隆"));
    }
}
