package ru.vldf.sportsportal.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import ru.vldf.sportsportal.mapper.manual.security.PayloadMapper;
import ru.vldf.sportsportal.service.security.keykeeper.KeyProvider;
import ru.vldf.sportsportal.service.security.keykeeper.Payload;
import ru.vldf.sportsportal.service.security.userdetails.IdentifiedUserDetails;

@Service
public class SecurityService implements SecurityProvider, AuthorizationProvider {

    private KeyProvider keyProvider;
    private TokenEncoder tokenEncoder;
    private PayloadMapper payloadMapper;

    @Autowired
    public void setKeyProvider(KeyProvider keyProvider) {
        this.keyProvider = keyProvider;
    }

    @Autowired
    public void setTokenEncoder(TokenEncoder tokenEncoder) {
        this.tokenEncoder = tokenEncoder;
    }

    @Autowired
    public void setPayloadMapper(PayloadMapper payloadMapper) {
        this.payloadMapper = payloadMapper;
    }


    @Override
    public Pair<String, String> authentication(String username, String password) {
        Pair<Payload, Payload> payloadPair = keyProvider.authentication(username, password);
        return Pair.of(
                tokenEncoder.getAccessToken(payloadMapper.toMap(payloadPair.getFirst())),
                tokenEncoder.getRefreshToken(payloadMapper.toMap(payloadPair.getSecond()))
        );
    }

    @Override
    public IdentifiedUserDetails authorization(String accessToken) {
        return keyProvider.authorization(payloadMapper.toPayload(tokenEncoder.verify(accessToken)));
    }

    @Override
    public Pair<String, String> refresh(String refreshToken) {
        Pair<Payload, Payload> payloadPair = keyProvider.refresh(payloadMapper.toPayload(tokenEncoder.verify(refreshToken)));
        return Pair.of(
                tokenEncoder.getAccessToken(payloadMapper.toMap(payloadPair.getFirst())),
                tokenEncoder.getRefreshToken(payloadMapper.toMap(payloadPair.getSecond()))
        );
    }

    @Override
    public void logout(String accessToken) {
        keyProvider.logout(payloadMapper.toPayload(tokenEncoder.verify(accessToken)));
    }
}
