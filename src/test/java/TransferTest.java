import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TransferTest {
    DashboardPage dashboardPage;
    DataHelper.CardInfo firstCard = DataHelper.getFirstCardInfo();
    DataHelper.CardInfo secondCard = DataHelper.getSecondCardInfo();

    // менеджер паролей
    @BeforeAll
    static void setUpAll() {
        ChromeOptions options = new ChromeOptions();
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false);
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("--disable-features=PasswordLeakDetection");
        options.addArguments("--incognito");
        Configuration.browserCapabilities = options;
    }


    @BeforeEach
    void setup() {
        open("http://localhost:9999");
        var verificationPage = new LoginPage().validLogin(DataHelper.getAuthInfo());
        dashboardPage = verificationPage.validVerify(DataHelper.getVerificationCode());
    }

    @Test
    void shouldTransferFromFirstToSecond() {
        int firstBefore = dashboardPage.getCardBalance(firstCard);
        int secondBefore = dashboardPage.getCardBalance(secondCard);
        int amount = DataHelper.generateValidAmount(firstBefore);

        var transferPage = dashboardPage.selectCardToTransfer(secondCard);
        dashboardPage = transferPage.makeTransfer(String.valueOf(amount), firstCard);
        dashboardPage.reload();

        assertEquals(firstBefore - amount, dashboardPage.getCardBalance(firstCard));
        assertEquals(secondBefore + amount, dashboardPage.getCardBalance(secondCard));
    }

    @Test
    void shouldTransferFromSecondToFirst() {
        int firstBefore = dashboardPage.getCardBalance(firstCard);
        int secondBefore = dashboardPage.getCardBalance(secondCard);
        int amount = DataHelper.generateValidAmount(secondBefore);

        var transferPage = dashboardPage.selectCardToTransfer(firstCard);
        dashboardPage = transferPage.makeTransfer(String.valueOf(amount), secondCard);
        dashboardPage.reload();

        assertEquals(firstBefore + amount, dashboardPage.getCardBalance(firstCard));
        assertEquals(secondBefore - amount, dashboardPage.getCardBalance(secondCard));
    }

    @Disabled("Баг: приложение разрешает перевод суммы больше баланса, см. issue в репозитории")
    @Test
    void shouldNotTransferMoreThanBalance() {
        int firstBefore = dashboardPage.getCardBalance(firstCard);
        int secondBefore = dashboardPage.getCardBalance(secondCard);
        int amount = DataHelper.generateInvalidAmount(firstBefore);

        var transferPage = dashboardPage.selectCardToTransfer(secondCard);
        transferPage.fillAndSubmit(String.valueOf(amount), firstCard);
        transferPage.shouldShowError();
        dashboardPage = transferPage.cancel();

        assertEquals(firstBefore, dashboardPage.getCardBalance(firstCard));
        assertEquals(secondBefore, dashboardPage.getCardBalance(secondCard));
    }
}