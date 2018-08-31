package ru.vldf.sportsportal.service.generic;

import org.springframework.security.core.context.SecurityContextHolder;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;
import ru.vldf.sportsportal.repository.common.RoleRepository;
import ru.vldf.sportsportal.repository.common.UserRepository;
import ru.vldf.sportsportal.service.security.userdetails.IdentifiedUserDetails;

public abstract class AbstractSecurityService extends AbstractMessageService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;


    protected UserRepository getUserRepository() {
        return userRepository;
    }

    protected void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    protected RoleRepository getRoleRepository() {
        return roleRepository;
    }

    protected void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    /**
     * Returns authenticated user id.
     *
     * @return user id
     * @throws AuthorizationRequiredException - if user is anonymous
     */
    protected Integer getCurrentUserId() throws AuthorizationRequiredException {
        try {
            return ((IdentifiedUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        } catch (ClassCastException | NullPointerException e) {
            throw new AuthorizationRequiredException(mGet("sportsportal.auth.filter.credentialsNotFound.message"), e);
        }
    }

    /**
     * Returns authenticated user.
     *
     * @return user entity
     * @throws AuthorizationRequiredException - if user is anonymous
     */
    protected UserEntity getCurrentUserEntity() throws AuthorizationRequiredException {
        return userRepository.getOne(getCurrentUserId());
    }
}
