package ru.vldf.sportsportal.service.security;

import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;
import ru.vldf.sportsportal.domain.sectional.security.KeyEntity;
import ru.vldf.sportsportal.mapper.manual.security.JwtPayloadMapper;
import ru.vldf.sportsportal.mapper.manual.security.UserDetailsMapper;
import ru.vldf.sportsportal.repository.security.KeyRepository;
import ru.vldf.sportsportal.service.security.userdetails.IdentifiedUserDetails;
import ru.vldf.sportsportal.service.security.userdetails.JwtPayload;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@Service
@SuppressWarnings("deprecation")
public class SecurityService implements SecurityProvider {

    private BCryptPasswordEncoder passwordEncoder;

    private TokenEncoder tokenEncoder;
    private KeyRepository keyRepository;
    private JwtPayloadMapper payloadMapper;
    private UserDetailsMapper detailsMapper;

    @Autowired
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setTokenEncoder(TokenEncoder tokenEncoder) {
        this.tokenEncoder = tokenEncoder;
    }

    @Autowired
    public void setKeyRepository(KeyRepository keyRepository) {
        this.keyRepository = keyRepository;
    }

    @Autowired
    public void setPayloadMapper(JwtPayloadMapper payloadMapper) {
        this.payloadMapper = payloadMapper;
    }

    @Autowired
    public void setDetailsMapper(UserDetailsMapper detailsMapper) {
        this.detailsMapper = detailsMapper;
    }

    /**
     * Getting used token type from token service.
     *
     * @return tokenType
     */
    @Deprecated
    @SuppressWarnings("DeprecatedIsStillUsed")
    public String getTokenType() {
        return "Bearer";
    }


    /**
     * Returns new pair of jwt (access and refresh).
     *
     * @param userEntity {@link UserEntity} user
     * @return {@link Pair} pair of jwt (access and refresh)
     * @throws JwtException if could not generate jwt
     */
    @Override
    public Pair<String, String> authorization(UserEntity userEntity) throws JwtException {
        UUID accessKey = UUID.randomUUID();
        UUID refreshKey = UUID.randomUUID();

        KeyEntity keyEntity = new KeyEntity();
        keyEntity.setUser(userEntity);
        keyEntity.setType(KeyType.ACCESS.toString());
        keyEntity.setUuid(passwordEncoder.encode(accessKey.toString()));
        keyEntity.setRelated(new KeyEntity());
        keyEntity.getRelated().setUser(userEntity);
        keyEntity.getRelated().setType(KeyType.REFRESH.toString());
        keyEntity.getRelated().setUuid(passwordEncoder.encode(refreshKey.toString()));
        keyEntity.getRelated().setRelated(keyEntity);

        keyEntity = keyRepository.save(keyEntity);

        return Pair.of(
                tokenEncoder.getAccessToken(payloadMapper.toMap(new JwtPayload()
                        .setKeyId(keyEntity.getId())
                        .setUserId(userEntity.getId())
                        .setUuid(accessKey)
                )),
                tokenEncoder.getRefreshToken(payloadMapper.toMap(new JwtPayload()
                        .setKeyId(keyEntity.getRelated().getId())
                        .setUserId(userEntity.getId())
                        .setUuid(refreshKey)
                ))
        );
    }

    /**
     * Returns new pair of jwt (access and refresh).
     *
     * @param refreshToken {@link String} current refresh jwt
     * @return {@link Pair} pair of jwt (access and refresh)
     * @throws JwtException            if encoding or decoding jwt failed
     * @throws EntityNotFoundException if user or key not found
     * @throws SecurityException       if jwt payload invalid
     */
    @Override
    @Transactional
    public Pair<String, String> authorization(String refreshToken) throws JwtException, EntityNotFoundException, SecurityException {
        JwtPayload payload = payloadMapper.toJwtPayload(tokenEncoder.verify(refreshToken));
        KeyEntity refreshKeyEntity = keyRepository.getOne(payload.getKeyId());
        if (!refreshKeyEntity.getUser().getId().equals(payload.getUserId())) {
            throw new SecurityException("Token is not owned by user");
        } else if (!KeyType.REFRESH.toString().equals(refreshKeyEntity.getType())) {
            throw new SecurityException("Invalid token type");
        } else if (!passwordEncoder.matches(payload.getUuid().toString(), refreshKeyEntity.getUuid())) {
            throw new SecurityException("Invalid token uuid");
        } else {
            KeyEntity accessKeyEntity = refreshKeyEntity.getRelated();
            if (!KeyType.ACCESS.toString().equals(accessKeyEntity.getType())) {
                throw new IllegalStateException(String.format(
                        "Invalid related key type. Direct key id %d, related key id %d",
                        refreshKeyEntity.getId(),
                        accessKeyEntity.getId()
                ));
            } else {
                UUID accessKey = UUID.randomUUID();
                UUID refreshKey = UUID.randomUUID();

                Integer userId = refreshKeyEntity.getUser().getId();
                accessKeyEntity.setUuid(passwordEncoder.encode(accessKey.toString()));
                refreshKeyEntity.setUuid(passwordEncoder.encode(refreshKey.toString()));
                keyRepository.save(refreshKeyEntity);

                return Pair.of(
                        tokenEncoder.getAccessToken(payloadMapper.toMap(new JwtPayload()
                                .setKeyId(accessKeyEntity.getId())
                                .setUserId(userId)
                                .setUuid(accessKey)
                        )),
                        tokenEncoder.getRefreshToken(payloadMapper.toMap(new JwtPayload()
                                .setKeyId(refreshKeyEntity.getId())
                                .setUserId(userId)
                                .setUuid(refreshKey)
                        ))
                );
            }
        }
    }

    @Override
    public IdentifiedUserDetails authentication(String accessToken) throws JwtException {
        return detailsMapper.toIdentifiedUserDetails(tokenEncoder.verify(accessToken));
    }

    @Deprecated
    @SuppressWarnings("DeprecatedIsStillUsed")
    public String login(IdentifiedUserDetails identifiedUserDetails) throws JwtException {
        return tokenEncoder.getAccessToken(detailsMapper.toMap(identifiedUserDetails));
    }

    private enum KeyType {
        ACCESS, REFRESH
    }
}
