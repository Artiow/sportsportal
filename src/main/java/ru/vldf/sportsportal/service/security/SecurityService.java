package ru.vldf.sportsportal.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import ru.vldf.sportsportal.mapper.manual.security.PayloadMapper;
import ru.vldf.sportsportal.service.security.encoder.Encoder;
import ru.vldf.sportsportal.service.security.keykeeper.KeyProvider;
import ru.vldf.sportsportal.service.security.keykeeper.Payload;
import ru.vldf.sportsportal.service.security.userdetails.IdentifiedUserDetails;

@Service
public class SecurityService implements SecurityProvider, AuthorizationProvider {

    private Encoder encoder;
    private KeyProvider keyProvider;
    private PayloadMapper payloadMapper;

    @Autowired
    public void setEncoder(Encoder encoder) {
        this.encoder = encoder;
    }

    @Autowired
    public void setKeyProvider(KeyProvider keyProvider) {
        this.keyProvider = keyProvider;
    }

    @Autowired
    public void setPayloadMapper(PayloadMapper payloadMapper) {
        this.payloadMapper = payloadMapper;
    }


    @Override
    public Pair<String, String> authentication(String username, String password) {
        return getTokenPair(keyProvider.authentication(username, password));
    }

    @Override
    public IdentifiedUserDetails authorization(String accessToken) {
        return keyProvider.authorization(payloadMapper.toPayload(encoder.verify(accessToken)));
    }

    @Override
    public Pair<String, String> refresh(String refreshToken) {
        return getTokenPair(keyProvider.refresh(payloadMapper.toPayload(encoder.verify(refreshToken))));
    }

    @Override
    public void logout(String accessToken) {
        keyProvider.logout(payloadMapper.toPayload(encoder.verify(accessToken)));
    }

    @Override
    public void logoutAll(String accessToken) {
        keyProvider.logoutAll(payloadMapper.toPayload(encoder.verify(accessToken)));
    }

    /**
     * Returns generated token pair.
     *
     * @param payloadPair {@link Pair} payload pair
     * @return {@link Pair} generated token pair
     */
    private Pair<String, String> getTokenPair(Pair<Payload, Payload> payloadPair) {
        return Pair.of(
                encoder.getAccessToken(payloadMapper.toMap(payloadPair.getFirst())),
                encoder.getRefreshToken(payloadMapper.toMap(payloadPair.getSecond()))
        );
    }
}
