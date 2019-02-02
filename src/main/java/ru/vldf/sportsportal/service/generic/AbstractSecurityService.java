package ru.vldf.sportsportal.service.generic;

import org.springframework.security.core.context.SecurityContextHolder;
import ru.vldf.sportsportal.config.messages.MessageContainer;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;
import ru.vldf.sportsportal.repository.common.RoleRepository;
import ru.vldf.sportsportal.repository.common.UserRepository;
import ru.vldf.sportsportal.service.security.userdetails.IdentifiedUserDetails;

import java.util.Collection;

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
     * @return {@link Integer} user id
     * @throws UnauthorizedAccessException if user is anonymous
     */
    public Integer getCurrentUserId() throws UnauthorizedAccessException {
        try {
            return ((IdentifiedUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        } catch (ClassCastException | NullPointerException e) {
            throw new UnauthorizedAccessException(msg("sportsportal.auth.filter.credentialsNotFound.message"), e);
        }
    }

    /**
     * Returns authenticated user.
     *
     * @return {@link UserEntity} user entity
     * @throws UnauthorizedAccessException if user is anonymous
     */
    public UserEntity getCurrentUserEntity() throws UnauthorizedAccessException {
        return userRepository.getOne(getCurrentUserId());
    }

    /**
     * Checks if the user is current.
     *
     * @param userEntity {@link UserEntity} checked user
     * @return <tt>true</tt> if successful, false otherwise
     * @throws UnauthorizedAccessException if current user is anonymous
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isCurrentUser(UserEntity userEntity) throws UnauthorizedAccessException {
        return getCurrentUserId().equals(userEntity.getId());
    }

    /**
     * Checks if the current user in collection of users.
     *
     * @param userEntityCollection {@link Collection<UserEntity>} checked collection of user
     * @return <tt>true</tt> if successful, false otherwise
     * @throws UnauthorizedAccessException if current user is anonymous
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isContainCurrentUser(Collection<UserEntity> userEntityCollection) throws UnauthorizedAccessException {
        return userEntityCollection.contains(getCurrentUserEntity());
    }

    /**
     * Check whether the user has the necessary rights.
     *
     * @param code {@link String} role code
     * @return <tt>true</tt> if current user has role by code
     * @throws UnauthorizedAccessException if user is anonymous
     * @throws ResourceNotFoundException   if role code not exist
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean currentUserHasRoleByCode(String code) throws UnauthorizedAccessException, ResourceNotFoundException {
        if (!roleRepository.existsByCode(code)) {
            throw new ResourceNotFoundException(msg("sportsportal.common.Role.notExistByCode.message", code));
        } else return userRepository.hasRoleByCode(getCurrentUserId(), code);
    }
}
