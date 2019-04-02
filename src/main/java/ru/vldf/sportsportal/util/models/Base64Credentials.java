package ru.vldf.sportsportal.util.models;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;
import org.springframework.util.Base64Utils;

import java.nio.charset.StandardCharsets;

/**
 * @author Namednev Artem
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE, staticName = "of")
public final class Base64Credentials {

    private final String username;
    private final String password;

    /**
     * Decode Base64 credentials (like {@literal username:password}).
     *
     * @param credentials the Base64 encoding of username and password joined by a colon.
     * @return decoded Base64 credentials.
     * @throws IllegalArgumentException if credentials has not valid format.
     */
    public static Base64Credentials decode(String credentials) throws IllegalArgumentException {
        Assert.hasText(credentials, "credentials must not be blank");
        String[] arr = new String(Base64Utils.decodeFromString(credentials), StandardCharsets.UTF_8).split(":", 2);
        if (arr.length == 2) return Base64Credentials.of(arr[0], arr[1]);
        else throw new IllegalArgumentException("invalid base64 credentials format");
    }
}
