package gymapp.utility;

import java.security.SecureRandom;
import java.util.stream.Collectors;

public class PasswordGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generatePassword(int length) {
        return RANDOM.ints(length, 0, CHARACTERS.length())
                .mapToObj(i -> String.valueOf(CHARACTERS.charAt(i)))
                .collect(Collectors.joining());
    }
}
