package ru.vldf.sportsportal.service.security;

import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vldf.sportsportal.mapper.manual.security.UserDetailsMapper;
import ru.vldf.sportsportal.service.security.userdetails.IdentifiedUserDetails;

@Service
public class SecurityService {

    private TokenService tokenService;

    private UserDetailsMapper mapper;

    @Autowired
    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
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
        return tokenService.getTokenType();
    }


    /**
     * Returns JWT by UserDetails.
     *
     * @param identifiedUserDetails - user data
     * @return encoded json
     */
    public String login(IdentifiedUserDetails identifiedUserDetails) throws JwtException {
        return tokenService.generate(mapper.toMap(identifiedUserDetails));
    }

    /**
     * Returns UserDetails by JWT.
     *
     * @param token - encoded json
     * @return user data
     */
    public IdentifiedUserDetails authentication(String token) throws JwtException {
        return mapper.toIdentifiedUserDetails(tokenService.verify(token));
    }
}
