package ru.vldf.sportsportal.service.security;

import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import ru.vldf.sportsportal.mapper.manual.security.UserDetailsMapper;
import ru.vldf.sportsportal.service.security.userdetails.IdentifiedUserDetails;

@Service
public class SecurityService implements SecurityProvider {

    private TokenEncoder tokenEncoder;

    private UserDetailsMapper mapper;

    @Autowired
    public void setTokenEncoder(TokenEncoder tokenEncoder) {
        this.tokenEncoder = tokenEncoder;
    }

    @Autowired
    public void setMapper(UserDetailsMapper mapper) {
        this.mapper = mapper;
    }


    /**
     * Getting used token type from token service.
     *
     * @return tokenType
     */
    public String getTokenType() {
        return tokenEncoder.getTokenType();
    }


    @Deprecated
    @SuppressWarnings("DeprecatedIsStillUsed")
    public String login(IdentifiedUserDetails identifiedUserDetails) throws JwtException {
        return tokenEncoder.getAccessToken(mapper.toMap(identifiedUserDetails));
    }

    @Override
    public Pair<String, String> authorization(Integer userId) {
        return null;
    }

    @Override
    public Pair<String, String> authorization(String refreshToken) {
        return null;
    }

    @Override
    public IdentifiedUserDetails authentication(String accessToken) {
        return mapper.toIdentifiedUserDetails(tokenEncoder.verify(accessToken));
    }
}
