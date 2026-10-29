import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private final SelenideElement heading = $("[data-test-id=dashboard]");
    private final SelenideElement reloadButton = $("[data-test-id=action-reload]");
    private final ElementsCollection cards = $$(".list__item");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";

    public DashboardPage() {
        heading.shouldBe(visible);
    }

    // найти строку карты по последним 4 цифрам номера
    private SelenideElement findCard(DataHelper.CardInfo card) {
        String last4 = card.getNumber().substring(15);
        return cards.findBy(text(last4));
    }

    // 1. получить баланс карты
    public int getCardBalance(DataHelper.CardInfo card) {
        return extractBalance(findCard(card).getText());
    }

    // 2. выбрать карту для пополнения
    public TransferPage selectCardToTransfer(DataHelper.CardInfo card) {
        findCard(card).$("button").click();
        return new TransferPage();
    }

    // обновить
    public void reload() {
        reloadButton.click();
        heading.shouldBe(visible);
    }

    private int extractBalance(String text) {
        // текст вида: "**** **** **** 0001, баланс: 9900 р."
        int start = text.indexOf(balanceStart);
        int finish = text.indexOf(balanceFinish);
        String value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value.trim());
    }
}
