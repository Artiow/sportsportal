package ru.vldf.sportsportal.service;

import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vldf.sportsportal.config.messages.MessageContainer;
import ru.vldf.sportsportal.domain.UserEntity;
import ru.vldf.sportsportal.dto.UserDTO;
import ru.vldf.sportsportal.dto.security.TokenDTO;
import ru.vldf.sportsportal.dto.shortcut.UserShortDTO;
import ru.vldf.sportsportal.mapper.UserMapper;
import ru.vldf.sportsportal.repository.RoleRepository;
import ru.vldf.sportsportal.repository.UserRepository;
import ru.vldf.sportsportal.service.generic.ResourceCannotCreateException;
import ru.vldf.sportsportal.service.generic.ResourceNotFoundException;
import ru.vldf.sportsportal.service.security.SecurityService;
import ru.vldf.sportsportal.service.security.userdetails.IdentifiedUser;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;

@Service
public class UserService {

    private MessageContainer messages;

    private BCryptPasswordEncoder passwordEncoder;

    private SecurityService securityService;

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    private UserMapper userMapper;

    @Autowired
    public void setMessages(MessageContainer messages) {
        this.messages = messages;
    }

    @Autowired
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }


    /**
     * Returns user data by user id.
     *
     * @param id - user id
     * @return user's data
     * @throws ResourceNotFoundException - if user with sent id not found
     */
    @Transactional(readOnly = true)
    public UserShortDTO get(@NotNull Integer id) throws ResourceNotFoundException {
        try {
            return userMapper.toShortDTO(userRepository.getOne(id));
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(messages.getAndFormat("sportsportal.User.notExistById.message", id), e);
        }
    }

    /**
     * Login user and returns TokenDTO.
     *
     * @param login    - user's login
     * @param password - users's password
     * @return token info
     * @throws UsernameNotFoundException - if user not found
     * @throws JwtException              - if could not parse jwt
     */
    @Transactional(readOnly = true)
    public TokenDTO login(@NotNull String login, @NotNull String password) throws UsernameNotFoundException, JwtException {
        UserEntity user;

        try {
            user = userRepository.findByLogin(login);
            if (user == null) {
                throw new EntityNotFoundException(messages.get("sportsportal.auth.service.repository.message"));
            } else if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new BadCredentialsException(messages.get("sportsportal.auth.service.encoder.message"));
            }

        } catch (EntityNotFoundException | BadCredentialsException e) {
            throw new UsernameNotFoundException(messages.get("sportsportal.auth.service.loginError.message"), e);
        }

        return new TokenDTO()
                .setLogin(userMapper.toLoginDTO(user))
                .setTokenType(securityService.getTokenType())
                .setAccessToken(
                        securityService.login(
                                new IdentifiedUser(
                                        user.getId(),
                                        User.builder()
                                                .username(user.getLogin())
                                                .password(user.getPassword())

                                                // todo: multiple roles!
                                                .roles(user.getRoles().iterator().next().getCode().toUpperCase())

                                                .build()
                                )
                        )
                );
    }

    /**
     * Register new user and returns his id.
     *
     * @param userDTO - user data
     * @return user id
     * @throws ResourceCannotCreateException - if user could not create
     */
    @Transactional
    public Integer register(@NotNull UserDTO userDTO) throws ResourceCannotCreateException {
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        // todo: multiple roles!
        Integer roleId = userDTO.getRoles().iterator().next().getId();
        String login = userDTO.getLogin();

        if (!roleRepository.existsById(roleId)) {
            throw new ResourceCannotCreateException(messages.getAndFormat(
                    "sportsportal.Role.notExistById.message", roleId
            ));
        } else if (userRepository.existsByLogin(login)) {
            throw new ResourceCannotCreateException(messages.getAndFormat(
                    "sportsportal.User.alreadyExistByLogin.message", login
            ));
        }

        return userRepository.save(userMapper.toEntity(userDTO)).getId();
    }
}
