import java.util.Random;

public class DataHelper {
    private DataHelper() {
    }

    public static class AuthInfo {
        private final String login;
        private final String password;

        public AuthInfo(String login, String password) {
            this.login = login;
            this.password = password;
        }

        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static class VerificationCode {
        private final String code;

        public VerificationCode(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

    public static VerificationCode getVerificationCode() {
        return new VerificationCode("12345");
    }

    public static class CardInfo {
        private final String number;

        public CardInfo(String number) {
            this.number = number;
        }

        public String getNumber() {
            return number;
        }
    }

    public static CardInfo getFirstCardInfo() {
        return new CardInfo("5559 0000 0000 0001");
    }

    public static CardInfo getSecondCardInfo() {
        return new CardInfo("5559 0000 0000 0002");
    }


    public static int generateValidAmount(int balance) {
        return new Random().nextInt(Math.max(balance / 2, 1)) + 1;
    }

    // сумма, которая точно больше баланса
    public static int generateInvalidAmount(int balance) {
        return Math.abs(balance) + new Random().nextInt(10_000) + 1;
    }
}
