package ru.vldf.sportsportal.service.security;

import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import ru.vldf.sportsportal.mapper.manual.security.UserDetailsMapper;
import ru.vldf.sportsportal.service.security.userdetails.IdentifiedUserDetails;

@Service
@SuppressWarnings("deprecation")
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


    @Override
    public Pair<String, String> authorization(Integer userId) throws JwtException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Pair<String, String> authorization(String refreshToken) throws JwtException {
        throw new UnsupportedOperationException();
    }

    @Override
    public IdentifiedUserDetails authentication(String accessToken) throws JwtException {
        return mapper.toIdentifiedUserDetails(tokenEncoder.verify(accessToken));
    }

    @Deprecated
    @SuppressWarnings("DeprecatedIsStillUsed")
    public String login(IdentifiedUserDetails identifiedUserDetails) throws JwtException {
        return tokenEncoder.getAccessToken(mapper.toMap(identifiedUserDetails));
    }
}
