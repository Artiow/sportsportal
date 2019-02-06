package ru.vldf.sportsportal.service;

import org.postgresql.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.vldf.sportsportal.config.messages.MessageContainer;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;
import ru.vldf.sportsportal.dto.sectional.common.UserDTO;
import ru.vldf.sportsportal.dto.security.JwtPairDTO;
import ru.vldf.sportsportal.integration.mail.MailService;
import ru.vldf.sportsportal.mapper.sectional.common.UserMapper;
import ru.vldf.sportsportal.repository.common.RoleRepository;
import ru.vldf.sportsportal.repository.common.UserRepository;
import ru.vldf.sportsportal.service.generic.AbstractSecurityService;
import ru.vldf.sportsportal.service.generic.ResourceCannotCreateException;
import ru.vldf.sportsportal.service.generic.ResourceCannotUpdateException;
import ru.vldf.sportsportal.service.generic.ResourceNotFoundException;
import ru.vldf.sportsportal.service.security.SecurityProvider;

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.OptimisticLockException;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.UUID;

@Service
public class AuthService extends AbstractSecurityService {

    @Value("${code.role.user}")
    private String userRoleCode;

    @Value("${api.path.auth.confirm}")
    private String confirmPath;

    private PasswordEncoder passwordEncoder;

    private SecurityProvider securityProvider;
    private MailService mailService;
    private UserMapper userMapper;


    @Autowired
    public AuthService(MessageContainer messages, UserRepository userRepository, RoleRepository roleRepository) {
        super(messages, userRepository, roleRepository);
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setSecurityProvider(SecurityProvider securityProvider) {
        this.securityProvider = securityProvider;
    }

    @Autowired
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }


    /**
     * Logging user and returns pair of token.
     *
     * @param email    {@link String} users email
     * @param password {@link String} users password
     * @return {@link JwtPairDTO} token pair
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public JwtPairDTO login(@NotNull String email, @NotNull String password) {
        return buildJwtPair(securityProvider.authentication(email, password));
    }

    /**
     * Refresh user pair of token.
     *
     * @param refreshToken {@link String} refresh token
     * @return {@link JwtPairDTO} token pair
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public JwtPairDTO refresh(@NotNull String refreshToken) {
        return buildJwtPair(securityProvider.refresh(refreshToken));
    }

    /**
     * Logout user.
     *
     * @param accessToken {@link String} access token
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void logout(@NotNull String accessToken) {
        securityProvider.logout(accessToken);
    }

    /**
     * Logout all user sessions.
     *
     * @param accessToken {@link String} access token
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void logoutAll(@NotNull String accessToken) {
        securityProvider.logoutAll(accessToken);
    }

    /**
     * Register new user and returns his id.
     *
     * @param userDTO {@link UserDTO} full user data
     * @return {@link Integer} user identifier
     * @throws ResourceCannotCreateException if user could not create
     */
    @Transactional(
            rollbackFor = {ResourceCannotCreateException.class},
            noRollbackFor = {EntityNotFoundException.class, OptimisticLockException.class, DataAccessException.class}
    )
    public Integer register(@NotNull UserDTO userDTO) throws ResourceCannotCreateException {
        String email = userDTO.getEmail();
        UserRepository userRepository = userRepository();
        if (userRepository.isEnabledByEmail(email)) {
            throw new ResourceCannotCreateException(msg("sportsportal.common.User.alreadyExistByEmail.message", email));
        } else try {
            UserEntity userEntity = userMapper.toEntity(userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword())));
            userEntity.setRoles(Collections.emptyList());
            userEntity.setIsDisabled(true);
            if (userRepository.existsByEmail(email)) {
                try {
                    userEntity = userMapper.merge(userRepository.findByEmail(email), userEntity);
                } catch (EntityNotFoundException e) {
                    throw new RuntimeException(msg("sportsportal.common.User.cannotCreate.message"), e);
                } catch (OptimisticLockException e) {
                    throw new ResourceCannotCreateException(msg("sportsportal.common.User.cannotCreate.message"), e);
                }
            }
            return userRepository.save(userEntity).getId();
        } catch (DataAccessException e) {
            throw new ResourceCannotCreateException(msg("sportsportal.common.User.cannotCreate.message"), e);
        }
    }

    /**
     * Init confirmation for user.
     *
     * @param userId        {@link Integer} user identifier
     * @param confirmOrigin {@link String} confirmation link origin
     * @throws ResourceNotFoundException     if user could not found
     * @throws ResourceCannotUpdateException if could not sent email
     */
    @Transactional(
            rollbackFor = {ResourceNotFoundException.class, ResourceCannotUpdateException.class},
            noRollbackFor = {EntityNotFoundException.class}
    )
    public void initConfirmation(Integer userId, String confirmOrigin) throws ResourceNotFoundException, ResourceCannotUpdateException {
        String confirmCode = Base64.encodeBytes(UUID.randomUUID().toString().getBytes());
        UserRepository userRepository = userRepository();
        try {
            UserEntity userEntity = userRepository.getOne(userId);
            if (userEntity.getRoles().isEmpty()) {
                userEntity.setConfirmCode(confirmCode);
                sendConfirmationEmail(userEntity.getEmail(), confirmOrigin, confirmCode);
                userRepository.save(userEntity);
            } else {
                throw new ResourceCannotUpdateException(msg("sportsportal.common.User.alreadyConfirmed.message"));
            }
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(msg("sportsportal.common.User.notExistById.message", userId), e);
        } catch (MessagingException e) {
            throw new ResourceCannotUpdateException(msg("sportsportal.common.User.cannotSendEmail.message"), e);
        }
    }

    /**
     * Confirm user and give him user role.
     *
     * @param confirmCode {@link String} user's confirmation code
     * @throws ResourceNotFoundException if user not found by confirm code
     */
    @Transactional(
            rollbackFor = {ResourceNotFoundException.class}
    )
    public void confirm(String confirmCode) throws ResourceNotFoundException {
        UserRepository userRepository = userRepository();
        UserEntity userEntity = userRepository.findByConfirmCode(confirmCode);
        if (userEntity == null) {
            throw new ResourceNotFoundException(msg("sportsportal.common.User.notExistByConfirmCode.message"));
        } else {
            userEntity.setIsDisabled(false);
            userEntity.setConfirmCode(null);
            userEntity.setRoles(roleRepository().findAllByCode(userRoleCode));
            userRepository.save(userEntity);
        }
    }

    /**
     * Returns built jwt pair.
     *
     * @param jwtPair {@link Pair} raw jwt pair
     * @return {@link JwtPairDTO} built jwt pair
     */
    private JwtPairDTO buildJwtPair(Pair<String, String> jwtPair) {
        return new JwtPairDTO()
                .setAccessToken(jwtPair.getFirst())
                .setRefreshToken(jwtPair.getSecond());
    }

    /**
     * @param emailAddress  {@link String} sending address
     * @param confirmOrigin {@link String} confirm host
     * @param confirmCode   {@link String} confirm code
     * @throws MessagingException if could not sent email
     */
    private void sendConfirmationEmail(String emailAddress, String confirmOrigin, String confirmCode) throws MessagingException {
        mailService.sender()
                .setDestination(emailAddress)
                .setSubject(msg("sportsportal.email.confirm.subject"))
                .setHtml(String.format(
                        "<p>%s</p>",
                        msg(
                                "sportsportal.email.confirm.text.env",
                                String.format(
                                        "<a href=\"%s\">%s</a>",
                                        String.format(confirmPath, confirmOrigin, confirmCode),
                                        msg("sportsportal.email.confirm.text.link")
                                )
                        )
                )).send();
    }
}
