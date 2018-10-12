package ru.vldf.sportsportal.service.generic;

import org.springframework.security.core.context.SecurityContextHolder;
import ru.vldf.sportsportal.config.messages.MessageContainer;
import ru.vldf.sportsportal.domain.sectional.common.RoleEntity;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;
import ru.vldf.sportsportal.repository.common.RoleRepository;
import ru.vldf.sportsportal.repository.common.UserRepository;
import ru.vldf.sportsportal.service.security.userdetails.IdentifiedUserDetails;

import java.util.ArrayList;
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
     * @return user id
     * @throws UnauthorizedAccessException if user is anonymous
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
     * @throws UnauthorizedAccessException if user is anonymous
     */
    public UserEntity getCurrentUserEntity() throws UnauthorizedAccessException {
        return userRepository.getOne(getCurrentUserId());
    }

    /**
     * Check whether the user has the necessary rights.
     *
     * @param roleCode {@link String} role code
     * @return <tt>true</tt> if current user has role by code
     * @throws UnauthorizedAccessException if user is anonymous
     * @throws ResourceNotFoundException   if role code not exist
     */
    public boolean currentUserHasRole(String roleCode) throws UnauthorizedAccessException, ResourceNotFoundException {
        if (!roleRepository.existsByCode(roleCode)) {
            throw new ResourceNotFoundException(mGetAndFormat("sportsportal.common.Role.notExistByCode.message", roleCode));
        }
        return userRepository.existsByIdAndRoleCode(getCurrentUserId(), roleCode);
    }

    /**
     * Returns current user role codes.
     *
     * @return {@link Collection<String>} role entity codes
     * @throws UnauthorizedAccessException if user is anonymous
     */
    public Collection<String> getCurrentRoleCodes() throws UnauthorizedAccessException {
        Collection<RoleEntity> roleEntities = getCurrentRoleEntities();
        Collection<String> roleCodes = new ArrayList<>(roleEntities.size());
        for (RoleEntity roleEntity : roleEntities) roleCodes.add(roleEntity.getCode());
        return roleCodes;
    }

    /**
     * Returns current user roles.
     *
     * @return {@link Collection<RoleEntity>} role entities
     * @throws UnauthorizedAccessException if user is anonymous
     */
    public Collection<RoleEntity> getCurrentRoleEntities() throws UnauthorizedAccessException {
        return getCurrentUserEntity().getRoles();
    }
}
