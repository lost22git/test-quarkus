import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.hypersistence.utils.hibernate.type.range.Range;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import lost.test.quarkus.controller.MatchController;
import lost.test.quarkus.model.MatchCreateParam;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

@QuarkusTest
public class MatchControllerTest {

    @Inject
    MatchController matchController;
    @Inject
    ObjectMapper objectMapper;

    @Test
    void add() throws JsonProcessingException {
        var matchCreateParam = new MatchCreateParam(Set.of(1L, 2L), Range.open(ZonedDateTime.now(), ZonedDateTime.now().plusDays(1)));
        var requestBody = objectMapper.writeValueAsString(matchCreateParam);

        given()
            .log()
            .all()
            .body(requestBody)
            .contentType(JSON)
            .when()
            .post("/match")
            .then()
            .statusCode(200);
    }

}
