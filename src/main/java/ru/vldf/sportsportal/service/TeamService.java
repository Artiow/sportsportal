package ru.vldf.sportsportal.service;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;
import ru.vldf.sportsportal.domain.sectional.tournament.TeamEntity;
import ru.vldf.sportsportal.dto.sectional.tournament.TeamDTO;
import ru.vldf.sportsportal.mapper.sectional.tournament.TeamMapper;
import ru.vldf.sportsportal.repository.tournament.TeamRepository;
import ru.vldf.sportsportal.service.generic.*;

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
     * @throws UnauthorizedAccessException          if authorization is missing.
     * @throws MethodArgumentNotAcceptableException if method argument not acceptable.
     */
    @Override
    @Transactional(rollbackFor = {UnauthorizedAccessException.class, MethodArgumentNotAcceptableException.class})
    public Integer create(TeamDTO teamDTO) throws UnauthorizedAccessException, MethodArgumentNotAcceptableException {
        createCheck(teamDTO);
        TeamEntity teamEntity = teamMapper.toEntity(teamDTO);
        normalizeCaptains(teamEntity);
        return teamRepository.save(teamEntity).getId();
    }

    /**
     * Update and save team details by team identifier.
     *
     * @param id      the team identifier.
     * @param teamDTO the team new details.
     * @throws UnauthorizedAccessException          if authorization is missing.
     * @throws ForbiddenAccessException             if user don't have permission to update this team details.
     * @throws MethodArgumentNotAcceptableException if method argument not acceptable.
     * @throws ResourceNotFoundException            if team not found.
     */
    @Override
    @Transactional(rollbackFor = {UnauthorizedAccessException.class, ForbiddenAccessException.class, MethodArgumentNotAcceptableException.class, ResourceNotFoundException.class})
    public void update(Integer id, TeamDTO teamDTO) throws UnauthorizedAccessException, ForbiddenAccessException, MethodArgumentNotAcceptableException, ResourceNotFoundException {
        updateCheck(teamDTO);
        TeamEntity teamEntity = teamRepository.findById(id).orElseThrow(ResourceNotFoundException.supplier(msg("sportsportal.tournament.Team.notExistById.message", id)));
        rightsCheck(teamEntity);
        teamEntity = teamMapper.inject(teamEntity, teamDTO);
        normalizeCaptains(teamEntity);
        teamRepository.save(teamEntity);
    }

    @Override
    public void delete(Integer id) {
        throw new UnsupportedOperationException();
    }


    private void createCheck(TeamDTO teamDTO) throws UnauthorizedAccessException, MethodArgumentNotAcceptableException {
        Map<String, String> errors = new HashMap<>();
        boolean currentUserIsAdmin = currentUserIsAdmin();
        if (!currentUserIsAdmin && (teamDTO.getIsLocked() != null)) {
            errors.put("isLocked", msg("sportsportal.tournament.Team.validation.forbiddenIsLocked.message"));
        }
        if (!currentUserIsAdmin && (teamDTO.getIsDisabled() != null)) {
            errors.put("isDisabled", msg("sportsportal.tournament.Team.validation.forbiddenIsDisabled.message"));
        }
        if (teamRepository.existsByName(teamDTO.getName())) {
            errors.put("name", msg("sportsportal.tournament.Team.validation.alreadyExistByName.message", teamDTO.getName()));
        }
        if (!errors.isEmpty()) {
            validationExceptionFor("create", 0, teamDTO, errors);
        }
    }

    private void updateCheck(TeamDTO teamDTO) throws UnauthorizedAccessException, MethodArgumentNotAcceptableException {
        boolean currentUserIsAdmin = currentUserIsAdmin();
        Map<String, String> errors = new HashMap<>();
        if (!currentUserIsAdmin && (teamDTO.getIsLocked() != null)) {
            errors.put("isLocked", msg("sportsportal.tournament.Team.validation.forbiddenIsLocked.message"));
        }
        if (!currentUserIsAdmin && (teamDTO.getIsDisabled() != null)) {
            errors.put("isDisabled", msg("sportsportal.tournament.Team.validation.forbiddenIsDisabled.message"));
        }
        if (teamRepository.existsByNameAndIdNot(teamDTO.getName(), teamDTO.getId())) {
            errors.put("name", msg("sportsportal.tournament.Team.validation.alreadyExistByName.message", teamDTO.getName()));
        }
        if (!errors.isEmpty()) {
            validationExceptionFor("update", 1, teamDTO, errors);
        }
    }

    private void rightsCheck(TeamEntity teamEntity) throws UnauthorizedAccessException, ForbiddenAccessException {
        if (!currentUserIsAdmin() && !(isCurrentUser(teamEntity.getMainCaptain()) || isCurrentUser(teamEntity.getViceCaptain()))) {
            throw new ForbiddenAccessException(msg("sportsportal.tournament.Team.forbidden.message"));
        }
    }

    private void normalizeCaptains(TeamEntity teamEntity) throws UnauthorizedAccessException {
        if (teamEntity.getMainCaptain() == null) {
            UserEntity currentUser = getCurrentUserEntity();
            teamEntity.setMainCaptain(currentUser);
            teamEntity.setViceCaptain(currentUser);
        }
    }


    @SneakyThrows({NoSuchMethodException.class})
    private void validationExceptionFor(
            String methodName, int parameterIndex, TeamDTO target, Map<String, String> errors
    ) throws MethodArgumentNotAcceptableException {
        throw MethodArgumentNotAcceptableException.by(methodParameter(methodName, parameterIndex), target, errors);
    }

    private MethodParameter methodParameter(String methodName, int parameterIndex) throws NoSuchMethodException {
        return new MethodParameter(getClass().getMethod(methodName, TeamDTO.class), parameterIndex);
    }
}
