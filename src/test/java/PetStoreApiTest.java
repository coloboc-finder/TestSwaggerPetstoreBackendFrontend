import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;

public class PetStoreApiTest {

    @org.junit.jupiter.api.Test
    public void testAddAndGetPet() {
        // 1. Добавляем питомца
        String petJson = "{\n" +
                "  \"id\": 123,\n" +
                "  \"name\": \"Rex\",\n" +
                "  \"status\": \"available\"\n" +
                "}";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(petJson)
                .when()
                .post("https://petstore.swagger.io/v2/pet")
                .then()
                .statusCode(200)
                .body("name", equalTo("Rex"));

        // 2. Получаем питомца и проверяем данные
        RestAssured.given()
                .accept(ContentType.JSON)
                .when()
                .get("https://petstore.swagger.io/v2/pet/123")
                .then()
                .statusCode(200)
                .body("id", equalTo(123))
                .body("status", equalTo("available"));
    }

    @Test
    public void testDeletePet() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .when()
                .delete("https://petstore.swagger.io/v2/pet/123")
                .then()
                .statusCode(200);

        // Проверяем, что питомец удалён
        RestAssured.given()
                .accept(ContentType.JSON)
                .when()
                .get("https://petstore.swagger.io/v2/pet/123")
                .then()
                .statusCode(404);
    }
}