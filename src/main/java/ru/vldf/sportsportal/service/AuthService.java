package ru.vldf.sportsportal.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.postgresql.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;
import ru.vldf.sportsportal.dto.sectional.common.UserDTO;
import ru.vldf.sportsportal.dto.sectional.common.specialized.PasswordHolderDTO;
import ru.vldf.sportsportal.dto.security.JwtPairDTO;
import ru.vldf.sportsportal.integration.mail.MailService;
import ru.vldf.sportsportal.mapper.sectional.common.UserMapper;
import ru.vldf.sportsportal.repository.common.UserRepository;
import ru.vldf.sportsportal.service.general.AbstractSecurityService;
import ru.vldf.sportsportal.service.general.throwable.ResourceCannotCreateException;
import ru.vldf.sportsportal.service.general.throwable.ResourceCannotUpdateException;
import ru.vldf.sportsportal.service.general.throwable.ResourceNotFoundException;
import ru.vldf.sportsportal.service.general.throwable.UnauthorizedAccessException;
import ru.vldf.sportsportal.service.security.SecurityProvider;

import javax.mail.MessagingException;
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
        UserEntity userEntity = getCurrentUserEntity();
        // confirm code clearing
        if (userEntity.getConfirmCode() != null) {
            userEntity.setConfirmCode(null);
            userRepository().save(userEntity);
        }
        return buildJwtPair(securityProvider.generate(userEntity.getId()));
    }


    /**
     * Register new user and returns its identifier.
     *
     * @param userDTO the user details.
     * @return new created user identifier.
     * @throws ResourceCannotCreateException if user could not create.
     */
    @Transactional(
            rollbackFor = {ResourceCannotCreateException.class}
    )
    public Integer register(UserDTO userDTO) throws ResourceCannotCreateException {
        UserRepository userRepository = userRepository();
        if (userRepository.existsByEmailAndIsDisabledIsFalse(userDTO.getEmail())) {
            throw new ResourceCannotCreateException(msg("sportsportal.common.User.alreadyExistByEmail.message", userDTO.getEmail()));
        } else {
            // password encoding and user saving
            UserEntity userEntity = userMapper.toEntity(userDTO);
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            if (userRepository.existsByEmail(userEntity.getEmail())) {
                var existing = userRepository.findByEmail(userEntity.getEmail());
                userEntity.setVersion(existing.getVersion()); // version synchronization
                userEntity = userMapper.merge(existing, userEntity);
            }
            return userRepository.save(userEntity).getId();
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
            noRollbackFor = {MessagingException.class}
    )
    public void initConfirmation(Integer userId, String confirmOrigin) throws ResourceNotFoundException, ResourceCannotUpdateException {
        try {
            String confirmCode = Base64.encodeBytes(UUID.randomUUID().toString().getBytes());
            UserEntity userEntity = findUserById(userId);
            if (userEntity.getRoles().isEmpty()) {
                userEntity.setConfirmCode(confirmCode);
                sendUserConfirmationEmail(userEntity.getEmail(), confirmOrigin, confirmCode);
                userRepository().save(userEntity);
            } else {
                throw new ResourceCannotUpdateException(msg("sportsportal.common.User.alreadyConfirmed.message"));
            }
        } catch (MessagingException e) {
            throw new ResourceCannotUpdateException(msg("sportsportal.mail.cannotSendEmail.message"), e);
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
            userEntity.getRoles().add(userRole());
            userRepository.save(userEntity);
        }
    }

    /**
     * Init password recovery for user.
     *
     * @param userId        the user identifier.
     * @param confirmOrigin the confirmation link origin.
     * @throws ResourceNotFoundException     if user could not found.
     * @throws ResourceCannotUpdateException if could not sent email.
     */
    @Transactional(
            rollbackFor = {ResourceNotFoundException.class, ResourceCannotUpdateException.class},
            noRollbackFor = {MessagingException.class}
    )
    public void initRecovery(int userId, String confirmOrigin) throws ResourceNotFoundException, ResourceCannotUpdateException {

    }

    /**
     * Recover user password.
     *
     * @param confirmCode the user's confirmation code.
     * @throws ResourceNotFoundException if user not found by confirm code.
     */
    @Transactional(
            rollbackFor = {ResourceNotFoundException.class}
    )
    public void recover(String confirmCode, PasswordHolderDTO passwordHolderDTO) throws ResourceNotFoundException {

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
     * User confirmation email sending.
     *
     * @param emailAddress  the sending address.
     * @param confirmOrigin the confirm host.
     * @param confirmCode   the confirm code.
     * @throws MessagingException if could not sent email.
     */
    private void sendUserConfirmationEmail(
            String emailAddress, String confirmOrigin, String confirmCode
    ) throws MessagingException {
        sendEmail(emailAddress, confirmOrigin, confirmCode, EmailConfirmationType.USER_CONFIRMATION);
    }

    /**
     * Password recovery email sending.
     *
     * @param emailAddress  the sending address.
     * @param confirmOrigin the confirm host.
     * @param confirmCode   the confirm code.
     * @throws MessagingException if could not sent email.
     */
    private void sendPasswordRecoveryEmail(
            String emailAddress, String confirmOrigin, String confirmCode
    ) throws MessagingException {
        sendEmail(emailAddress, confirmOrigin, confirmCode, EmailConfirmationType.PASSWORD_RECOVERY);
    }

    private void sendEmail(
            String emailAddress, String confirmOrigin, String confirmCode, EmailConfirmationType confirmationType
    ) throws MessagingException {
        mailService.sender()
                .setTo(emailAddress)
                .setFrom(msg(confirmationType.getFrom()), msg(confirmationType.getPersonal()))
                .setSubject(msg(confirmationType.getSubject()))
                .setHtml(
                        String.format(
                                "<p>%s</p>",
                                msg(
                                        confirmationType.getTextEnv(),
                                        String.format(
                                                "<a href=\"%s\">%s</a>",
                                                String.format(confirmPath, confirmOrigin, confirmCode),
                                                msg(confirmationType.getTextLink())
                                        )
                                )
                        )
                ).send();
    }


    @Getter
    @RequiredArgsConstructor
    private enum EmailConfirmationType {

        USER_CONFIRMATION(
                "sportsportal.email.confirm.from",
                "sportsportal.email.confirm.personal",
                "sportsportal.email.confirm.subject",
                "sportsportal.email.confirm.text.env",
                "sportsportal.email.confirm.text.link"
        ),

        PASSWORD_RECOVERY(
                "sportsportal.email.recover.from",
                "sportsportal.email.recover.personal",
                "sportsportal.email.recover.subject",
                "sportsportal.email.recover.text.env",
                "sportsportal.email.recover.text.link"
        );

        private final String from;
        private final String personal;
        private final String subject;
        private final String textEnv;
        private final String textLink;
    }
}
