import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;

public class PetStoreApiTestVersion_2 {

    private static final int PET_ID = 987654;
    private static final String PET_NAME = "Rex";
    private static final String PET_STATUS = "available";
    private static final String PET_JSON = """
        {
          "id": %d,
          "name": "%s",
          "status": "%s"
        }""".formatted(PET_ID, PET_NAME, PET_STATUS);

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

    @Test
    public void testAddAndGetPet() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(PET_JSON)
                .when()
                .post("/pet")
                .then()
                .statusCode(200)
                .body("name", equalTo(PET_NAME));
        RestAssured.given()
                .accept(ContentType.JSON)
                .when()
                .get("/pet/" + PET_ID)
                .then()
                .statusCode(200)
                .body("id", equalTo(PET_ID))
                .body("name", equalTo(PET_NAME))
                .body("status", equalTo(PET_STATUS));
    }

    @Test
    public void testDeletePet() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .when()
                .delete("/pet/" + PET_ID)
                .then()
                .statusCode(200);
        RestAssured.given()
                .accept(ContentType.JSON)
                .when()
                .get("/pet/" + PET_ID)
                .then()
                .statusCode(404);
    }
}
