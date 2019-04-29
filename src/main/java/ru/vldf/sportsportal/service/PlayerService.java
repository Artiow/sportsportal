package ru.vldf.sportsportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;
import ru.vldf.sportsportal.domain.sectional.tournament.PlayerEntity;
import ru.vldf.sportsportal.domain.sectional.tournament.PlayerEntity_;
import ru.vldf.sportsportal.dto.pagination.PageDTO;
import ru.vldf.sportsportal.dto.pagination.filters.PlayerFilterDTO;
import ru.vldf.sportsportal.dto.sectional.tournament.PlayerDTO;
import ru.vldf.sportsportal.dto.sectional.tournament.shortcut.PlayerShortDTO;
import ru.vldf.sportsportal.mapper.manual.JavaTimeMapper;
import ru.vldf.sportsportal.mapper.sectional.tournament.PlayerMapper;
import ru.vldf.sportsportal.repository.tournament.PlayerRepository;
import ru.vldf.sportsportal.service.general.AbstractSecurityService;
import ru.vldf.sportsportal.service.general.CRUDService;
import ru.vldf.sportsportal.service.general.throwable.*;
import ru.vldf.sportsportal.util.ReflectionUtil;
import ru.vldf.sportsportal.util.domain.PlayerBindingChecker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Namednev Artem
 */
@Service
public class PlayerService extends AbstractSecurityService implements CRUDService<PlayerEntity, PlayerDTO> {

    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;
    private final JavaTimeMapper javaTimeMapper;


    @Autowired
    public PlayerService(PlayerRepository playerRepository, PlayerMapper playerMapper, JavaTimeMapper javaTimeMapper) {
        this.playerRepository = playerRepository;
        this.playerMapper = playerMapper;
        this.javaTimeMapper = javaTimeMapper;
    }


    private static MethodParameter createMethodParameter() {
        return ReflectionUtil.methodParameter(PlayerService.class, "create", new Class[]{PlayerDTO.class}, 0);
    }

    private static MethodParameter updateMethodParameter() {
        return ReflectionUtil.methodParameter(PlayerService.class, "update", new Class[]{Integer.class, PlayerDTO.class}, 1);
    }


    /**
     * Returns requested filtered page with list of players.
     *
     * @param filterDTO the filter parameters.
     * @return filtered requested page with players.
     */
    @Transactional(readOnly = true)
    public PageDTO<PlayerShortDTO> getList(PlayerFilterDTO filterDTO) {
        PlayerFilter filter = new PlayerFilter(filterDTO);
        return PageDTO.from(playerRepository.findAll(filter, filter.getPageRequest()).map(playerMapper::toShortDTO));
    }

    /**
     * Returns requested player by player identifier.
     *
     * @param id the player identifier.
     * @return requested player data.
     * @throws ResourceNotFoundException if player not found.
     */
    @Override
    @Transactional(readOnly = true, rollbackFor = {ResourceNotFoundException.class})
    public PlayerDTO get(Integer id) throws ResourceNotFoundException {
        return playerMapper.toDTO(findById(id));
    }

    /**
     * Returns requested player by player identifier.
     *
     * @param id the player identifier.
     * @return requested player short data.
     * @throws ResourceNotFoundException if player not found.
     */
    @Transactional(readOnly = true, rollbackFor = {ResourceNotFoundException.class})
    public PlayerShortDTO getShort(Integer id) throws ResourceNotFoundException {
        return playerMapper.toShortDTO(findById(id));
    }

    /**
     * Create and save new player and returns its identifier.
     *
     * @param playerDTO the new player details.
     * @return created player identifier.
     * @throws UnauthorizedAccessException          if authorization is missing.
     * @throws MethodArgumentNotAcceptableException if method argument not acceptable.
     */
    @Override
    @Transactional(rollbackFor = {UnauthorizedAccessException.class, MethodArgumentNotAcceptableException.class})
    public Integer create(PlayerDTO playerDTO) throws UnauthorizedAccessException, MethodArgumentNotAcceptableException {
        createCheck(playerDTO);
        return playerRepository.save(createdByCurrentUser(playerMapper.toEntity(playerDTO))).getId();
    }

