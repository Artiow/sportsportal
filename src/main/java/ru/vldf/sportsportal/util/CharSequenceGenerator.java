package ru.vldf.sportsportal.util;

import java.util.Random;

public class CharSequenceGenerator {

    /**
     * Returns a random generated sequence of characters of a given length.
     *
     * @param length the length of generated sequence.
     * @return random generated sequence of characters of a given length.
     */
    public static String generate(int length) {
        Random random = new Random();
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sequenceBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) sequenceBuilder.append(chars.charAt(random.nextInt(chars.length())));
        return sequenceBuilder.toString();
    }
}
