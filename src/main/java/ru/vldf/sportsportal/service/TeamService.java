package ru.vldf.sportsportal.service;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;
import ru.vldf.sportsportal.domain.sectional.tournament.TeamEntity;
import ru.vldf.sportsportal.dto.sectional.tournament.TeamDTO;
import ru.vldf.sportsportal.mapper.sectional.tournament.TeamMapper;
import ru.vldf.sportsportal.repository.tournament.TeamRepository;
import ru.vldf.sportsportal.service.generic.*;
import ru.vldf.sportsportal.util.ValidationExceptionBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Namednev Artem
 */
@Service
public class TeamService extends AbstractSecurityService implements CRUDService<TeamEntity, TeamDTO> {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;


    @Autowired
    public TeamService(TeamRepository teamRepository, TeamMapper teamMapper) {
        this.teamRepository = teamRepository;
        this.teamMapper = teamMapper;
    }


    /**
     * Returns requested team by team identifier.
     *
     * @param id the team identifier.
     * @return requested team data.
     * @throws ResourceNotFoundException if team not found.
     */
    @Override
    @Transactional(readOnly = true, rollbackFor = {ResourceNotFoundException.class})
    public TeamDTO get(Integer id) throws ResourceNotFoundException {
        return teamRepository.findById(id).map(teamMapper::toDTO).orElseThrow(ResourceNotFoundException.supplier(msg("sportsportal.tournament.Team.notExistById.message", id)));
    }

    /**
     * Create and save new team and returns its identifier.
     *
     * @param teamDTO the new team details.
     * @return created team identifier.
     * @throws UnauthorizedAccessException     if authorization is missing.
     * @throws MethodArgumentNotValidException if method argument not valid.
     */
    @Override
    @Transactional(rollbackFor = {UnauthorizedAccessException.class, MethodArgumentNotValidException.class})
    public Integer create(TeamDTO teamDTO) throws UnauthorizedAccessException, MethodArgumentNotValidException {
        createCheck(teamDTO);
        TeamEntity teamEntity = teamMapper.toEntity(teamDTO);
        if (teamEntity.getMainCaptain() == null) {
            UserEntity currentUser = getCurrentUserEntity();
            teamEntity.setMainCaptain(currentUser);
            teamEntity.setViceCaptain(currentUser);
        }
        return teamRepository.save(teamEntity).getId();
    }

    /**
     * Update and save team details by team identifier.
     *
     * @param id      the team identifier.
     * @param teamDTO the team new details.
     * @throws UnauthorizedAccessException     if authorization is missing.
     * @throws ForbiddenAccessException        if user don't have permission to update this team details.
     * @throws MethodArgumentNotValidException if method argument not valid.
     * @throws ResourceNotFoundException       if team not found.
     */
    @Override
    @Transactional(rollbackFor = {UnauthorizedAccessException.class, ForbiddenAccessException.class, MethodArgumentNotValidException.class, ResourceNotFoundException.class})
    public void update(Integer id, TeamDTO teamDTO) throws UnauthorizedAccessException, ForbiddenAccessException, MethodArgumentNotValidException, ResourceNotFoundException {
        updateCheck(teamDTO);
        TeamEntity teamEntity = teamRepository.findById(id).orElseThrow(ResourceNotFoundException.supplier(msg("sportsportal.tournament.Team.notExistById.message", id)));
        if (!currentUserIsAdmin() && !(isCurrentUser(teamEntity.getMainCaptain()) || isCurrentUser(teamEntity.getViceCaptain()))) {
            throw new ForbiddenAccessException(msg("sportsportal.tournament.Team.forbidden.message"));
        }
        teamEntity = teamMapper.mergeToEntity(teamEntity, teamDTO);
        if (teamEntity.getMainCaptain() == null) {
            UserEntity currentUser = getCurrentUserEntity();
            teamEntity.setMainCaptain(currentUser);
            teamEntity.setViceCaptain(currentUser);
        }
        teamRepository.save(teamEntity);
    }

    @Override
    public void delete(Integer id) {
        throw new UnsupportedOperationException();
    }


    private void createCheck(TeamDTO teamDTO) throws UnauthorizedAccessException, MethodArgumentNotValidException {
        boolean currentUserIsAdmin = currentUserIsAdmin();
        Map<String, String> errors = new HashMap<>();
        if (teamRepository.existsByName(teamDTO.getName())) {
            errors.put("name", msg("sportsportal.tournament.Team.validation.alreadyExistByName.message", teamDTO.getName()));
        }
        if (!currentUserIsAdmin && (teamDTO.getIsLocked() != null)) {
            errors.put("isLocked", msg("sportsportal.tournament.Team.validation.forbiddenIsLocked.message"));
        }
        if (!currentUserIsAdmin && (teamDTO.getIsDisabled() != null)) {
            errors.put("isDisabled", msg("sportsportal.tournament.Team.validation.forbiddenIsDisabled.message"));
        }
        if (!errors.isEmpty()) {
            validationExceptionFor("create", 0, teamDTO, errors);
        }
    }

    private void updateCheck(TeamDTO teamDTO) throws UnauthorizedAccessException, MethodArgumentNotValidException {
        boolean currentUserIsAdmin = currentUserIsAdmin();
        Map<String, String> errors = new HashMap<>();
        if (teamRepository.existsByNameAndIdNot(teamDTO.getName(), teamDTO.getId())) {
            errors.put("name", msg("sportsportal.tournament.Team.validation.alreadyExistByName.message", teamDTO.getName()));
        }
        if (!currentUserIsAdmin && (teamDTO.getIsLocked() != null)) {
            errors.put("isLocked", msg("sportsportal.tournament.Team.validation.forbiddenIsLocked.message"));
        }
        if (!currentUserIsAdmin && (teamDTO.getIsDisabled() != null)) {
            errors.put("isDisabled", msg("sportsportal.tournament.Team.validation.forbiddenIsDisabled.message"));
        }
        if (!errors.isEmpty()) {
            validationExceptionFor("update", 1, teamDTO, errors);
        }
    }


    @SneakyThrows({NoSuchMethodException.class})
    private void validationExceptionFor(
            String methodName, int parameterIndex, TeamDTO target, Map<String, String> errors
    ) throws MethodArgumentNotValidException {
        throw ValidationExceptionBuilder.buildFor(methodParameter(methodName, parameterIndex), target, errors);
    }

    private MethodParameter methodParameter(String methodName, int parameterIndex) throws NoSuchMethodException {
        return new MethodParameter(getClass().getMethod(methodName, TeamDTO.class), parameterIndex);
    }
}
