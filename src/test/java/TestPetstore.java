//import com.codeborne.selenide.Condition;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Test;
//import static com.codeborne.selenide.Selenide.*;
//
//public class TestPetstore {
//    private static final int PET_ID_RANGE = 100000;
//
//    @Test
//    void testPetstore() {
//
//        int testPetId = (int)(System.nanoTime() % PET_ID_RANGE);
//
//        open("https://petstore.swagger.io/");
//
//        $("a[href='#/pet/getPetById']").shouldBe(Condition.visible)
//                .click();
//
//        $(".btn.try-out__btn").shouldBe(Condition.visible)
//                .click();
//
//        $("input[placeholder='petId']").setValue(String.valueOf(testPetId))
//                .click();
//
//        $("button.execute").shouldBe(Condition.visible)
//                .click();
//
//        $("pre.microlight").shouldBe(Condition.visible)
//                .shouldHave(Condition.text("/" + testPetId));
//
//    }
//
//    @AfterEach
//    void tearDown() {
//        closeWebDriver();
//    }
//
//}