    /**
     * Update and save player details by player identifier.
     *
     * @param id        the player identifier.
     * @param playerDTO the player new details.
     * @throws UnauthorizedAccessException          if authorization is missing.
     * @throws ForbiddenAccessException             if user don't have permission to update this player details.
     * @throws MethodArgumentNotAcceptableException if method argument not acceptable.
     * @throws ResourceNotFoundException            if player not found.
     */
    @Override
    @Transactional(rollbackFor = {UnauthorizedAccessException.class, ForbiddenAccessException.class, MethodArgumentNotAcceptableException.class, ResourceNotFoundException.class})
    public void update(Integer id, PlayerDTO playerDTO) throws UnauthorizedAccessException, ForbiddenAccessException, MethodArgumentNotAcceptableException, ResourceNotFoundException {
        updateCheck(id, playerDTO);
        PlayerEntity playerEntity = findById(id);
        rightsCheck(playerEntity);
        playerEntity = playerMapper.inject(playerEntity, playerDTO);

        if (!PlayerBindingChecker.match(playerEntity, playerEntity.getUser()) && !currentUserIsAdmin()) {
            // player binding reset
            playerEntity.setUser(null);
        }

        if (!currentUserIsAdmin()) {
            // disabling, admin check required
            playerEntity.setIsDisabled(true);
        }

        playerRepository.save(updatedByCurrentUser(playerEntity));
    }

    /**
     * Assign player to user.
     *
     * @param playerId the player identifier.
     * @param userId   the user identifier.
     * @param force    {@literal true} if assigning must be override, {@literal false} otherwise.
     * @throws UnauthorizedAccessException   if authorization is missing.
     * @throws ForbiddenAccessException      if user don't have permission to update this player details.
     * @throws ResourceNotFoundException     if player or user not found.
     * @throws ResourceCannotUpdateException if player already assigned or user details mismatch.
     */
    @Transactional(rollbackFor = {UnauthorizedAccessException.class, ForbiddenAccessException.class, ResourceNotFoundException.class, ResourceCannotUpdateException.class})
    public void assign(Integer playerId, Integer userId, Boolean force) throws UnauthorizedAccessException, ForbiddenAccessException, ResourceNotFoundException, ResourceCannotUpdateException {
        PlayerEntity playerEntity = findById(playerId);
        if ((playerEntity.getUser() == null) || (force == Boolean.TRUE)) {
            rightsCheck(playerEntity);
            UserEntity userEntity = userId != null ? findUserById(userId) : null;
            if (PlayerBindingChecker.match(playerEntity, userEntity) || currentUserIsAdmin()) {
                playerEntity.setUser(userEntity);
                playerRepository.save(playerEntity);
            } else {
                throw new ResourceCannotUpdateException(msg("sportsportal.tournament.Player.cannotBeAssigned.message", playerId, userId));
            }
        } else {
            throw new ResourceCannotUpdateException(msg("sportsportal.tournament.Player.alreadyAssigned.message", playerId, playerEntity.getUser().getId()));
        }
    }

    /**
     * Delete player by player identifier.
     *
     * @param id the player identifier.
     * @throws UnauthorizedAccessException if authorization is missing.
     * @throws ForbiddenAccessException    if user don't have permission to delete this player.
     * @throws ResourceNotFoundException   if player not found.
     */
    @Override
    @Transactional(rollbackFor = {UnauthorizedAccessException.class, ForbiddenAccessException.class, ResourceNotFoundException.class})
    public void delete(Integer id) throws UnauthorizedAccessException, ForbiddenAccessException, ResourceNotFoundException {
        PlayerEntity playerEntity = findById(id);
        rightsCheck(playerEntity);
        playerRepository.delete(playerEntity);
    }


    private PlayerEntity findById(int id) throws ResourceNotFoundException {
        return playerRepository.findById(id).orElseThrow(ResourceNotFoundException.supplier(msg("sportsportal.tournament.Player.notExistById.message", id)));
    }


    private void createCheck(PlayerDTO playerDTO) throws UnauthorizedAccessException, MethodArgumentNotAcceptableException {
        Map<String, String> errors = new HashMap<>();
        boolean currentUserIsAdmin = currentUserIsAdmin();
        if (!currentUserIsAdmin && (playerDTO.getIsLocked() != null)) {
            errors.put("isLocked", msg("sportsportal.tournament.Player.validation.forbiddenIsLocked.message"));
        }
        if (!currentUserIsAdmin && (playerDTO.getIsDisabled() != null)) {
            errors.put("isDisabled", msg("sportsportal.tournament.Player.validation.forbiddenIsDisabled.message"));
        }
        if (playerRepository.existsByNameAndSurnameAndPatronymicAndBirthdate(playerDTO.getName(), playerDTO.getSurname(), playerDTO.getPatronymic(), javaTimeMapper.toTimestamp(playerDTO.getBirthdate()))) {
            errors.put(null, msg("sportsportal.tournament.Player.validation.alreadyExistByFullName.message"));
        }
        if (!errors.isEmpty()) {
            throw MethodArgumentNotAcceptableException.by(createMethodParameter(), playerDTO, errors);
        }
    }

