package ru.vldf.sportsportal.service.security.keykeeper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vldf.sportsportal.config.messages.MessageContainer;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;
import ru.vldf.sportsportal.domain.sectional.security.KeyEntity;
import ru.vldf.sportsportal.mapper.manual.security.UserDetailsMapper;
import ru.vldf.sportsportal.repository.common.UserRepository;
import ru.vldf.sportsportal.repository.security.KeyRepository;
import ru.vldf.sportsportal.service.security.userdetails.IdentifiedUserDetails;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.UUID;

@Service
public class KeyService implements KeyProvider {

    private MessageContainer messages;
    private PasswordEncoder passwordEncoder;

    private KeyRepository keyRepository;
    private UserRepository userRepository;
    private UserDetailsMapper detailsMapper;

    @Autowired
    public void setMessages(MessageContainer messages) {
        this.messages = messages;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setKeyRepository(KeyRepository keyRepository) {
        this.keyRepository = keyRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setDetailsMapper(UserDetailsMapper detailsMapper) {
        this.detailsMapper = detailsMapper;
    }


    @Override
    @Transactional(noRollbackFor = {EntityNotFoundException.class, BadCredentialsException.class})
    public Pair<Payload, Payload> authentication(String email, String password) {
        UserEntity userEntity;
        try {
            userEntity = userRepository.findByEmail(email);
            if (userEntity == null) {
                throw new EntityNotFoundException(messages.get("sportsportal.auth.service.userNotFound.message"));
            } else if (!passwordEncoder.matches(password, userEntity.getPassword())) {
                throw new BadCredentialsException(messages.get("sportsportal.auth.service.passwordEncoder.message"));
            }
        } catch (EntityNotFoundException | BadCredentialsException e) {
            throw new UsernameNotFoundException(messages.get("sportsportal.auth.service.loginError.message"), e);
        }

        KeyEntity keyEntity = new KeyEntity();
        keyEntity.setUser(userEntity);
        keyEntity.setType(KeyType.ACCESS.name());
        keyEntity.setRelated(new KeyEntity());
        keyEntity.getRelated().setUser(userEntity);
        keyEntity.getRelated().setType(KeyType.REFRESH.name());
        keyEntity.getRelated().setRelated(keyEntity);

        return getGeneratedPayloadPair(keyEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public IdentifiedUserDetails authorization(Payload accessKey) {
        return detailsMapper.toDetails(getValidatedKeyEntity(accessKey, KeyType.ACCESS).getUser());
    }

    @Override
    @Transactional
    public Pair<Payload, Payload> refresh(Payload refreshKey) {
        return getGeneratedPayloadPair(getValidatedKeyEntity(refreshKey, KeyType.REFRESH));
    }

    @Override
    @Transactional
    public void logout(Payload accessKey) {
        keyRepository.delete(getValidatedKeyEntity(accessKey, KeyType.ACCESS));
    }

    @Override
    @Transactional
    public void logoutAll(Payload accessKey) {
        UserEntity userEntity = getValidatedKeyEntity(accessKey, KeyType.ACCESS).getUser();
        userEntity.setKeys(Collections.emptyList());
        userRepository.save(userEntity);
    }

    private KeyEntity getValidatedKeyEntity(Payload key, KeyType requiredKeyType) throws BadCredentialsException, InsufficientAuthenticationException {
        try {
            KeyEntity keyEntity = keyRepository.getOne(key.getKeyId());
            if (!keyEntity.getType().equals(requiredKeyType.name())) {
                throw new BadCredentialsException(messages.get("sportsportal.auth.key.invalidTokenType.message"));
            } else if (!keyEntity.getUuid().equals(key.getUuid())) {
                throw new InsufficientAuthenticationException(messages.get("sportsportal.auth.key.insufficientToken.message"));
            } else {
                return keyEntity;
            }
        } catch (EntityNotFoundException e) {
            throw new BadCredentialsException(messages.get("sportsportal.auth.key.tokenNotFound.message"));
        }
    }

    private Pair<Payload, Payload> getGeneratedPayloadPair(KeyEntity keyEntity) throws IllegalArgumentException {
        KeyType directKeyType;
        KeyType relatedKeyType;

        try {
            directKeyType = KeyType.valueOf(keyEntity.getType());
            relatedKeyType = KeyType.valueOf(keyEntity.getRelated().getType());
        } catch (NullPointerException | IllegalArgumentException e) {
            throw new IllegalArgumentException("One or more keys has no type or null", e);
        }

        if (directKeyType.equals(relatedKeyType)) {
            throw new IllegalArgumentException("Keys must be of a different type");
        } else if (!directKeyType.equals(KeyType.ACCESS)) {
            keyEntity = keyEntity.getRelated();
        }

        UUID newAccessKey = UUID.randomUUID();
        UUID newRefreshKey = UUID.randomUUID();
        keyEntity.setUuid(newAccessKey);
        keyEntity.getRelated().setUuid(newRefreshKey);
        keyEntity = keyRepository.save(keyEntity);

        Integer userId = keyEntity.getUser().getId();

        return Pair.of(
                new Payload()
                        .setUserId(userId)
                        .setKeyId(keyEntity.getId())
                        .setUuid(newAccessKey),
                new Payload()
                        .setUserId(userId)
                        .setKeyId(keyEntity.getRelated().getId())
                        .setUuid(newRefreshKey)
        );
    }
}
