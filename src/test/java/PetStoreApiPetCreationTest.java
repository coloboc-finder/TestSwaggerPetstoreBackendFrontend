import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class PetStoreApiPetCreationTest {

    private static final String BASE_URL = "https://petstore.swagger.io/v2";
    private static final String PET_NAME = "Rex";
    private static final String PET_STATUS = "available";
    private int testPetId;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    @BeforeEach
    public void createTestPet() {
        testPetId = (int) (System.nanoTime() % 1000000);

        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(String.format(
                        "{\"id\": %d, \"name\": \"%s\", \"status\": \"%s\"}",
                        testPetId, PET_NAME, PET_STATUS))
                .when()
                .post("/pet")
                .then()
                .statusCode(200);
    }

    @Test
    public void testCreatePet() {
        given()
                .accept(ContentType.JSON)
                .when()
                .get("/pet/" + testPetId)
                .then()
                .statusCode(200)
                .body("id", equalTo(testPetId))
                .body("name", equalTo(PET_NAME))
                .body("status", equalTo(PET_STATUS));
    }

    @AfterEach
    public void cleanupTestPet() {
        try {
            given()
                    .when()
                    .delete("/pet/" + testPetId)
                    .then()
                    .statusCode(anyOf(is(200), is(404)));
        } catch (Exception e) {
            System.out.println("Cleanup failed for pet: " + testPetId);
        }
    }
}
