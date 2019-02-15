package ru.vldf.sportsportal.dto.security;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
public class JwtPairDTO {

    private String accessToken;
    private String refreshToken;
}
