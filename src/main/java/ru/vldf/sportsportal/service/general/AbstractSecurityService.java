package ru.vldf.sportsportal.service.general;

import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.vldf.sportsportal.domain.general.AbstractAuditableEntity;
import ru.vldf.sportsportal.domain.sectional.common.RoleEntity;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;
import ru.vldf.sportsportal.repository.common.RoleRepository;
import ru.vldf.sportsportal.repository.common.UserRepository;
import ru.vldf.sportsportal.service.general.throwable.UnauthorizedAccessException;
import ru.vldf.sportsportal.service.security.userdetails.model.IdentifiedUserDetails;
import ru.vldf.sportsportal.util.SimpleGrantedAuthorityBuilder;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * @author Namednev Artem
 */
@SuppressWarnings({"BooleanMethodIsAlwaysInverted", "unused"})
public abstract class AbstractSecurityService extends AbstractMessageService {

    private Map<String, GrantedAuthority> authorities = Collections.emptyMap();


    @Value("${code.role.admin}")
    private String adminRoleCode;

    @Value("${code.role.user}")
    private String userRoleCode;


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    @PostConstruct
    private void init() {
        this.authorities = ImmutableMap.of(
                adminRoleCode, SimpleGrantedAuthorityBuilder.of(adminRoleCode),
                userRoleCode, SimpleGrantedAuthorityBuilder.of(userRoleCode)
        );
    }


    protected UserRepository userRepository() {
        return userRepository;
    }

    protected RoleRepository roleRepository() {
        return roleRepository;
    }


    protected RoleEntity adminRole() {
        // noinspection OptionalGetWithoutIsPresent
        return roleRepository.findByCode(adminRoleCode).get();
    }

    protected RoleEntity userRole() {
        // noinspection OptionalGetWithoutIsPresent
        return roleRepository.findByCode(userRoleCode).get();
    }


    /**
     * Returns user details by authenticated user.
     *
     * @return user details.
     * @throws UnauthorizedAccessException if user is anonymous (unauthorized).
     */
    protected IdentifiedUserDetails getCurrentUserDetails() throws UnauthorizedAccessException {
        try {
            return (IdentifiedUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (ClassCastException | NullPointerException e) {
            throw new UnauthorizedAccessException(msg("sportsportal.auth.filter.credentialsNotFound.message"), e);
        }
    }

    /**
     * Returns user entity by authenticated user.
     *
     * @return user entity.
     * @throws UnauthorizedAccessException if user is anonymous (unauthorized).
     */
    protected UserEntity getCurrentUserEntity() throws UnauthorizedAccessException {
        // noinspection OptionalGetWithoutIsPresent
        return userRepository.findById(getCurrentUserDetails().getId()).get();
    }


    /**
     * Checks if the user is current.
     *
     * @param userEntity the checked user.
     * @return {@literal true} if successful, {@literal false} otherwise.
     * @throws UnauthorizedAccessException if current user is anonymous (unauthorized).
     */
    protected boolean isCurrentUser(UserEntity userEntity) throws UnauthorizedAccessException {
        return Objects.equals(getCurrentUserEntity(), userEntity);
    }

    /**
     * Checks if the current user in collection of users.
     *
     * @param userEntityCollection the collection of checked users.
     * @return {@literal true} if successful, {@literal false} otherwise.
     * @throws UnauthorizedAccessException if current user is anonymous.
     */
    protected boolean currentUserIn(Collection<UserEntity> userEntityCollection) throws UnauthorizedAccessException {
        return userEntityCollection.contains(getCurrentUserEntity());
    }


    /**
     * Checks if the current user has admin rights.
     *
     * @return {@literal true} if successful, {@literal false} otherwise.
     * @throws UnauthorizedAccessException if current user is anonymous.
     */
    protected boolean currentUserIsAdmin() throws UnauthorizedAccessException {
        return currentUserIs(adminRoleCode);
    }

    /**
     * Checks if the current user has user rights.
     *
     * @return {@literal true} if successful, {@literal false} otherwise.
     * @throws UnauthorizedAccessException if current user is anonymous.
     */
    protected boolean currentUserIsUser() throws UnauthorizedAccessException {
        return currentUserIs(userRoleCode);
    }

    private boolean currentUserIs(String roleKey) throws UnauthorizedAccessException {
        return getCurrentUserDetails().getAuthorities().contains(authorities.get(roleKey));
    }


    /**
     * Set creator details in auditable entity.
     *
     * @param entity the auditable entity.
     * @param <T>    the auditable entity type.
     * @return created auditable entity.
     * @throws UnauthorizedAccessException if current user is anonymous.
     */
    protected <T extends AbstractAuditableEntity> T createdByCurrentUser(T entity) throws UnauthorizedAccessException {
        UserEntity creator = getCurrentUserEntity();
        Timestamp now = Timestamp.from(Instant.now());
        entity.setCreator(creator);
        entity.setCreatedAt(now);
        entity.setUpdater(creator);
        entity.setUpdatedAt(now);
        return entity;
    }

    /**
     * Set updater details in auditable entity.
     *
     * @param entity the auditable entity.
     * @param <T>    the auditable entity type.
     * @return created auditable entity.
     * @throws UnauthorizedAccessException if current user is anonymous.
     */
    protected <T extends AbstractAuditableEntity> T updatedByCurrentUser(T entity) throws UnauthorizedAccessException {
        entity.setUpdater(getCurrentUserEntity());
        entity.setUpdatedAt(Timestamp.from(Instant.now()));
        return entity;
    }
}
