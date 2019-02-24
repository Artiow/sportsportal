package ru.vldf.sportsportal.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.vldf.sportsportal.mapper.manual.security.PayloadMapper;
import ru.vldf.sportsportal.service.security.encoder.Encoder;
import ru.vldf.sportsportal.service.security.keykeeper.KeyProvider;
import ru.vldf.sportsportal.service.security.keykeeper.Payload;
import ru.vldf.sportsportal.service.security.userdetails.IdentifiedUserDetails;

/**
 * @author Namednev Artem
 */
@Service
public class SecurityService implements SecurityProvider, AuthorizationProvider {

    private final Encoder encoder;
    private final KeyProvider provider;
    private final PayloadMapper mapper;


    @Autowired
    public SecurityService(Encoder encoder, KeyProvider provider, PayloadMapper mapper) {
        this.encoder = encoder;
        this.provider = provider;
        this.mapper = mapper;
    }


    @Override
    public IdentifiedUserDetails authorization(String username, String password) throws UsernameNotFoundException, BadCredentialsException {
        return provider.authorization(username, password);
    }

    @Override
    public IdentifiedUserDetails authorization(String accessToken) throws UsernameNotFoundException {
        return provider.authorization(verify(accessToken));
    }

    @Override
    public Pair<String, String> access(String username, String password) throws UsernameNotFoundException, BadCredentialsException {
        return getTokenPair(provider.access(username, password));
    }

    @Override
    public Pair<String, String> refresh(String refreshToken) throws UsernameNotFoundException {
        return getTokenPair(provider.refresh(verify(refreshToken)));
    }


    /**
     * Verify token and extract it payload.
     *
     * @param token the JSON Web Token.
     * @return token payload.
     */
    private Payload verify(String token) {
        return mapper.toPayload(encoder.verify(token));
    }

    /**
     * Returns generated token pair.
     *
     * @param payloadPair the payload pair.
     * @return generated token pair.
     */
    private Pair<String, String> getTokenPair(Pair<Payload, Payload> payloadPair) {
        return Pair.of(
                encoder.getAccessToken(mapper.toMap(payloadPair.getFirst())),
                encoder.getRefreshToken(mapper.toMap(payloadPair.getSecond()))
        );
    }
}