    private void updateCheck(Integer playerId, PlayerDTO playerDTO) throws UnauthorizedAccessException, MethodArgumentNotAcceptableException {
        boolean currentUserIsAdmin = currentUserIsAdmin();
        Map<String, String> errors = new HashMap<>();
        if (!currentUserIsAdmin && (playerDTO.getIsLocked() != null)) {
            errors.put("isLocked", msg("sportsportal.tournament.Player.validation.forbiddenIsLocked.message"));
        }
        if (!currentUserIsAdmin && (playerDTO.getIsDisabled() != null)) {
            errors.put("isDisabled", msg("sportsportal.tournament.Player.validation.forbiddenIsDisabled.message"));
        }
        if (playerRepository.existsByNameAndSurnameAndPatronymicAndBirthdateAndIdIsNot(playerDTO.getName(), playerDTO.getSurname(), playerDTO.getPatronymic(), javaTimeMapper.toTimestamp(playerDTO.getBirthdate()), playerId)) {
            errors.put(null, msg("sportsportal.tournament.Player.validation.alreadyExistByFullName.message"));
        }
        if (!errors.isEmpty()) {
            throw MethodArgumentNotAcceptableException.by(updateMethodParameter(), playerDTO, errors);
        }
    }

    private void rightsCheck(PlayerEntity playerEntity) throws UnauthorizedAccessException, ForbiddenAccessException {
        if (!currentUserIsAdmin() && !(isCurrentUser(playerEntity.getUser()) || isCurrentUser(playerEntity.getCreator()))) {
            throw new ForbiddenAccessException(msg("sportsportal.tournament.Player.forbidden.message"));
        }
    }


    public static class PlayerFilter extends StringSearcher<PlayerEntity> {

        // crutch! todo: separate the filter object and specification logic
        private final JavaTimeMapper mapper = new JavaTimeMapper();

        private String name;
        private String surname;
        private String patronymic;
        private Timestamp minBirthdate;
        private Timestamp maxBirthdate;
        private Boolean isDisabled;
        private Boolean isLocked;

        public PlayerFilter(PlayerFilterDTO dto) {
            super(dto, PlayerEntity_.name, PlayerEntity_.surname, PlayerEntity_.patronymic);
            this.name = StringUtils.hasText(dto.getName()) ? dto.getName().trim().toLowerCase() : null;
            this.surname = StringUtils.hasText(dto.getSurname()) ? dto.getSurname().trim().toLowerCase() : null;
            this.patronymic = StringUtils.hasText(dto.getPatronymic()) ? dto.getPatronymic().trim().toLowerCase() : null;
            this.minBirthdate = mapper.toTimestamp(dto.getMinBirthdate());
            this.maxBirthdate = mapper.toTimestamp(dto.getMaxBirthdate());
            this.isDisabled = dto.getIsDisabled();
            this.isLocked = dto.getIsLocked();
        }

        @Override
        public Predicate toPredicate(Root<PlayerEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            List<Predicate> predicates = super.toPredicateList(root, cb);
            if (name != null) {
                predicates.add(cb.like(cb.lower(root.get(PlayerEntity_.name)), name));
            }
            if (surname != null) {
                predicates.add(cb.like(cb.lower(root.get(PlayerEntity_.surname)), surname));
            }
            if (patronymic != null) {
                predicates.add(cb.like(cb.lower(root.get(PlayerEntity_.patronymic)), patronymic));
            }
            if (minBirthdate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get(PlayerEntity_.birthdate), minBirthdate));
            }
            if (maxBirthdate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get(PlayerEntity_.birthdate), maxBirthdate));
            }
            if (isDisabled != null) {
                predicates.add(cb.equal(root.get(PlayerEntity_.isDisabled), isDisabled));
            }
            if (isLocked != null) {
                predicates.add(cb.equal(root.get(PlayerEntity_.isLocked), isLocked));
            }
            return query
                    .where(cb.and(predicates.toArray(new Predicate[0])))
                    .orderBy(cb.asc(root.get(PlayerEntity_.surname)))
                    .distinct(true).getRestriction();
        }
    }
}
