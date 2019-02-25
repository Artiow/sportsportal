package ru.vldf.sportsportal.service;

import org.postgresql.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;
import ru.vldf.sportsportal.dto.sectional.common.UserDTO;
import ru.vldf.sportsportal.dto.security.JwtPairDTO;
import ru.vldf.sportsportal.integration.mail.MailService;
import ru.vldf.sportsportal.mapper.sectional.common.UserMapper;
import ru.vldf.sportsportal.repository.common.UserRepository;
import ru.vldf.sportsportal.service.generic.*;
import ru.vldf.sportsportal.service.security.SecurityProvider;

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.OptimisticLockException;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.UUID;

/**
 * @author Namednev Artem
 */
@Service
public class AuthService extends AbstractSecurityService {

    private final PasswordEncoder passwordEncoder;
    private final SecurityProvider securityProvider;
    private final MailService mailService;
    private final UserMapper userMapper;


    @Value("${api.protocol}")
    private String apiProtocol;

    @Value("${api.host}")
    private String apiHost;

    @Value("${api.path.common.auth}")
    private String apiPath;


    @Value("${code.role.user}")
    private String userRoleCode;

    @Value("${api.path.auth.confirm}")
    private String confirmPath;


    @Autowired
    public AuthService(
            PasswordEncoder passwordEncoder,
            SecurityProvider securityProvider,
            MailService mailService,
            UserMapper userMapper
    ) {
        this.passwordEncoder = passwordEncoder;
        this.securityProvider = securityProvider;
        this.mailService = mailService;
        this.userMapper = userMapper;
    }


    /**
     * Returns user token pair (access and refresh).
     *
     * @return token pair.
     * @throws UnauthorizedAccessException if user authorization is missing.
     */
    public JwtPairDTO login() throws UnauthorizedAccessException {
        return buildJwtPair(securityProvider.login(getCurrentUserEntity()));
    }


    /**
     * Register new user and returns its identifier.
     *
     * @param userDTO the user details.
     * @return new created user identifier.
     * @throws ResourceCannotCreateException if user could not create.
     */
    @Transactional(
            rollbackFor = {ResourceCannotCreateException.class},
            noRollbackFor = {EntityNotFoundException.class, OptimisticLockException.class, DataAccessException.class}
    )
    public Integer register(@NotNull UserDTO userDTO) throws ResourceCannotCreateException {
        String email = userDTO.getEmail();
        UserRepository userRepository = userRepository();
        if (userRepository.isEnabled(email)) {
            throw new ResourceCannotCreateException(msg("sportsportal.common.User.alreadyExistByEmail.message", email));
        } else try {
            // password encoding and user saving
            userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            UserEntity userEntity = userMapper.toEntity(userDTO);
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
     * @param userId        the user identifier.
     * @param confirmOrigin the confirmation link origin.
     * @throws ResourceNotFoundException     if user could not found.
     * @throws ResourceCannotUpdateException if could not sent email.
     */
    @Transactional(
            rollbackFor = {ResourceNotFoundException.class, ResourceCannotUpdateException.class},
            noRollbackFor = {EntityNotFoundException.class}
    )
    public void initConfirmation(Integer userId, String confirmOrigin) throws ResourceNotFoundException, ResourceCannotUpdateException {
        if (!StringUtils.hasText(confirmOrigin)) {
            confirmOrigin = String.format("%s://%s%s", apiProtocol, apiHost, apiPath);
        }

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
     * Confirm user and assign its with user role.
     *
     * @param confirmCode the user's confirmation code.
     * @throws ResourceNotFoundException if user not found by confirm code.
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
            userEntity.getRoles().add(roleRepository().findByCode(userRoleCode));
            userRepository.save(userEntity);
        }
    }

    /**
     * Returns built JWT pair.
     *
     * @param jwtPair the raw JWT pair.
     * @return built JWT pair.
     */
    private JwtPairDTO buildJwtPair(Pair<String, String> jwtPair) {
        JwtPairDTO jwtPairDTO = new JwtPairDTO();
        jwtPairDTO.setAccessToken(jwtPair.getFirst());
        jwtPairDTO.setRefreshToken(jwtPair.getSecond());
        return jwtPairDTO;
    }

    /**
     * Confirmation email sending.
     *
     * @param emailAddress  the sending address.
     * @param confirmOrigin the confirm host.
     * @param confirmCode   the confirm code.
     * @throws MessagingException if could not sent email.
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
