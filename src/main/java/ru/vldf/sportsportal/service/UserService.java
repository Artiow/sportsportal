package ru.vldf.sportsportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vldf.sportsportal.config.messages.MessageContainer;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;
import ru.vldf.sportsportal.dto.sectional.common.shortcut.UserShortDTO;
import ru.vldf.sportsportal.mapper.sectional.common.UserMapper;
import ru.vldf.sportsportal.repository.common.RoleRepository;
import ru.vldf.sportsportal.repository.common.UserRepository;
import ru.vldf.sportsportal.service.generic.AbstractSecurityService;
import ru.vldf.sportsportal.service.generic.ForbiddenAccessException;
import ru.vldf.sportsportal.service.generic.ResourceNotFoundException;
import ru.vldf.sportsportal.service.generic.UnauthorizedAccessException;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;

@Service
public class UserService extends AbstractSecurityService {

    @Value("${code.role.admin}")
    private String adminRoleCode;

    private UserMapper userMapper;


    @Autowired
    public UserService(MessageContainer messages, UserRepository userRepository, RoleRepository roleRepository) {
        super(messages, userRepository, roleRepository);
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
     * @throws UnauthorizedAccessException if authorization is missing
     * @throws ForbiddenAccessException    if user don't have permission to get this user data
     * @throws ResourceNotFoundException   if user with sent id not found
     */
    @Transactional(
            readOnly = true,
            rollbackFor = {UnauthorizedAccessException.class, ForbiddenAccessException.class, ResourceNotFoundException.class},
            noRollbackFor = {EntityNotFoundException.class}
    )
    public UserShortDTO get(@NotNull Integer id) throws UnauthorizedAccessException, ForbiddenAccessException, ResourceNotFoundException {
        try {
            UserEntity userEntity = userRepository().getOne(id);
            if (!currentUserHasRoleByCode(adminRoleCode) && (!isCurrentUser(userEntity))) {
                throw new ForbiddenAccessException(mGet("sportsportal.common.User.forbidden.message"));
            } else {
                return userMapper.toShortDTO(userEntity);
            }
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(mGetAndFormat("sportsportal.common.User.notExistById.message", id), e);
        }
    }

    /**
     * Delete user.
     *
     * @param id {@link Integer} user identifier
     * @throws UnauthorizedAccessException if authorization is missing
     * @throws ForbiddenAccessException    if user don't have permission to delete this user
     * @throws ResourceNotFoundException   if user not found
     */
    @Transactional(
            rollbackFor = {UnauthorizedAccessException.class, ForbiddenAccessException.class, ResourceNotFoundException.class},
            noRollbackFor = {EntityNotFoundException.class}
    )
    public void delete(Integer id) throws UnauthorizedAccessException, ForbiddenAccessException, ResourceNotFoundException {
        try {
            UserRepository userRepository = userRepository();
            UserEntity userEntity = userRepository.getOne(id);
            if (!currentUserHasRoleByCode(adminRoleCode) && (!isCurrentUser(userEntity))) {
                throw new ForbiddenAccessException(mGet("sportsportal.common.User.forbidden.message"));
            } else {
                userRepository.delete(userEntity);
            }
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(mGetAndFormat("sportsportal.common.User.notExistById.message", id), e);
        }
    }
}
