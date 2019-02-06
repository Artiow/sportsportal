package ru.vldf.sportsportal.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Pair<String, String> authentication(String username, String password) {
        return getTokenPair(provider.authentication(username, password));
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public IdentifiedUserDetails authorization(String accessToken) {
        return provider.authorization(mapper.toPayload(encoder.verify(accessToken)));
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Pair<String, String> refresh(String refreshToken) {
        return getTokenPair(provider.refresh(mapper.toPayload(encoder.verify(refreshToken))));
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void logout(String accessToken) {
        provider.logout(mapper.toPayload(encoder.verify(accessToken)));
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void logoutAll(String accessToken) {
        provider.logoutAll(mapper.toPayload(encoder.verify(accessToken)));
    }


    /**
     * Returns generated token pair.
     *
     * @param payloadPair {@link Pair} payload pair
     * @return {@link Pair} generated token pair
     */
    private Pair<String, String> getTokenPair(Pair<Payload, Payload> payloadPair) {
        return Pair.of(
                encoder.getAccessToken(mapper.toMap(payloadPair.getFirst())),
                encoder.getRefreshToken(mapper.toMap(payloadPair.getSecond()))
        );
    }
}
