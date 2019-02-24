package ru.vldf.sportsportal.service.generic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;
import ru.vldf.sportsportal.repository.common.RoleRepository;
import ru.vldf.sportsportal.repository.common.UserRepository;
import ru.vldf.sportsportal.service.security.userdetails.IdentifiedUserDetails;

import java.util.Collection;

/**
 * @author Namednev Artem
 */
public abstract class AbstractSecurityService extends AbstractMessageService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    protected UserRepository userRepository() {
        return userRepository;
    }

    protected RoleRepository roleRepository() {
        return roleRepository;
    }


    /**
     * Returns authenticated user identifier.
     *
     * @return user identifier.
     * @throws UnauthorizedAccessException if user is anonymous.
     */
    protected Integer getCurrentUserId() throws UnauthorizedAccessException {
        try {
            return userDetails().getId();
        } catch (ClassCastException | NullPointerException e) {
            throw new UnauthorizedAccessException(msg("sportsportal.auth.filter.credentialsNotFound.message"), e);
        }
    }

    /**
     * Returns authenticated user email.
     *
     * @return user email.
     * @throws UnauthorizedAccessException if user is anonymous.
     */
    protected String getCurrentUserEmail() throws UnauthorizedAccessException {
        try {
            return userDetails().getUsername();
        } catch (ClassCastException | NullPointerException e) {
            throw new UnauthorizedAccessException(msg("sportsportal.auth.filter.credentialsNotFound.message"), e);
        }
    }

    /**
     * Extract authenticated user user details from security context.
     */
    private IdentifiedUserDetails userDetails() throws ClassCastException, NullPointerException {
        return (IdentifiedUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }


    /**
     * Returns authenticated user entity.
     *
     * @return user entity.
     * @throws UnauthorizedAccessException if user is anonymous.
     */
    protected UserEntity getCurrentUserEntity() throws UnauthorizedAccessException {
        return userRepository.getOne(getCurrentUserId());
    }


    /**
     * Checks if the user is current.
     *
     * @param userEntity the checked user.
     * @return {@literal true} if successful, {@literal false} otherwise.
     * @throws UnauthorizedAccessException if current user is anonymous.
     */
    protected boolean isCurrentUser(UserEntity userEntity) throws UnauthorizedAccessException {
        return getCurrentUserId().equals(userEntity.getId());
    }

    /**
     * Checks if the current user in collection of users.
     *
     * @param userEntityCollection the collection of checked users.
     * @return {@literal true} if successful, {@literal false} otherwise.
     * @throws UnauthorizedAccessException if current user is anonymous.
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    protected boolean isContainCurrentUser(Collection<UserEntity> userEntityCollection) throws UnauthorizedAccessException {
        return userEntityCollection.contains(getCurrentUserEntity());
    }


    /**
     * Check whether the user has the necessary rights.
     *
     * @param code the role code.
     * @return {@literal true} if current user has role by code, {@literal false} otherwise.
     * @throws UnauthorizedAccessException if user is anonymous.
     * @throws ResourceNotFoundException   if role code not exist.
     */
    protected boolean currentUserHasRoleByCode(String code) throws UnauthorizedAccessException, ResourceNotFoundException {
        if (roleRepository.existsByCode(code)) {
            return userRepository.hasRoleByCode(getCurrentUserEmail(), code);
        } else {
            throw new ResourceNotFoundException(msg("sportsportal.common.Role.notExistByCode.message", code));
        }
    }
}
