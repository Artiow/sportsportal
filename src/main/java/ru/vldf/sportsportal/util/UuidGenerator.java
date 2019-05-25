package ru.vldf.sportsportal.util;

import org.postgresql.util.Base64;

import java.util.UUID;

/**
 * @author Namednev Artem
 */
public final class UuidGenerator {

    /**
     * Returns a random generated sequence of characters from UUID bytes..
     *
     * @return base64 random string.
     */
    public static String uniqueBase64() {
        return Base64.encodeBytes(UUID.randomUUID().toString().getBytes());
    }
}
