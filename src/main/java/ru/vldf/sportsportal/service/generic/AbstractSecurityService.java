package ru.vldf.sportsportal.service.generic;

import org.springframework.security.core.context.SecurityContextHolder;
import ru.vldf.sportsportal.config.messages.MessageContainer;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;
import ru.vldf.sportsportal.repository.common.RoleRepository;
import ru.vldf.sportsportal.repository.common.UserRepository;
import ru.vldf.sportsportal.service.security.userdetails.IdentifiedUserDetails;

public abstract class AbstractSecurityService extends AbstractMessageService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    public AbstractSecurityService(MessageContainer messages, UserRepository userRepository, RoleRepository roleRepository) {
        super(messages);
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public UserRepository userRepository() {
        return userRepository;
    }

    public RoleRepository roleRepository() {
        return roleRepository;
    }

    /**
     * Returns authenticated user id.
     *
     * @return user id
     * @throws UnauthorizedAccessException - if user is anonymous
     */
    public Integer getCurrentUserId() throws UnauthorizedAccessException {
        try {
            return ((IdentifiedUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        } catch (ClassCastException | NullPointerException e) {
            throw new UnauthorizedAccessException(mGet("sportsportal.auth.filter.credentialsNotFound.message"), e);
        }
    }

    /**
     * Returns authenticated user.
     *
     * @return user entity
     * @throws UnauthorizedAccessException - if user is anonymous
     */
    public UserEntity getCurrentUserEntity() throws UnauthorizedAccessException {
        return userRepository.getOne(getCurrentUserId());
    }
}
