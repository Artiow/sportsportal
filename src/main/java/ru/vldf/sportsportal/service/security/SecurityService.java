package ru.vldf.sportsportal.service.security;

import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
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


    /**
     * Returns JWT by UserDetails.
     *
     * @param identifiedUserDetails - user data
     * @return encoded json
     */
    public String login(IdentifiedUserDetails identifiedUserDetails) throws JwtException {
        return tokenEncoder.getAccessToken(mapper.toMap(identifiedUserDetails));
    }

    /**
     * Returns UserDetails by JWT.
     *
     * @param token - encoded json
     * @return user data
     */
    @Override
    public IdentifiedUserDetails authentication(String token) throws JwtException {
        return mapper.toIdentifiedUserDetails(tokenEncoder.verify(token));
    }
}
