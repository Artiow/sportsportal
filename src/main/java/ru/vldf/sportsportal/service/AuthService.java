package ru.vldf.sportsportal.service;

import io.jsonwebtoken.JwtException;
import org.postgresql.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
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
import ru.vldf.sportsportal.service.generic.*;
import ru.vldf.sportsportal.service.security.SecurityService;
import ru.vldf.sportsportal.service.security.userdetails.IdentifiedUserDetails;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class AuthService extends AbstractSecurityService {

    @Value("${code.role.user}")
    private String userRoleCode;

    @Value("${api.path.auth.confirm}")
    private String confirmPath;

    private BCryptPasswordEncoder passwordEncoder;
    private SecurityService securityService;
    private JavaMailSender javaMailSender;
    private LoginMapper loginMapper;
    private UserMapper userMapper;


    @Autowired
    public AuthService(MessageContainer messages, UserRepository userRepository, RoleRepository roleRepository) {
        super(messages, userRepository, roleRepository);
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
    public void setJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
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
     * Logging user and returns his token.
     *
     * @param login    {@link String} users login
     * @param password {@link String} users password
     * @return {@link TokenDTO} token info
     * @throws UsernameNotFoundException if user not found
     * @throws JwtException              if could not parse jwt
     */
    @Transactional(
            readOnly = true,
            rollbackFor = {UsernameNotFoundException.class, JwtException.class},
            noRollbackFor = {EntityNotFoundException.class, BadCredentialsException.class}
    )
    public TokenDTO login(@NotNull String login, @NotNull String password) throws UsernameNotFoundException, JwtException {
        UserEntity user;
        try {
            user = userRepository().findByLogin(login);
            if (user == null) {
                throw new EntityNotFoundException(mGet("sportsportal.auth.service.userRepository.message"));
            } else if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new BadCredentialsException(mGet("sportsportal.auth.service.passwordEncoder.message"));
            }
        } catch (EntityNotFoundException | BadCredentialsException e) {
            throw new UsernameNotFoundException(mGet("sportsportal.auth.service.loginError.message"), e);
        }
        return new TokenDTO()
                .setLogin(loginMapper.toLoginDTO(user))
                .setTokenType(securityService.getTokenType())
                .setTokenHash(securityService.login(loginMapper.toIdentifiedUser(user)));
    }

    /**
     * Verify user and returns his token.
     *
     * @param accessToken {@link String} access token
     * @return {@link TokenDTO} token info
     * @throws UsernameNotFoundException  if user not found
     * @throws ResourceNotFoundException  if user not found
     * @throws SentDataCorruptedException if token not valid
     * @throws JwtException               if could not parse jwt
     */
    @Transactional(
            readOnly = true,
            rollbackFor = {UsernameNotFoundException.class, ResourceNotFoundException.class, SentDataCorruptedException.class, JwtException.class},
            noRollbackFor = {EntityNotFoundException.class, BadCredentialsException.class}
    )
    public TokenDTO verify(String accessToken) throws UsernameNotFoundException, ResourceNotFoundException, SentDataCorruptedException, JwtException {
        final String tokenType = securityService.getTokenType();
        try {
            if ((accessToken == null) || (!accessToken.startsWith(tokenType))) {
                throw new BadCredentialsException(String.format("Sent token null or not starts with \'%s\'", tokenType));
            }
            IdentifiedUserDetails userDetails = securityService.authentication(accessToken.substring(tokenType.length()).trim());
            UserEntity user = userRepository().getOne(userDetails.getId());
            if ((!user.getLogin().equals(userDetails.getUsername())) || (!user.getPassword().equals(userDetails.getPassword()))) {
                throw new UsernameNotFoundException(mGet("sportsportal.auth.service.loginError.message"));
            }
            return new TokenDTO()
                    .setLogin(loginMapper.toLoginDTO(user))
                    .setTokenType(securityService.getTokenType())
                    .setTokenHash(securityService.login(loginMapper.toIdentifiedUser(user)));
        } catch (BadCredentialsException e) {
            throw new SentDataCorruptedException(mGet("sportsportal.auth.filter.credentialsNotValid.message"), e);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(mGet("sportsportal.auth.service.userRepository.message"), e);
        }
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
            noRollbackFor = {JpaObjectRetrievalFailureException.class}
    )
    public Integer register(@NotNull UserDTO userDTO) throws ResourceCannotCreateException {
        String login = userDTO.getLogin();
        UserRepository userRepository = userRepository();
        if (userRepository.existsByLogin(login)) {
            throw new ResourceCannotCreateException(mGetAndFormat("sportsportal.common.User.alreadyExistByLogin.message", login));
        }
        try {
            UserEntity userEntity = userMapper.toEntity(userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword())));
            userEntity.setRoles(new ArrayList<>());
            return userRepository.save(userEntity).getId();
        } catch (JpaObjectRetrievalFailureException e) {
            throw new ResourceCannotCreateException(mGet("sportsportal.common.User.cannotCreate.message"), e);
        }
    }

    /**
     * Init confirmation for user.
     *
     * @param userId      {@link Integer} user identifier
     * @param confirmRoot {@link String} confirmation link root
     * @throws ResourceNotFoundException     if user could not found
     * @throws ResourceCannotUpdateException if could not sent email
     */
    @Transactional(
            rollbackFor = {ResourceNotFoundException.class, ResourceCannotUpdateException.class},
            noRollbackFor = {EntityNotFoundException.class}
    )
    public void initConfirmation(Integer userId, String confirmRoot) throws ResourceNotFoundException, ResourceCannotUpdateException {
        String confirmCode = Base64.encodeBytes(UUID.randomUUID().toString().getBytes());
        UserRepository userRepository = userRepository();
        try {
            UserEntity userEntity = userRepository.getOne(userId);
            if (userEntity.getRoles().isEmpty()) {
                userEntity.setConfirmCode(confirmCode);
                sendEmail(userEntity.getEmail(), confirmRoot, confirmCode);
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
     * @param emailAddress {@link String} sending address
     * @param confirmRoot  {@link String} confirm root
     * @param confirmCode  {@link String} confirm code
     * @throws MessagingException if could not sent email
     */
    private void sendEmail(String emailAddress, String confirmRoot, String confirmCode) throws MessagingException {
        MimeMessage mailMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage);
        messageHelper.setTo(emailAddress);
        messageHelper.setSubject(mGet("sportsportal.email.confirm.subject"));
        messageHelper.setText(String.format(
                "<p>%s</p>",
                mGetAndFormat(
                        "sportsportal.email.confirm.text.env",
                        String.format(
                                "<a href=\"%s\">%s</a>",
                                String.format(confirmPath, confirmRoot, confirmCode),
                                mGet("sportsportal.email.confirm.text.link")
                        )
                )
        ), true);
        javaMailSender.send(mailMessage);
    }
}
