import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Test;
import static com.codeborne.selenide.Selenide.*;

public class TestPetstore {

    @Test
    void testPetstore() {

        int testPetId = (int)(System.nanoTime()%100000);

        open("https://petstore.swagger.io/");

        $("a[href='#/pet/getPetById']").shouldBe(Condition.visible).click();

        $(".btn.try-out__btn").shouldBe(Condition.visible).click();

        $("input[placeholder='petId']").setValue(String.valueOf(testPetId)).click();

        $("button.execute").shouldBe(Condition.visible).click();

        $("pre.microlight").shouldBe(Condition.visible).shouldHave(Condition.text("/" + testPetId));
    }

}
