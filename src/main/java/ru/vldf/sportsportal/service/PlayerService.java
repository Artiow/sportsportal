package ru.vldf.sportsportal.service;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import ru.vldf.sportsportal.service.general.throwable.ForbiddenAccessException;
import ru.vldf.sportsportal.service.general.throwable.MethodArgumentNotAcceptableException;
import ru.vldf.sportsportal.service.general.throwable.ResourceNotFoundException;
import ru.vldf.sportsportal.service.general.throwable.UnauthorizedAccessException;

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
        updateCheck(playerDTO);
        PlayerEntity playerEntity = findById(id);
        rightsCheck(playerEntity);
        playerEntity = playerMapper.inject(playerEntity, playerDTO);

        if (!currentUserIsAdmin()) {
            // disabling, admin check required
            playerEntity.setIsDisabled(true);
        }

        playerRepository.save(updatedByCurrentUser(playerEntity));
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
            validationExceptionFor("create", 0, playerDTO, errors);
        }
    }

    private void updateCheck(PlayerDTO playerDTO) throws UnauthorizedAccessException, MethodArgumentNotAcceptableException {
        boolean currentUserIsAdmin = currentUserIsAdmin();
        Map<String, String> errors = new HashMap<>();
        if (!currentUserIsAdmin && (playerDTO.getIsLocked() != null)) {
            errors.put("isLocked", msg("sportsportal.tournament.Player.validation.forbiddenIsLocked.message"));
        }
        if (!currentUserIsAdmin && (playerDTO.getIsDisabled() != null)) {
            errors.put("isDisabled", msg("sportsportal.tournament.Player.validation.forbiddenIsDisabled.message"));
        }
        if (playerRepository.existsByNameAndSurnameAndPatronymicAndBirthdateAndIdIsNot(playerDTO.getName(), playerDTO.getSurname(), playerDTO.getPatronymic(), javaTimeMapper.toTimestamp(playerDTO.getBirthdate()), playerDTO.getId())) {
            errors.put(null, msg("sportsportal.tournament.Player.validation.alreadyExistByFullName.message"));
        }
        if (!errors.isEmpty()) {
            validationExceptionFor("update", 1, playerDTO, errors);
        }
    }

    private void rightsCheck(PlayerEntity playerEntity) throws UnauthorizedAccessException, ForbiddenAccessException {
        if (!currentUserIsAdmin() && !(isCurrentUser(playerEntity.getUser()) || isCurrentUser(playerEntity.getCreator()))) {
            throw new ForbiddenAccessException(msg("sportsportal.tournament.Player.forbidden.message"));
        }
    }


    @SneakyThrows({NoSuchMethodException.class})
    private void validationExceptionFor(
            String methodName, int parameterIndex, PlayerDTO target, Map<String, String> errors
    ) throws MethodArgumentNotAcceptableException {
        throw MethodArgumentNotAcceptableException.by(methodParameter(methodName, parameterIndex), target, errors);
    }

    private MethodParameter methodParameter(String methodName, int parameterIndex) throws NoSuchMethodException {
        return new MethodParameter(getClass().getMethod(methodName, PlayerDTO.class), parameterIndex);
    }


    public static class PlayerFilter extends StringSearcher<PlayerEntity> {

        private Boolean isDisabled;
        private Boolean isLocked;

        public PlayerFilter(PlayerFilterDTO dto) {
            super(dto, PlayerEntity_.name, PlayerEntity_.surname, PlayerEntity_.patronymic);
            this.isDisabled = dto.getIsDisabled();
            this.isLocked = dto.getIsLocked();
        }

        @Override
        public Predicate toPredicate(Root<PlayerEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            Collection<Predicate> predicates = new ArrayList<>();
            Predicate rootPredicate = super.toPredicate(root, query, cb);
            if (rootPredicate != null) {
                predicates.add(rootPredicate);
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
