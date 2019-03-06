package ru.vldf.sportsportal.service.security.userdetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vldf.sportsportal.config.messages.MessageContainer;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;
import ru.vldf.sportsportal.mapper.manual.security.UserDetailsMapper;
import ru.vldf.sportsportal.repository.common.UserRepository;
import ru.vldf.sportsportal.service.security.userdetails.model.IdentifiedUserDetails;

/**
 * @author Namednev Artem
 */
@Service
public class UserDetailsService implements UserDetailsProvider {

    private final MessageContainer messages;
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final UserDetailsMapper userDetailsMapper;


    @Autowired
    public UserDetailsService(
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
    @Transactional
    public IdentifiedUserDetails authorization(String email, String password) throws UsernameNotFoundException, BadCredentialsException {
        UserEntity userEntity;
        if ((userEntity = userRepository.findByEmail(email)) == null) {
            throw new UsernameNotFoundException(messages.get("sportsportal.auth.service.usernameNotFound.message"));
        } else if (!passwordEncoder.matches(password, userEntity.getPassword())) {
            throw new BadCredentialsException(messages.get("sportsportal.auth.service.badCredentials.message"));
        } else {
            return userDetailsMapper.toDetails(userEntity);
        }
    }

    @Override
    @Transactional
    public IdentifiedUserDetails authorization(Integer userId) throws UsernameNotFoundException {
        return userRepository.findById(userId).map(userDetailsMapper::toDetails).orElseThrow(
                () -> new UsernameNotFoundException(messages.get("sportsportal.auth.service.usernameNotFound.message"))
        );
    }
}
