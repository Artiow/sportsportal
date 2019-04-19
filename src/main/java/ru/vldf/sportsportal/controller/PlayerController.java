package ru.vldf.sportsportal.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.vldf.sportsportal.dto.pagination.PageDTO;
import ru.vldf.sportsportal.dto.pagination.filters.PlayerFilterDTO;
import ru.vldf.sportsportal.dto.sectional.tournament.PlayerDTO;
import ru.vldf.sportsportal.dto.sectional.tournament.shortcut.PlayerShortDTO;
import ru.vldf.sportsportal.service.PlayerService;
import ru.vldf.sportsportal.service.general.throwable.ForbiddenAccessException;
import ru.vldf.sportsportal.service.general.throwable.ResourceNotFoundException;
import ru.vldf.sportsportal.service.general.throwable.UnauthorizedAccessException;

import java.time.LocalDate;

import static ru.vldf.sportsportal.util.ResourceLocationBuilder.buildURL;

/**
 * @author Namednev Artem
 */
@RestController
@Api(tags = {"Player"})
@RequestMapping("${api.path.tournament.player}")
public class PlayerController {

    private final PlayerService playerService;


    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }


    /**
     * Returns requested page with players for current player filter.
     *
     * @param name the player name.
     * @param surname the player surname.
     * @param patronymic the player patronymic.
     * @param minBirthdate the player minimum birthdate.
     * @param maxBirthdate the player maximum birthdate.
     * @param isLocked     the locking flag.
     * @param isDisabled   the disabling flag.
     * @param searchString the search string.
     * @param pageSize     the page size.
     * @param pageNum      the page number.
     * @return page with short players details.
     */
    @GetMapping("/list")
    @ApiOperation("получить страницу с игроками")
    public PageDTO<PlayerShortDTO> getList(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname,
            @RequestParam(required = false) String patronymic,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate minBirthdate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate maxBirthdate,
            @RequestParam(required = false) Boolean isLocked,
            @RequestParam(required = false) Boolean isDisabled,
            @RequestParam(required = false) String searchString,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) Integer pageNum
    ) {
        PlayerFilterDTO playerFilterDTO = new PlayerFilterDTO();
        playerFilterDTO.setName(name);
        playerFilterDTO.setSurname(surname);
        playerFilterDTO.setPatronymic(patronymic);
        playerFilterDTO.setMinBirthdate(minBirthdate);
        playerFilterDTO.setMaxBirthdate(maxBirthdate);
        playerFilterDTO.setIsLocked(isLocked);
        playerFilterDTO.setIsDisabled(isDisabled);
        playerFilterDTO.setSearchString(searchString);
        playerFilterDTO.setPageSize(pageSize);
        playerFilterDTO.setPageNum(pageNum);
        return playerService.getList(playerFilterDTO);
    }

    /**
     * Returns player by identifier with full information.
     *
     * @param id the player identifier.
     * @return requested player.
     * @throws ResourceNotFoundException if requested player not found.
     */
    @GetMapping("/{id}")
    @ApiOperation("получить игрока")
    public PlayerDTO get(
            @PathVariable int id
    ) throws ResourceNotFoundException {
        return playerService.get(id);
    }

    /**
     * Returns player by identifier with short information.
     *
     * @param id the player identifier.
     * @return requested player.
     * @throws ResourceNotFoundException if requested player not found.
     */
    @GetMapping("/{id}/short")
    @ApiOperation("получить игрока c краткой информацией")
    public PlayerShortDTO getShort(
            @PathVariable int id
    ) throws ResourceNotFoundException {
        return playerService.getShort(id);
    }

    /**
     * Create and save new player and returns its location.
     *
     * @param playerDTO the new player details.
     * @return created player location.
     * @throws UnauthorizedAccessException     if authorization is missing.
     * @throws MethodArgumentNotValidException if method argument not valid.
     */
    @PostMapping
    @ApiOperation("создать игрока")
    public ResponseEntity<Void> create(
            @RequestBody @Validated(PlayerDTO.CreateCheck.class) PlayerDTO playerDTO
    ) throws UnauthorizedAccessException, MethodArgumentNotValidException {
        return ResponseEntity.created(buildURL(playerService.create(playerDTO))).build();
    }

    /**
     * Update and save player details by player identifier.
     *
     * @param id        the player identifier.
     * @param playerDTO the player new details.
     * @return no content.
     * @throws UnauthorizedAccessException     if authorization is missing.
     * @throws ForbiddenAccessException        if user don't have permission to update this player details.
     * @throws MethodArgumentNotValidException if method argument not valid.
     * @throws ResourceNotFoundException       if player not found.
     */
    @PutMapping("/{id}")
    @ApiOperation("редактировать игрока")
    public ResponseEntity<Void> update(
            @PathVariable int id, @RequestBody @Validated(PlayerDTO.UpdateCheck.class) PlayerDTO playerDTO
    ) throws ForbiddenAccessException, UnauthorizedAccessException, MethodArgumentNotValidException, ResourceNotFoundException {
        playerService.update(id, playerDTO);
        return ResponseEntity.noContent().build();
    }

    /**
     * Delete player by player identifier.
     *
     * @param id the player identifier.
     * @return no content.
     * @throws UnauthorizedAccessException if authorization is missing.
     * @throws ForbiddenAccessException    if user don't have permission to delete this player.
     * @throws ResourceNotFoundException   if player not found.
     */
    @DeleteMapping("/{id}")
    @ApiOperation("удалить игрока")
    public ResponseEntity<Void> delete(
            @PathVariable int id
    ) throws UnauthorizedAccessException, ForbiddenAccessException, ResourceNotFoundException {
        playerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
