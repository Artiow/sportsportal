package ru.vldf.sportsportal.service;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity_;
import ru.vldf.sportsportal.domain.sectional.tournament.TeamEntity;
import ru.vldf.sportsportal.domain.sectional.tournament.TeamEntity_;
import ru.vldf.sportsportal.dto.pagination.filters.TeamFilterDTO;
import ru.vldf.sportsportal.dto.sectional.tournament.TeamDTO;
import ru.vldf.sportsportal.mapper.sectional.tournament.TeamMapper;
import ru.vldf.sportsportal.repository.tournament.TeamRepository;
import ru.vldf.sportsportal.service.generic.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
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
        return teamMapper.toDTO(findById(id));
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
        TeamEntity teamEntity = findById(id);
        rightsCheck(teamEntity);
        teamEntity = teamMapper.inject(teamEntity, teamDTO);
        normalizeCaptains(teamEntity);
        teamRepository.save(teamEntity);
    }

    /**
     * Delete team by team identifier.
     *
     * @param id the team identifier.
     * @throws UnauthorizedAccessException if authorization is missing.
     * @throws ForbiddenAccessException    if user don't have permission to delete this team.
     * @throws ResourceNotFoundException   if team not found.
     */
    @Override
    @Transactional(rollbackFor = {UnauthorizedAccessException.class, ForbiddenAccessException.class, ResourceNotFoundException.class})
    public void delete(Integer id) throws UnauthorizedAccessException, ForbiddenAccessException, ResourceNotFoundException {
        TeamEntity teamEntity = findById(id);
        rightsCheck(teamEntity);
        teamRepository.delete(teamEntity);
    }


    private TeamEntity findById(int id) throws ResourceNotFoundException {
        return teamRepository.findById(id).orElseThrow(ResourceNotFoundException.supplier(msg("sportsportal.tournament.Team.notExistById.message", id)));
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


    public static class TeamFilter extends StringSearcher<TeamEntity> {

        private Collection<Integer> mainCaptainsIds;
        private Collection<Integer> viceCaptainsIds;
        private Collection<Integer> captainsIds;
        private Boolean isDisabled;
        private Boolean isLocked;


        public TeamFilter(TeamFilterDTO dto) {
            super(dto, TeamEntity_.name);
            this.mainCaptainsIds = !CollectionUtils.isEmpty(dto.getMainCaptainsIds()) ? new ArrayList<>(dto.getMainCaptainsIds()) : null;
            this.viceCaptainsIds = !CollectionUtils.isEmpty(dto.getViceCaptainsIds()) ? new ArrayList<>(dto.getViceCaptainsIds()) : null;
            this.captainsIds = !CollectionUtils.isEmpty(dto.getCaptainsIds()) ? new ArrayList<>(dto.getCaptainsIds()) : null;
            this.isDisabled = dto.getIsDisabled();
            this.isLocked = dto.getIsLocked();
        }


        @Override
        public Predicate toPredicate(Root<TeamEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            Collection<Predicate> predicates = new ArrayList<>();
            Predicate rootPredicate = super.toPredicate(root, query, cb);
            if (rootPredicate != null) {
                predicates.add(rootPredicate);
            }
            if (mainCaptainsIds != null) {
                predicates.add(
                        root.join(TeamEntity_.mainCaptain).get(UserEntity_.id).in(mainCaptainsIds)
                );
            }
            if (viceCaptainsIds != null) {
                predicates.add(
                        root.join(TeamEntity_.viceCaptain).get(UserEntity_.id).in(viceCaptainsIds)
                );
            }
            if (captainsIds != null) {
                predicates.add(cb.or(
                        root.join(TeamEntity_.mainCaptain).get(UserEntity_.id).in(captainsIds),
                        root.join(TeamEntity_.viceCaptain).get(UserEntity_.id).in(captainsIds)
                ));
            }
            if (isDisabled != null) {
                predicates.add(cb.equal(root.get(TeamEntity_.isDisabled), isDisabled));
            }
            if (isLocked != null) {
                predicates.add(cb.equal(root.get(TeamEntity_.isLocked), isLocked));
            }
            return query
                    .where(cb.and(predicates.toArray(new Predicate[0])))
                    .orderBy(cb.asc(root.get(TeamEntity_.name)))
                    .distinct(true).getRestriction();
        }
    }
}
