package ru.vldf.sportsportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;
import ru.vldf.sportsportal.domain.sectional.tournament.TeamEntity;
import ru.vldf.sportsportal.dto.sectional.tournament.TeamDTO;
import ru.vldf.sportsportal.mapper.sectional.tournament.TeamMapper;
import ru.vldf.sportsportal.repository.tournament.TeamRepository;
import ru.vldf.sportsportal.service.generic.*;

import javax.persistence.EntityNotFoundException;

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
    @Transactional(
            readOnly = true,
            rollbackFor = {ResourceNotFoundException.class},
            noRollbackFor = {EntityNotFoundException.class}
    )
    public TeamDTO get(Integer id) throws ResourceNotFoundException {
        try {
            return teamMapper.toDTO(teamRepository.getOne(id));
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(msg("sportsportal.tournament.Team.notExistById.message", id), e);
        }
    }

    /**
     * Create and save new team and returns its identifier.
     *
     * @param teamDTO the new team details.
     * @return created team identifier.
     * @throws UnauthorizedAccessException if authorization is missing.
     * @throws ForbiddenAccessException    if current user does not have required permissions.
     */
    @Override
    @Transactional(
            readOnly = true,
            rollbackFor = {UnauthorizedAccessException.class, ForbiddenAccessException.class}
    )
    public Integer create(TeamDTO teamDTO) throws UnauthorizedAccessException, ForbiddenAccessException {
        permissionCheck(teamDTO);
        TeamEntity teamEntity = teamMapper.toEntity(teamDTO);
        if (teamEntity.getMainCaptain() == null) {
            UserEntity userEntity = getCurrentUserEntity();
            teamEntity.setMainCaptain(userEntity);
            teamEntity.setViceCaptain(userEntity);
        }
        return teamRepository.save(teamEntity).getId();
    }

    @Override
    public void update(Integer id, TeamDTO teamDTO) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Integer id) {
        throw new UnsupportedOperationException();
    }


    private void permissionCheck(TeamDTO teamDTO) throws UnauthorizedAccessException, ForbiddenAccessException {
        if ((!currentUserIsAdmin()) && ((teamDTO.getIsLocked() != null) || (teamDTO.getIsDisabled() != null))) {
            throw new ForbiddenAccessException(msg("sportsportal.tournament.Team.forbidden.message"));
        }
    }
}
