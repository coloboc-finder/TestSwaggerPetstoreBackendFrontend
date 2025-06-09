import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.awaitility.Awaitility.await;

import java.util.concurrent.TimeUnit;

public class PetStoreApiTest {

    private static final String BASE_URL = "https://petstore.swagger.io/v2";
    private static final String PET_NAME = "Rex";
    private static final String PET_STATUS = "available";
    private static int testPetId;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    @BeforeEach
    public void createTestPet() throws InterruptedException {
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

    private void waitForPetCreation(int petId) {
        await()
                .atMost(15, TimeUnit.SECONDS)
                .pollInterval(1, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    given()
                            .header("Cache-Control", "no-cache, no-store, must-revalidate")
                            .header("Pragma", "no-cache")
                            .log().all()
                            .accept(ContentType.JSON)
                            .when()
                            .get("/pet/" + petId)
                            .then()
                            .statusCode(200)
                            .body("id", equalTo(petId));
                });
    }

    @Test
    public void testGetPet() {
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

    @Test
    public void testDeletePet() {
        given()
                .accept(ContentType.JSON)
                .when()
                .get("/pet/" + testPetId)
                .then()
                .statusCode(200);

        given()
                .accept(ContentType.JSON)
                .when()
                .delete("/pet/" + testPetId)
                .then()
                .statusCode(200);

        given()
                .accept(ContentType.JSON)
                .when()
                .get("/pet/" + testPetId)
                .then()
                .statusCode(404);
    }

    @Test
    public void testGetNonExistentPet() {
        int nonExistentPetId = Integer.MAX_VALUE;

        given()
                .accept(ContentType.JSON)
                .when()
                .get("/pet/" + nonExistentPetId)
                .then()
                .statusCode(404);
    }

    @Test
    public void testDeleteNonExistentPet() {
        int nonExistentPetId = Integer.MAX_VALUE;

        given()
                .accept(ContentType.JSON)
                .when()
                .delete("/pet/" + nonExistentPetId)
                .then()
                .statusCode(404);
    }
}