import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.hidden;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private final SelenideElement amountField = $("[data-test-id=amount] input");
    private final SelenideElement fromField = $("[data-test-id=from] input");
    private final SelenideElement transferButton = $("[data-test-id=action-transfer]");
    private final SelenideElement cancelButton = $("[data-test-id=action-cancel]");
    private final SelenideElement errorNotification = $("[data-test-id=error-notification]");

    public TransferPage() {
        amountField.shouldBe(visible);
    }

    // заполнить форму и нажать «пополнить»
    public void fillAndSubmit(String amount, DataHelper.CardInfo from) {
        amountField.setValue(amount);
        fromField.setValue(from.getNumber());
        transferButton.click();
    }

    // 3. успешный перевод, возврат на список карт
    public DashboardPage makeTransfer(String amount, DataHelper.CardInfo from) {
        fillAndSubmit(amount, from);
        amountField.shouldBe(hidden);   // ждём, пока форма перевода закроется
        return new DashboardPage();
    }

    public void shouldShowError() {
        errorNotification.shouldBe(visible, Duration.ofSeconds(15));
    }

    public DashboardPage cancel() {
        cancelButton.click();
        return new DashboardPage();
    }
}
