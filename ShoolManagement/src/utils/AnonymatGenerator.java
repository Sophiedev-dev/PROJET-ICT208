package utils;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class AnonymatGenerator {
    private static final Random random = new Random();
    private static final Set<String> existingCodes = new HashSet<>();

    public static String generateUniqueCode() {
        String code;
        do {
            code = "A" + (10000 + random.nextInt(90000)); // exemple : A12345
        } while (existingCodes.contains(code));
        existingCodes.add(code);
        return code;
    }

    public static void reset() {
        existingCodes.clear(); // utile si on relance plusieurs fois le TP
    }
}
