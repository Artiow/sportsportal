package ru.vldf.sportsportal.util;

import java.util.Random;

public final class CharSequenceGenerator {

    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    /**
     * Returns a random generated sequence of characters of a given length.
     *
     * @param length the length of generated sequence.
     * @return random generated sequence of characters of a given length.
     */
    public static String generate(int length) {
        Random random = new Random();
        StringBuilder sequenceBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) sequenceBuilder.append(CHARS.charAt(random.nextInt(CHARS.length())));
        return sequenceBuilder.toString();
    }
}
