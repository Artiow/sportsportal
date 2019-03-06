package ru.vldf.sportsportal.service.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.vldf.sportsportal.config.messages.MessageContainer;
import ru.vldf.sportsportal.mapper.manual.security.PayloadMapper;
import ru.vldf.sportsportal.service.security.encoder.Encoder;
import ru.vldf.sportsportal.service.security.model.ExpirationType;
import ru.vldf.sportsportal.service.security.model.Payload;
import ru.vldf.sportsportal.service.security.userdetails.UserDetailsProvider;
import ru.vldf.sportsportal.service.security.userdetails.model.IdentifiedUserDetails;

import java.util.UUID;

/**
 * @author Namednev Artem
 */
@Service
public class SecurityService implements SecurityProvider, AuthorizationProvider {

    private final MessageContainer messages;

    private final Encoder encoder;
    private final UserDetailsProvider provider;
    private final PayloadMapper mapper;


    @Autowired
    public SecurityService(
            MessageContainer messages,
            Encoder encoder,
            UserDetailsProvider provider,
            PayloadMapper mapper
    ) {
        this.messages = messages;
        this.encoder = encoder;
        this.provider = provider;
        this.mapper = mapper;
    }


    @Override
    public IdentifiedUserDetails authorization(String username, String password) throws UsernameNotFoundException, BadCredentialsException {
        return provider.authorization(username, password);
    }

    @Override
    public IdentifiedUserDetails access(String accessToken) throws AuthenticationException {
        return provider.authorization(verify(accessToken, ExpirationType.ACCESS).getUserId());
    }

    @Override
    public IdentifiedUserDetails refresh(String refreshToken) throws AuthenticationException {
        return provider.authorization(verify(refreshToken, ExpirationType.REFRESH).getUserId());
    }

    @Override
    public Pair<String, String> generate(Integer userId) {
        Payload newAccessPayload = new Payload();
        newAccessPayload.setUserId(userId);
        newAccessPayload.setKeyUuid(UUID.randomUUID());
        newAccessPayload.setType(ExpirationType.ACCESS);
        Payload newRefreshPayload = new Payload();
        newRefreshPayload.setUserId(userId);
        newRefreshPayload.setKeyUuid(UUID.randomUUID());
        newRefreshPayload.setType(ExpirationType.REFRESH);
        return Pair.of(
                encoder.getAccessToken(mapper.toMap(newAccessPayload)),
                encoder.getRefreshToken(mapper.toMap(newRefreshPayload))
        );
    }


    /**
     * Verify token and extract it payload.
     *
     * @param token the JSON Web Token.
     * @return token payload.
     * @throws AuthenticationException if verifying failed.
     */
    private Payload verify(String token, ExpirationType type) throws AuthenticationException {
        Payload payload;
        try {
            payload = mapper.toPayload(encoder.verify(token));
        } catch (SignatureException e) {
            throw new InsufficientAuthenticationException(messages.get("sportsportal.auth.provider.insufficientToken.message"), e);
        } catch (ExpiredJwtException e) {
            throw new CredentialsExpiredException(messages.get("sportsportal.auth.provider.expiredToken.message"), e);
        } catch (JwtException e) {
            throw new BadCredentialsException(messages.get("sportsportal.auth.provider.couldNotParseToken.message"), e);
        }
        if (payload.getType() != type) {
            throw new BadCredentialsException(messages.get("sportsportal.auth.provider.invalidTokenType.message"));
        } else {
            return payload;
        }
    }
}
