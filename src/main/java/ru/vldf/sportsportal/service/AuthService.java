package ru.vldf.sportsportal.service;

import org.postgresql.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vldf.sportsportal.config.messages.MessageContainer;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;
import ru.vldf.sportsportal.dto.sectional.common.UserDTO;
import ru.vldf.sportsportal.dto.sectional.common.shortcut.UserShortDTO;
import ru.vldf.sportsportal.dto.security.JwtPairDTO;
import ru.vldf.sportsportal.mapper.sectional.common.UserMapper;
import ru.vldf.sportsportal.repository.common.RoleRepository;
import ru.vldf.sportsportal.repository.common.UserRepository;
import ru.vldf.sportsportal.service.generic.AbstractSecurityService;
import ru.vldf.sportsportal.service.generic.ResourceCannotCreateException;
import ru.vldf.sportsportal.service.generic.ResourceCannotUpdateException;
import ru.vldf.sportsportal.service.generic.ResourceNotFoundException;
import ru.vldf.sportsportal.service.security.SecurityProvider;
import ru.vldf.sportsportal.service.subsidiary.MailService;

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.OptimisticLockException;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
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
     * Returns users short data by user id.
     *
     * @param id {@link Integer} user identifier
     * @return {@link UserShortDTO} users data
     * @throws ResourceNotFoundException if user with sent id not found
     */
    @Transactional(
            readOnly = true,
            rollbackFor = {ResourceNotFoundException.class},
            noRollbackFor = {EntityNotFoundException.class}
    )
    public UserShortDTO get(@NotNull Integer id) throws ResourceNotFoundException {
        try {
            return userMapper.toShortDTO(userRepository().getOne(id));
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(mGetAndFormat("sportsportal.common.User.notExistById.message", id), e);
        }
    }

    /**
     * Logging user and returns pair of token.
     *
     * @param email    {@link String} users email
     * @param password {@link String} users password
     * @return {@link JwtPairDTO} token pair
     */
    public JwtPairDTO login(@NotNull String email, @NotNull String password) {
        return buildJwtPair(securityProvider.authentication(email, password));
    }

    /**
     * Refresh user pair of token.
     *
     * @param refreshToken {@link String} refresh token
     * @return {@link JwtPairDTO} token pair
     */
    public JwtPairDTO refresh(@NotNull String refreshToken) {
        return buildJwtPair(securityProvider.refresh(refreshToken));
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
        if (userRepository.hasAnyRole(email)) {
            throw new ResourceCannotCreateException(mGetAndFormat("sportsportal.common.User.alreadyExistByEmail.message", email));
        } else try {
            UserEntity userEntity = userMapper.toEntity(userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword())));
            userEntity.setRoles(new ArrayList<>());
            if (userRepository.existsByEmail(email)) {
                try {
                    userEntity = userMapper.merge(userRepository.findByEmail(email), userEntity);
                } catch (EntityNotFoundException ignored) {
                } catch (OptimisticLockException e) {
                    throw new ResourceCannotCreateException(mGet("sportsportal.common.User.cannotCreate.message"), e);
                }
            }
            return userRepository.save(userEntity).getId();
        } catch (DataAccessException e) {
            throw new ResourceCannotCreateException(mGet("sportsportal.common.User.cannotCreate.message"), e);
        }
    }

    /**
     * Init confirmation for user.
     *
     * @param userId      {@link Integer} user identifier
     * @param confirmHost {@link String} confirmation link host
     * @throws ResourceNotFoundException     if user could not found
     * @throws ResourceCannotUpdateException if could not sent email
     */
    @Transactional(
            rollbackFor = {ResourceNotFoundException.class, ResourceCannotUpdateException.class},
            noRollbackFor = {EntityNotFoundException.class}
    )
    public void initConfirmation(Integer userId, String confirmHost) throws ResourceNotFoundException, ResourceCannotUpdateException {
        String confirmCode = Base64.encodeBytes(UUID.randomUUID().toString().getBytes());
        UserRepository userRepository = userRepository();
        try {
            UserEntity userEntity = userRepository.getOne(userId);
            if (userEntity.getRoles().isEmpty()) {
                userEntity.setConfirmCode(confirmCode);
                sendConfirmationEmail(userEntity.getEmail(), confirmHost, confirmCode);
                userRepository.save(userEntity);
            } else {
                throw new ResourceCannotUpdateException(mGet("sportsportal.common.User.alreadyConfirmed.message"));
            }
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(mGetAndFormat("sportsportal.common.User.notExistById.message", userId), e);
        } catch (MessagingException e) {
            throw new ResourceCannotUpdateException(mGet("sportsportal.common.User.cannotSendEmail.message"), e);
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
            throw new ResourceNotFoundException(mGet("sportsportal.common.User.notExistByConfirmCode.message"));
        } else {
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
     * @param emailAddress {@link String} sending address
     * @param confirmHost  {@link String} confirm host
     * @param confirmCode  {@link String} confirm code
     * @throws MessagingException if could not sent email
     */
    private void sendConfirmationEmail(String emailAddress, String confirmHost, String confirmCode) throws MessagingException {
        mailService.sender()
                .setDestination(emailAddress)
                .setSubject(mGet("sportsportal.email.confirm.subject"))
                .setHtml(String.format(
                        "<p>%s</p>",
                        mGetAndFormat(
                                "sportsportal.email.confirm.text.env",
                                String.format(
                                        "<a href=\"%s\">%s</a>",
                                        String.format(confirmPath, confirmHost, confirmCode),
                                        mGet("sportsportal.email.confirm.text.link")
                                )
                        )
                )).send();
    }
}
