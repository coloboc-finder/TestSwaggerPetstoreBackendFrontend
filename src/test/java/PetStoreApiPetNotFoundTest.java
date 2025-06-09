import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;

public class PetStoreApiPetNotFoundTest {

    private static final String BASE_URL = "https://petstore.swagger.io/v2";
    private static final int NON_EXISTENT_PET_ID = Integer.MAX_VALUE;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    public void testGetNonExistentPet() {
        given()
                .accept(ContentType.JSON)
                .when()
                .get("/pet/" + NON_EXISTENT_PET_ID)
                .then()
                .statusCode(404);
    }

    @Test
    public void testDeleteNonExistentPet() {
        given()
                .accept(ContentType.JSON)
                .when()
                .delete("/pet/" + NON_EXISTENT_PET_ID)
                .then()
                .statusCode(404);
    }
}
