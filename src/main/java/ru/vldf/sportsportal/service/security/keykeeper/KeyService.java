package ru.vldf.sportsportal.service.security.keykeeper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.vldf.sportsportal.config.messages.MessageContainer;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;
import ru.vldf.sportsportal.mapper.manual.security.UserDetailsMapper;
import ru.vldf.sportsportal.repository.common.UserRepository;
import ru.vldf.sportsportal.service.security.userdetails.IdentifiedUserDetails;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

/**
 * @author Namednev Artem
 */
@Service
public class KeyService implements KeyProvider {

    private final MessageContainer messages;
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final UserDetailsMapper userDetailsMapper;


    @Autowired
    public KeyService(
            MessageContainer messages,
            PasswordEncoder passwordEncoder,
            UserRepository userRepository,
            UserDetailsMapper userDetailsMapper
    ) {
        this.messages = messages;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userDetailsMapper = userDetailsMapper;
    }


    @Override
    public Pair<Payload, Payload> authentication(String email, String password) {
        try {
            UserEntity userEntity = userRepository.findByEmail(email);
            if (userEntity == null) {
                throw new EntityNotFoundException(messages.get("sportsportal.auth.service.userNotFound.message"));
            } else if (!passwordEncoder.matches(password, userEntity.getPassword())) {
                throw new BadCredentialsException(messages.get("sportsportal.auth.service.passwordEncoder.message"));
            } else if (userEntity.getIsLocked()) {
                throw new LockedException(messages.get("sportsportal.auth.service.accountLocked.message"));
            } else if (userEntity.getIsDisabled()) {
                throw new DisabledException(messages.get("sportsportal.auth.service.accountDisabled.message"));
            } else {
                return getGeneratedPayloadPair(userEntity);
            }
        } catch (EntityNotFoundException | BadCredentialsException e) {
            throw new UsernameNotFoundException(messages.get("sportsportal.auth.service.loginError.message"), e);
        }
    }

    @Override
    public Pair<Payload, Payload> refresh(Payload refreshKey) {
        return getGeneratedPayloadPair(getValidatedUserEntity(refreshKey));
    }

    @Override
    public IdentifiedUserDetails authorization(Payload accessKey) {
        return userDetailsMapper.toDetails(getValidatedUserEntity(accessKey));
    }


    private UserEntity getValidatedUserEntity(Payload key) throws UsernameNotFoundException {
        try {
            UserEntity userEntity = userRepository.getOne(key.getUserId());
            if (userEntity.getIsLocked()) {
                throw new LockedException(messages.get("sportsportal.auth.service.accountLocked.message"));
            } else if (userEntity.getIsDisabled()) {
                throw new DisabledException(messages.get("sportsportal.auth.service.accountDisabled.message"));
            } else {
                return userEntity;
            }
        } catch (EntityNotFoundException e) {
            throw new UsernameNotFoundException(messages.get("sportsportal.auth.service.userNotFound.message"));
        }
    }

    private Pair<Payload, Payload> getGeneratedPayloadPair(UserEntity userEntity) throws IllegalArgumentException {
        Integer userId = userEntity.getId();
        Payload newAccessPayload = new Payload();
        newAccessPayload.setUserId(userId);
        newAccessPayload.setKeyUuid(UUID.randomUUID());
        Payload newRefreshPayload = new Payload();
        newRefreshPayload.setUserId(userId);
        newAccessPayload.setKeyUuid(UUID.randomUUID());
        return Pair.of(newAccessPayload, newRefreshPayload);
    }
}
