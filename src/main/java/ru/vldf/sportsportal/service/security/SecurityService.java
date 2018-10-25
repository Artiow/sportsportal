package ru.vldf.sportsportal.service.security;

import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vldf.sportsportal.domain.sectional.security.KeyEntity;
import ru.vldf.sportsportal.dto.security.JwtPayload;
import ru.vldf.sportsportal.mapper.manual.security.JwtPayloadMapper;
import ru.vldf.sportsportal.mapper.manual.security.UserDetailsMapper;
import ru.vldf.sportsportal.repository.security.KeyRepository;
import ru.vldf.sportsportal.service.security.userdetails.IdentifiedUserDetails;

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
    public String getTokenType() {
        return tokenEncoder.getTokenType();
    }


    @Override
    public Pair<String, String> authorization(Integer userId) throws JwtException {
        throw new UnsupportedOperationException();
    }

    @Override
    @Transactional(
            rollbackFor = {JwtException.class, SecurityException.class},
            noRollbackFor = {EntityNotFoundException.class, DataAccessException.class}
    )
    public Pair<String, String> authorization(String refreshToken) throws JwtException, SecurityException {
        JwtPayload payload = payloadMapper.toJwtPayload(tokenEncoder.verify(refreshToken));
        try {
            KeyEntity key = keyRepository.getOne(payload.getKeyId());
            if (!payload.getUserId().equals(key.getUser().getId())) {
                throw new SecurityException("Key user id does not match with stored user id");
            } else if (!KeyType.REFRESH.toString().equals(key.getType())) {
                throw new SecurityException("Key type does not match with stored key type");
            } else if (!passwordEncoder.matches(payload.getUuid().toString(), key.getUuid().toString())) {
                throw new SecurityException("Key uuid does not match with stored uuid");
            } else try {
                String accessKey = UUID.randomUUID().toString();
                String refreshKey = UUID.randomUUID().toString();
                throw new UnsupportedOperationException();
            } catch (DataAccessException e) {
                throw new SecurityException(e);
            }
        } catch (EntityNotFoundException e) {
            throw new SecurityException(String.format("Key with id %d does not exist", payload.getKeyId()), e);
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
