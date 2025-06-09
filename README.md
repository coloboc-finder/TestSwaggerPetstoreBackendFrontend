# 🧪 Тестовое задание: Автотесты для Swagger Petstore (Backend + Frontend)

## 📌 Цель

Написать автоматические тесты для [Swagger Petstore](https://petstore.swagger.io/), покрывающие как API (backend), так и UI (frontend) функциональность.

---

## 🛠️ Стек

- **Язык:** Java  
- **API-тесты:** RestAssured + JUnit/TestNG  
- **UI-тесты:** Selenium WebDriver + JUnit/TestNG  
- **Отчёты (по желанию):** Allure  
- **CI (по желанию):** GitHub Actions / Jenkins  

---

## 🔧 1. API (Backend) Тестирование

### Эндпоинты `/pet`:
- `POST /pet` – добавить питомца  
- `GET /pet/{id}` – получить питомца по ID  
- `PUT /pet` – обновить питомца  
- `DELETE /pet/{id}` – удалить питомца  

### ✔️ Проверки:
- Статус-коды (200, 404, 500)  
- Валидация JSON-схем (если возможно)  
- Обязательные поля (например, `name`, `status`)  
- Ошибки (несуществующий ID, невалидный JSON)

### 🧪 Пример тестов:

```java
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Test;
import static org.hamcrest.Matchers.*;

public class PetStoreApiTest {

    @Test
    public void testAddAndGetPet() {
        String petJson = """
            {
                "id": 123,
                "name": "Rex",
                "status": "available"
            }""";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(petJson)
                .when()
                .post("https://petstore.swagger.io/v2/pet")
                .then()
                .statusCode(200)
                .body("name", equalTo("Rex"));

        RestAssured.given()
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
                .when()
                .delete("https://petstore.swagger.io/v2/pet/123")
                .then()
                .statusCode(200);

        RestAssured.given()
                .when()
                .get("https://petstore.swagger.io/v2/pet/123")
                .then()
                .statusCode(404);
    }
}
```

---

## 🖥️ 2. UI (Frontend) Тестирование

### Задачи:
- Открытие главной страницы  
- Поиск питомца по ID  
- Добавление питомца через форму (если есть)  

### ✔️ Проверки:
- Загрузка элементов (страница доступна, формы и кнопки на месте)  
- Отображение данных (например, имя питомца)  
- Обработка ошибок (некорректный ID, пустое поле)

### 🧪 Пример теста:

```java
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class PetStoreUiTest {
    WebDriver driver;

    @BeforeMethod
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
        driver = new ChromeDriver();
        driver.get("https://petstore.swagger.io/");
    }

    @Test
    public void testPetSearch() {
        WebElement petIdInput = driver.findElement(By.cssSelector("input#petId"));
        WebElement searchButton = driver.findElement(By.cssSelector("button#searchPet"));

        petIdInput.sendKeys("123");
        searchButton.click();

        WebElement petName = driver.findElement(By.cssSelector(".pet-name"));
        assertEquals(petName.getText(), "Rex");
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}
```

---

## 🌟 Дополнительные улучшения (по желанию)

- Интеграция с Allure для красивых отчётов  
- Параметризованные тесты (`@DataProvider`, `@ParameterizedTest`)  
- Использование Page Object Model (POM)  
- Проверка валидации форм  
- Тестирование через CI/CD (GitHub Actions, Jenkins)

---

## 📝 Критерии оценки

### ✅ Backend:
- Корректность тестов  
- Покрытие CRUD-сценариев  
- Работа с ошибками и невалидными данными  

### ✅ Frontend:
- Реальные проверки UI  
- Корректные локаторы  
- Валидные assert’ы  

### 🔼 Доп. баллы:
- Allure, параметризация, CI  
- Читабельный код, структура  
