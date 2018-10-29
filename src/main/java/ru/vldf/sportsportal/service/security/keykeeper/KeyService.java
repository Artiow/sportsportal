package ru.vldf.sportsportal.service.security.keykeeper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.LockedException;
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
import ru.vldf.sportsportal.service.security.encoder.ExpirationType;
import ru.vldf.sportsportal.service.security.encoder.ExpiringClockProvider;
import ru.vldf.sportsportal.service.security.userdetails.IdentifiedUserDetails;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Service
public class KeyService implements KeyProvider {

    private MessageContainer messages;
    private PasswordEncoder passwordEncoder;
    private ExpiringClockProvider clockProvider;

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
    public void setClockProvider(ExpiringClockProvider clockProvider) {
        this.clockProvider = clockProvider;
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
    @Transactional(noRollbackFor = {EntityNotFoundException.class, BadCredentialsException.class, DisabledException.class, LockedException.class})
    public Pair<Payload, Payload> authentication(String email, String password) {
        UserEntity userEntity;

        try {
            userEntity = userRepository.findByEmail(email);
            if (userEntity == null) {
                throw new EntityNotFoundException(messages.get("sportsportal.auth.service.userNotFound.message"));
            } else if (!passwordEncoder.matches(password, userEntity.getPassword())) {
                throw new BadCredentialsException(messages.get("sportsportal.auth.service.passwordEncoder.message"));
            } else if (userEntity.getDisabled()) {
                throw new DisabledException(messages.get("sportsportal.auth.service.accountDisabled.message"));
            }
        } catch (EntityNotFoundException | BadCredentialsException e) {
            throw new UsernameNotFoundException(messages.get("sportsportal.auth.service.loginError.message"), e);
        }

        if (userEntity.getLocked()) {
            throw new LockedException(messages.get("sportsportal.auth.service.accountLocked.message"));
        } else if (!(userEntity.getKeys().size() < 20)) {
            userEntity.setLocked(true);
            userEntity.getKeys().clear();
            userRepository.save(userEntity);
            throw new LockedException(messages.get("sportsportal.auth.service.accountLocked.message"));
        } else {
            KeyEntity keyEntity = new KeyEntity();
            keyEntity.setUser(userEntity);
            keyEntity.setType(ExpirationType.ACCESS.name());
            keyEntity.setRelated(new KeyEntity());
            keyEntity.getRelated().setUser(userEntity);
            keyEntity.getRelated().setType(ExpirationType.REFRESH.name());
            keyEntity.getRelated().setRelated(keyEntity);
            return getGeneratedPayloadPair(keyEntity);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public IdentifiedUserDetails authorization(Payload accessKey) {
        return detailsMapper.toDetails(getValidatedKeyEntity(accessKey, ExpirationType.ACCESS).getUser());
    }

    @Override
    @Transactional
    public Pair<Payload, Payload> refresh(Payload refreshKey) {
        return getGeneratedPayloadPair(getValidatedKeyEntity(refreshKey, ExpirationType.REFRESH));
    }

    @Override
    @Transactional
    public void logout(Payload accessKey) {
        keyRepository.delete(getValidatedKeyEntity(accessKey, ExpirationType.ACCESS));
    }

    @Override
    @Transactional
    public void logoutAll(Payload accessKey) {
        UserEntity userEntity = getValidatedKeyEntity(accessKey, ExpirationType.ACCESS).getUser();
        userEntity.getKeys().clear();
        userRepository.save(userEntity);
    }

    private KeyEntity getValidatedKeyEntity(Payload key, ExpirationType requiredKeyType) throws BadCredentialsException, InsufficientAuthenticationException {
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
        ExpirationType directKeyType;
        ExpirationType relatedKeyType;

        try {
            directKeyType = ExpirationType.valueOf(keyEntity.getType());
            relatedKeyType = ExpirationType.valueOf(keyEntity.getRelated().getType());
        } catch (NullPointerException | IllegalArgumentException e) {
            throw new IllegalArgumentException("One or more keys has no type or null", e);
        }

        if (directKeyType.equals(relatedKeyType)) {
            throw new IllegalArgumentException("Keys must be of a different type");
        } else if (!directKeyType.equals(ExpirationType.ACCESS)) {
            keyEntity = keyEntity.getRelated();
        }

        UUID newAccessKey = UUID.randomUUID();
        UUID newRefreshKey = UUID.randomUUID();
        Pair<Date, Date> newAccessDates = clockProvider.gen(ExpirationType.ACCESS);
        Pair<Date, Date> refreshDates = clockProvider.gen(ExpirationType.REFRESH);

        keyEntity.setUuid(newAccessKey);
        keyEntity.setIssuedAt(new Timestamp(newAccessDates.getFirst().getTime()));
        keyEntity.setExpiredAt(new Timestamp(newAccessDates.getSecond().getTime()));
        keyEntity.getRelated().setUuid(newRefreshKey);
        keyEntity.getRelated().setIssuedAt(new Timestamp(refreshDates.getFirst().getTime()));
        keyEntity.getRelated().setExpiredAt(new Timestamp(refreshDates.getSecond().getTime()));
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
