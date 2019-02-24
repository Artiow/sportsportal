package ru.vldf.sportsportal.service.security.keykeeper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.vldf.sportsportal.config.messages.MessageContainer;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;
import ru.vldf.sportsportal.mapper.manual.security.UserDetailsMapper;
import ru.vldf.sportsportal.repository.common.UserRepository;
import ru.vldf.sportsportal.service.security.userdetails.IdentifiedUserDetails;

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
    public IdentifiedUserDetails authorization(String email, String password) throws UsernameNotFoundException, BadCredentialsException {
        return userDetailsMapper.toDetails(getUserEntity(email, password));
    }

    @Override
    public IdentifiedUserDetails authorization(Payload accessKey) throws UsernameNotFoundException {
        return userDetailsMapper.toDetails(getUserEntity(accessKey.getUserId()));
    }

    @Override
    public Pair<Payload, Payload> access(String email, String password) throws UsernameNotFoundException, BadCredentialsException {
        return getPayloadPair(getUserEntity(email, password).getId(), false);
    }

    @Override
    public Pair<Payload, Payload> refresh(Payload refreshKey) throws UsernameNotFoundException {
        return getPayloadPair(refreshKey.getUserId(), true);
    }


    /**
     * Return user entity by user email and password.
     *
     * @param email    the user email.
     * @param password the user password.
     * @return user entity.
     * @throws UsernameNotFoundException if such user does not exist.
     * @throws BadCredentialsException   if password is wrong.
     */
    private UserEntity getUserEntity(String email, String password) throws UsernameNotFoundException, BadCredentialsException {
        UserEntity userEntity;
        if ((userEntity = userRepository.findByEmail(email)) == null) {
            throw new UsernameNotFoundException(messages.get("sportsportal.auth.service.usernameNotFound.message"));
        } else if (!passwordEncoder.matches(password, userEntity.getPassword())) {
            throw new BadCredentialsException(messages.get("sportsportal.auth.service.badCredentials.message"));
        } else {
            return userEntity;
        }
    }

    /**
     * Return user entity by user identifier.
     *
     * @param userId the user identifier.
     * @return user entity.
     * @throws UsernameNotFoundException if such user does not exist.
     */
    private UserEntity getUserEntity(Integer userId) throws UsernameNotFoundException {
        return userRepository.findById(userId).orElseThrow(
                () -> new UsernameNotFoundException(messages.get("sportsportal.auth.service.usernameNotFound.message"))
        );
    }

    /**
     * Returns token payload pair (access and refresh) by user identifier.
     *
     * @param userId         the user identifier.
     * @param existenceCheck {@literal true} if existence check enabled, {@literal false} otherwise.
     * @return token payload pair (access and refresh).
     * @throws UsernameNotFoundException if such user does not exist.
     */
    private Pair<Payload, Payload> getPayloadPair(Integer userId, boolean existenceCheck) throws UsernameNotFoundException {
        if (existenceCheck && !userRepository.existsById(userId)) {
            throw new UsernameNotFoundException(messages.get("sportsportal.auth.service.usernameNotFound.message"));
        } else {
            Payload newAccessPayload = new Payload();
            newAccessPayload.setUserId(userId);
            newAccessPayload.setKeyUuid(UUID.randomUUID());
            Payload newRefreshPayload = new Payload();
            newRefreshPayload.setUserId(userId);
            newAccessPayload.setKeyUuid(UUID.randomUUID());
            return Pair.of(newAccessPayload, newRefreshPayload);
        }
    }
}
