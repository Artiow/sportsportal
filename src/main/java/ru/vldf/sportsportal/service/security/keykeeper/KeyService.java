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
    @Transactional(
            rollbackFor = {UsernameNotFoundException.class},
            noRollbackFor = {EntityNotFoundException.class, BadCredentialsException.class}
    )
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

        UUID accessKey = UUID.randomUUID();
        UUID refreshKey = UUID.randomUUID();

        KeyEntity keyEntity = new KeyEntity();
        keyEntity.setUser(userEntity);
        keyEntity.setType(KeyType.ACCESS.name());
        keyEntity.setUuid(accessKey);
        keyEntity.setRelated(new KeyEntity());
        keyEntity.getRelated().setUser(userEntity);
        keyEntity.getRelated().setType(KeyType.REFRESH.name());
        keyEntity.getRelated().setUuid(refreshKey);
        keyEntity.getRelated().setRelated(keyEntity);
        keyEntity = keyRepository.save(keyEntity);

        return Pair.of(
                new Payload()
                        .setUserId(userEntity.getId())
                        .setKeyId(keyEntity.getId())
                        .setUuid(accessKey),
                new Payload()
                        .setUserId(userEntity.getId())
                        .setKeyId(keyEntity.getRelated().getId())
                        .setUuid(refreshKey)
        );
    }

    @Override
    @Transactional(
            readOnly = true,
            rollbackFor = {BadCredentialsException.class},
            noRollbackFor = {EntityNotFoundException.class}
    )
    public IdentifiedUserDetails authorization(Payload accessKey) {
        try {
            KeyEntity keyEntity = keyRepository.getOne(accessKey.getKeyId());
            if (!keyEntity.getType().equals(KeyType.ACCESS.name())) {
                throw new BadCredentialsException(messages.get("sportsportal.auth.key.invalidToken.message"));
            } else if (!keyEntity.getUuid().equals(accessKey.getUuid())) {
                throw new InsufficientAuthenticationException(messages.get("sportsportal.auth.key.insufficientToken.message"));
            } else {
                return detailsMapper.toDetails(keyEntity.getUser());
            }
        } catch (EntityNotFoundException e) {
            throw new BadCredentialsException(messages.get("sportsportal.auth.key.tokenNotFound.message"));
        }
    }

    @Override
    @Transactional
    public Pair<Payload, Payload> refresh(Payload refreshKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Transactional
    public void logout(Payload accessKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Transactional
    public void logoutAll(Payload accessKey) {
        throw new UnsupportedOperationException();
    }
}
