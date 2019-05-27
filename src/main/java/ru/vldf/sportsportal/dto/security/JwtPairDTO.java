package ru.vldf.sportsportal.dto.security;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.dto.general.root.DataTransferObject;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
public class JwtPairDTO implements DataTransferObject {

    private String accessToken;
    private String refreshToken;
}
