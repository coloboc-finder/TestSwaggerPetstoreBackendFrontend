package frontend;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class TestPetstore {
    private static final String BASE_URL = "https://petstore.swagger.io";
    private static final int PET_ID_RANGE = 100000;
    private static final String PET_NAME = "Batman";
    private static final String PET_STATUS = "available";
    private static int testPetId;

    @BeforeEach
    public void setUp() {
        open(BASE_URL);
        testPetId = (int) (System.nanoTime() % PET_ID_RANGE);

    }

    @AfterEach
    void tearDown() {
        closeWebDriver();
    }

    @Test
    public void testOpeningMainPage() {
        $("h2.title")
                .shouldHave(text("Swagger Petstore"));
        $("a[href='#/pet/findPetsByStatus']")
                .shouldBe(visible)
                .scrollIntoView(true);
        sleep(300);
        $("a[href='#/pet/findPetsByStatus']")
                .click();
        $(".btn.try-out__btn")
                .shouldBe(visible)
                .click();
        $$("select option")
                .findBy(text("available"))
                .click();
        $("button.execute")
                .click();
        $("pre.microlight")
                .shouldBe(visible)
                .shouldHave(text("available"));
    }

    @Test
    public void testAddingPetForm() {
        fillOutPetForms(testPetId, PET_NAME, PET_STATUS);
        $("pre.microlight")
                .shouldBe(visible)
                .shouldHave(text(PET_NAME));
    }

    @Test
    public void testSearchForPetById() {
        fillOutPetForms(testPetId, PET_NAME, PET_STATUS);
        $("pre.microlight")
                .shouldBe(visible)
                .shouldHave(text("\"id\": " + testPetId));
    }

    @Test
    public void testSearchInvalidId() {
        String negativeId = "A123";
        $("a[href='#/pet/getPetById']")
                .shouldBe(visible)
                .scrollIntoView(true);
        sleep(300);
        $("a[href='#/pet/getPetById']")
                .click();
        $(".btn.try-out__btn")
                .shouldBe(visible)
                .click();
        $("input[placeholder='petId']")
                .setValue(negativeId)
                .click();
        $("button.execute")
                .shouldBe(visible)
                .click();
        $(".validation-errors")
                .shouldBe(visible)
                .shouldHave(text("For 'petId': Value must be an integer."));
    }

    @Test
    public void testSearchNonexistentPetById() {
        String emptyField = "";
        $("a[href='#/pet/getPetById']")
                .shouldBe(visible)
                .scrollIntoView(true);
        sleep(300);
        $("a[href='#/pet/getPetById']")
                .click();
        $(".btn.try-out__btn")
                .shouldBe(visible)
                .click();
        $("input[placeholder='petId']")
                .setValue(emptyField)
                .click();
        $("button.execute")
                .shouldBe(visible)
                .click();
        $(".validation-errors li")
                .shouldBe(visible)
                .shouldHave(text("For 'petId': Required field is not provided."));
    }

    @Test
    public void testDeletePetById() {
        fillOutPetForms(testPetId, PET_NAME, PET_STATUS);
        deletePetById(testPetId);

        $("a[href='#/pet/getPetById']")
                .shouldBe(visible)
                .click();
        $(".btn.try-out__btn")
                .shouldBe(visible)
                .click();
        $("input[placeholder='petId']")
                .shouldBe(visible, Duration.ofSeconds(10))
                .shouldBe(enabled)
                .setValue(String.valueOf(testPetId));
        $("button.execute")
                .shouldBe(visible)
                .click();
        $("pre.microlight")
                .shouldBe(visible)
                .shouldHave(text("Pet not found"));
    }

    private void fillOutPetForms(int testPetId, String PET_NAME, String PET_STATUS) {
        $("a[href='#/pet/addPet']")
                .shouldBe(visible, enabled)
                .scrollIntoView(true);
        sleep(300);
        $("a[href='#/pet/addPet']")
                .click();
        $(".btn.try-out__btn")
                .shouldBe(visible)
                .click();
        String newPetJson = """
                {
                  "id": %d,
                  "name": "%s",
                  "category": {
                    "id": 0,
                    "name": "string"
                  },
                  "photoUrls": [
                    "string"
                  ],
                  "tags": [
                    {
                      "id": 0,
                      "name": "string"
                    }
                  ],
                  "status": "%s"
                }
                """.formatted(testPetId, PET_NAME, PET_STATUS);
        SelenideElement jsonField = $(".body-param__text")
                .shouldBe(visible);
        jsonField
                .sendKeys(Keys.chord(Keys.CONTROL, "a"));
        jsonField
                .sendKeys(Keys.DELETE);
        jsonField
                .setValue(newPetJson);
        $("button.execute")
                .shouldBe(visible, enabled)
                .scrollIntoView(true);
        sleep(300);
        $("button.execute")
                .click();
    }

    private void deletePetById(int petId) {
        $("a[href='#/pet/deletePet']")
                .shouldBe(visible)
                .scrollIntoView(true);
        sleep(300);
        $("a[href='#/pet/deletePet']")
                .click();
        $(".btn.try-out__btn")
                .shouldBe(visible)
                .click();
        $("input[placeholder='petId']")
                .should(appear)
                .shouldBe(visible, enabled)
                .setValue(String.valueOf(testPetId));
        $("button.execute")
                .shouldBe(visible, enabled)
                .scrollIntoView(true)
                .click();
    }
}
