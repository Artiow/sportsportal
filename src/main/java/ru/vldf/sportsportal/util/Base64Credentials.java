package ru.vldf.sportsportal.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.util.Assert;
import org.springframework.util.Base64Utils;

import javax.validation.constraints.NotBlank;
import java.nio.charset.StandardCharsets;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
@RequiredArgsConstructor(staticName = "of")
public final class Base64Credentials {

    @NotBlank
    private final String username;

    @NotBlank
    private final String password;

    private transient String credentials = null;

    public static Base64Credentials decode(String credentials) {
        Assert.hasText(credentials, "credentials must not bu blank");
        String[] arr = new String(Base64Utils.decodeFromString(credentials), StandardCharsets.UTF_8).split(":", 2);
        if (arr.length == 2) return Base64Credentials.of(arr[0], arr[1]);
        else throw new IllegalArgumentException("invalid base64 credentials format");
    }

    public String encode() {
        return credentials == null ? credentials = username + ":" + password : credentials;
    }
}
