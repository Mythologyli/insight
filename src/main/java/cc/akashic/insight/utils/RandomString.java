package cc.akashic.insight.utils;

import java.util.Random;

public final class RandomString {
    /**
     * Generate random string.
     * Select characters from 0-9, a-z, A-Z.
     * @param length The length of the string
     * @return random string
     */
    public static String getRandomString(int length) {
        String stringToSelectFrom = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

        Random random = new Random();
        StringBuilder randomString = new StringBuilder();

        for (int i = 0; i < length; i++) {
            randomString.append(stringToSelectFrom.charAt(random.nextInt(62)));
        }

        return randomString.toString();
    }
}
