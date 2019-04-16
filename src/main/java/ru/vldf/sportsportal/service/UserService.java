package ru.vldf.sportsportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;
import ru.vldf.sportsportal.dto.sectional.common.shortcut.UserShortDTO;
import ru.vldf.sportsportal.mapper.sectional.common.UserMapper;
import ru.vldf.sportsportal.repository.common.UserRepository;
import ru.vldf.sportsportal.service.general.AbstractSecurityService;
import ru.vldf.sportsportal.service.general.throwable.ForbiddenAccessException;
import ru.vldf.sportsportal.service.general.throwable.ResourceNotFoundException;
import ru.vldf.sportsportal.service.general.throwable.UnauthorizedAccessException;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;

/**
 * @author Namednev Artem
 */
@Service
public class UserService extends AbstractSecurityService {

    private final UserMapper userMapper;


    @Autowired
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }


    /**
     * Returns users short data by user identifier.
     *
     * @param id the user identifier.
     * @return users short data.
     * @throws UnauthorizedAccessException if authorization is missing.
     * @throws ForbiddenAccessException    if user don't have permission to get this user data.
     * @throws ResourceNotFoundException   if user with sent id not found.
     */
    @Transactional(
            readOnly = true,
            rollbackFor = {UnauthorizedAccessException.class, ForbiddenAccessException.class, ResourceNotFoundException.class},
            noRollbackFor = {EntityNotFoundException.class}
    )
    public UserShortDTO get(@NotNull Integer id) throws UnauthorizedAccessException, ForbiddenAccessException, ResourceNotFoundException {
        try {
            UserEntity userEntity = userRepository().getOne(id);
            if ((!currentUserIsAdmin()) && (!isCurrentUser(userEntity))) {
                throw new ForbiddenAccessException(msg("sportsportal.common.User.forbidden.message"));
            } else {
                return userMapper.toShortDTO(userEntity);
            }
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(msg("sportsportal.common.User.notExistById.message", id), e);
        }
    }

    /**
     * Delete user by user identifier.
     *
     * @param id the user identifier.
     * @throws UnauthorizedAccessException if authorization is missing.
     * @throws ForbiddenAccessException    if user don't have permission to delete this user.
     * @throws ResourceNotFoundException   if user not found.
     */
    @Transactional(
            rollbackFor = {UnauthorizedAccessException.class, ForbiddenAccessException.class, ResourceNotFoundException.class},
            noRollbackFor = {EntityNotFoundException.class}
    )
    public void delete(Integer id) throws UnauthorizedAccessException, ForbiddenAccessException, ResourceNotFoundException {
        try {
            UserRepository userRepository = userRepository();
            UserEntity userEntity = userRepository.getOne(id);
            if ((!currentUserIsAdmin()) && (!isCurrentUser(userEntity))) {
                throw new ForbiddenAccessException(msg("sportsportal.common.User.forbidden.message"));
            } else {
                userRepository.delete(userEntity);
            }
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(msg("sportsportal.common.User.notExistById.message", id), e);
        }
    }
}
