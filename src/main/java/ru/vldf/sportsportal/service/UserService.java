package ru.vldf.sportsportal.service;

import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vldf.sportsportal.config.messages.MessageContainer;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;
import ru.vldf.sportsportal.dto.sectional.common.UserDTO;
import ru.vldf.sportsportal.dto.sectional.common.shortcut.UserShortDTO;
import ru.vldf.sportsportal.dto.security.TokenDTO;
import ru.vldf.sportsportal.mapper.sectional.common.UserMapper;
import ru.vldf.sportsportal.mapper.security.LoginMapper;
import ru.vldf.sportsportal.repository.common.RoleRepository;
import ru.vldf.sportsportal.repository.common.UserRepository;
import ru.vldf.sportsportal.service.generic.AbstractMessageService;
import ru.vldf.sportsportal.service.generic.ResourceCannotCreateException;
import ru.vldf.sportsportal.service.generic.ResourceNotFoundException;
import ru.vldf.sportsportal.service.security.SecurityService;
import ru.vldf.sportsportal.service.security.userdetails.IdentifiedUserDetails;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;

@Service
public class UserService extends AbstractMessageService {

    @Value("${code.role.user}")
    private String userRoleCode;

    private BCryptPasswordEncoder passwordEncoder;

    private SecurityService securityService;

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    private LoginMapper loginMapper;
    private UserMapper userMapper;

    @Autowired
    public void setMessages(MessageContainer messages) {
        super.setMessages(messages);
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
    public void setLoginMapper(LoginMapper loginMapper) {
        this.loginMapper = loginMapper;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }


    /**
     * Returns users short data by user id.
     *
     * @param id {@link Integer} user identifier
     * @return {@link UserShortDTO} users data
     * @throws ResourceNotFoundException if user with sent id not found
     */
    @Transactional(readOnly = true)
    public UserShortDTO get(@NotNull Integer id) throws ResourceNotFoundException {
        try {
            return userMapper.toShortDTO(userRepository.getOne(id));
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(mGetAndFormat("sportsportal.common.User.notExistById.message", id), e);
        }
    }

    /**
     * Logging user and returns his token.
     *
     * @param login    {@link String} users login
     * @param password {@link String} users password
     * @return {@link TokenDTO} token info
     * @throws UsernameNotFoundException if user not found
     * @throws JwtException              if could not parse jwt
     */
    @Transactional(readOnly = true)
    public TokenDTO login(@NotNull String login, @NotNull String password) throws UsernameNotFoundException, JwtException {
        UserEntity user;

        try {
            user = userRepository.findByLogin(login);
            if (user == null) {
                throw new EntityNotFoundException(mGet("sportsportal.auth.service.userRepository.message"));
            } else if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new BadCredentialsException(mGet("sportsportal.auth.service.passwordEncoder.message"));
            }

        } catch (EntityNotFoundException | BadCredentialsException e) {
            throw new UsernameNotFoundException(mGet("sportsportal.auth.service.loginError.message"), e);
        }

        return new TokenDTO()
                .setUserInfo(loginMapper.toLoginDTO(user))
                .setTokenType(securityService.getTokenType())
                .setTokenHash(securityService.login(loginMapper.toIdentifiedUser(user)));
    }

    /**
     * Verify user and returns his token.
     *
     * @param accessToken {@link String} access token
     * @return {@link TokenDTO} token info
     * @throws UsernameNotFoundException if user not found
     * @throws JwtException              if could not parse jwt
     */
    @Transactional(readOnly = true)
    public TokenDTO verify(String accessToken) throws UsernameNotFoundException, JwtException {
        // todo: debug!
        final String tokenType = securityService.getTokenType();
        if ((accessToken == null) || (!accessToken.startsWith(tokenType))) {
            throw new BadCredentialsException(mGet("sportsportal.auth.filter.credentialsNotValid.message"));
        } else {
            IdentifiedUserDetails userDetails = securityService.authentication(accessToken.substring(tokenType.length()).trim());
            UserEntity user = userRepository.findById(userDetails.getId()).orElseThrow(
                    () -> new EntityNotFoundException(mGet("sportsportal.auth.service.userRepository.message"))
            );

            String login = userDetails.getUsername();
            String password = userDetails.getPassword();
            if ((!user.getLogin().equals(login)) || (!user.getPassword().equals(password))) {
                throw new UsernameNotFoundException(mGet("sportsportal.auth.service.loginError.message"));
            } else {
                return new TokenDTO()
                        .setUserInfo(loginMapper.toLoginDTO(user))
                        .setTokenType(securityService.getTokenType())
                        .setTokenHash(securityService.login(loginMapper.toIdentifiedUser(user)));
            }
        }
    }

    /**
     * Register new user and returns his id.
     *
     * @param userDTO {@link UserDTO} full user data
     * @return {@link Integer} user identifier
     * @throws ResourceCannotCreateException if user could not create
     */
    @Transactional
    public Integer register(@NotNull UserDTO userDTO) throws ResourceCannotCreateException {
        String login = userDTO.getLogin();
        if (userRepository.existsByLogin(login)) {
            throw new ResourceCannotCreateException(mGetAndFormat("sportsportal.common.User.alreadyExistByLogin.message", login));
        }

        UserEntity user = userMapper.toEntity(userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword())));
        user.setRoles(roleRepository.findAllByCode(userRoleCode));

        return userRepository.save(user).getId();
    }
}
