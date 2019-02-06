package ru.vldf.sportsportal.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.vldf.sportsportal.dto.pagination.PageDTO;
import ru.vldf.sportsportal.dto.pagination.filters.PlaygroundFilterDTO;
import ru.vldf.sportsportal.dto.sectional.lease.PlaygroundDTO;
import ru.vldf.sportsportal.dto.sectional.lease.shortcut.PlaygroundShortDTO;
import ru.vldf.sportsportal.dto.sectional.lease.specialized.PlaygroundBoardDTO;
import ru.vldf.sportsportal.dto.sectional.lease.specialized.ReservationListDTO;
import ru.vldf.sportsportal.service.PlaygroundService;
import ru.vldf.sportsportal.service.generic.*;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

import static ru.vldf.sportsportal.util.ResourceLocationBuilder.buildURL;

/**
 * @author Namednev Artem
 */
@RestController
@Api(tags = {"Playground"})
@RequestMapping("${api.path.lease.playground}")
public class PlaygroundController {

    private final PlaygroundService playgroundService;

    @Value("${api.path.lease.order}")
    private String orderPath;


    @Autowired
    public PlaygroundController(PlaygroundService playgroundService) {
        this.playgroundService = playgroundService;
    }


    /**
     * Returns requested page with playgrounds for current playground filter.
     *
     * @param opening      {@link LocalTime} opening time
     * @param closing      {@link LocalTime} closing time
     * @param featureCodes {@link Collection<String>} feature codes
     * @param sportCodes   {@link Collection<String>} sport codes
     * @param startPrice   {@link BigDecimal} minimal playground price
     * @param endPrice     {@link BigDecimal} maximal playground price
     * @param searchString {@link String} search string
     * @param pageSize     {@link Integer} page size
     * @param pageNum      {@link Integer} page number
     * @param minRate      {@link Integer} minimal playground rate
     * @return {@link PageDTO<PlaygroundShortDTO>} page with playgrounds
     */
    @GetMapping("/list")
    @ApiOperation("получить страницу с площадками")
    public PageDTO<PlaygroundShortDTO> getList(
            @RequestParam(required = false) @DateTimeFormat(iso = ISO.TIME) LocalTime opening,
            @RequestParam(required = false) @DateTimeFormat(iso = ISO.TIME) LocalTime closing,
            @RequestParam(required = false) Collection<String> featureCodes,
            @RequestParam(required = false) Collection<String> sportCodes,
            @RequestParam(required = false) BigDecimal startPrice,
            @RequestParam(required = false) BigDecimal endPrice,
            @RequestParam(required = false) String searchString,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer minRate
    ) {
        PlaygroundFilterDTO playgroundFilterDTO = new PlaygroundFilterDTO();
        playgroundFilterDTO.setFeatureCodes(featureCodes);
        playgroundFilterDTO.setSportCodes(sportCodes);
        playgroundFilterDTO.setStartPrice(startPrice);
        playgroundFilterDTO.setEndPrice(endPrice);
        playgroundFilterDTO.setMinRate(minRate);
        playgroundFilterDTO.setOpening(opening);
        playgroundFilterDTO.setClosing(closing);
        playgroundFilterDTO.setSearchString(searchString);
        playgroundFilterDTO.setPageSize(pageSize);
        playgroundFilterDTO.setPageNum(pageNum);
        return playgroundService.getList(playgroundFilterDTO);
    }

    /**
     * Returns playground by identifier with full information.
     *
     * @param id playground identifier
     * @return {@link PlaygroundDTO} requested playground
     * @throws ResourceNotFoundException if requested playground not found
     */
    @GetMapping("/{id}")
    @ApiOperation("получить площадку")
    public PlaygroundDTO get(@PathVariable int id) throws ResourceNotFoundException {
        return playgroundService.get(id);
    }

    /**
     * Returns playground by identifier with short information.
     *
     * @param id playground identifier
     * @return {@link PlaygroundShortDTO} requested playground
     * @throws ResourceNotFoundException if requested playground not found
     */
    @GetMapping("/{id}/short")
    @ApiOperation("получить площадку c краткой информацией")
    public PlaygroundShortDTO getShort(@PathVariable int id) throws ResourceNotFoundException {
        return playgroundService.getShort(id);
    }

    /**
     * Returns requested playground with time grid by start date and end date.
     *
     * @param id   playground identifier
     * @param from {@link Date} first date of grid
     * @param to   {@link Date} last date of grid
     * @return {@link PlaygroundBoardDTO} requested time grid
     * @throws ResourceNotFoundException if requested playground not found
     */
    @GetMapping("/{id}/grid")
    @ApiOperation("получить сетку времени для площадки")
    public PlaygroundBoardDTO getGrid(
            @PathVariable int id,
            @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate to
    ) throws ResourceNotFoundException, ResourceCorruptedException {
        return playgroundService.getGrid(id, from, to);
    }

    /**
     * Returns available for reservation times for playground by identifier and collections of checked
     * reservation times.
     *
     * @param id           playground identifier
     * @param version      playground version
     * @param reservations {@link Set<String>} checked reservation times
     * @return {@link ReservationListDTO} with available for reservation times
     * @throws ResourceNotFoundException if requested playground not found
     */
    @GetMapping("/{id}/check")
    @ApiOperation("проверить доступность бронирования")
    public ReservationListDTO check(
            @PathVariable int id,
            @RequestParam long version,
            @RequestParam(required = false) Set<String> reservations
    ) throws ResourceNotFoundException {
        return playgroundService.check(id, version, reservations);
    }

    /**
     * Reserve playground for authorize user by sent datetime and returns order location.
     *
     * @param id                 playground identifier
     * @param reservationListDTO {@link ReservationListDTO} reservation data
     * @return new order {@link URI}
     * @throws UnauthorizedAccessException   if authorization is missing
     * @throws ResourceNotFoundException     if requested playground not found
     * @throws ResourceCannotCreateException if reservation cannot create
     */
    @PostMapping("/{id}/reserve")
    @ApiOperation("забронировать площадку")
    public ResponseEntity<Void> reserve(@PathVariable int id, @RequestBody @Validated ReservationListDTO reservationListDTO)
            throws UnauthorizedAccessException, ResourceNotFoundException, ResourceCannotCreateException {
        return ResponseEntity.created(buildURL(orderPath, playgroundService.reserve(id, reservationListDTO))).build();
    }

    /**
     * Create playground and returns its location.
     *
     * @param playgroundDTO {@link PlaygroundDTO} new playground data
     * @return new playgrounds {@link URI}
     * @throws UnauthorizedAccessException   if authorization is missing
     * @throws ResourceCannotCreateException if playground create update
     */
    @PostMapping
    @ApiOperation("создать площадку")
    public ResponseEntity<Void> create(@RequestBody @Validated(PlaygroundDTO.CreateCheck.class) PlaygroundDTO playgroundDTO)
            throws UnauthorizedAccessException, ResourceCannotCreateException {
        return ResponseEntity.created(buildURL(playgroundService.create(playgroundDTO))).build();
    }

    /**
     * Update playground by id.
     *
     * @param id            playground identifier
     * @param playgroundDTO {@link PlaygroundDTO} playground data
     * @return no content
     * @throws UnauthorizedAccessException     if authorization is missing
     * @throws ForbiddenAccessException        if user don't have permission to update this playground
     * @throws ResourceNotFoundException       if playground not found
     * @throws ResourceCannotUpdateException   if playground cannot update
     * @throws ResourceOptimisticLockException if playground was already updated
     */
    @PutMapping("/{id}")
    @ApiOperation("редактировать площадку")
    public ResponseEntity<Void> update(@PathVariable int id, @RequestBody @Validated(PlaygroundDTO.UpdateCheck.class) PlaygroundDTO playgroundDTO)
            throws UnauthorizedAccessException, ForbiddenAccessException, ResourceNotFoundException, ResourceCannotUpdateException, ResourceOptimisticLockException {
        playgroundService.update(id, playgroundDTO);
        return ResponseEntity.noContent().build();
    }

    /**
     * Delete playground by id.
     *
     * @param id playground identifier
     * @return no content
     * @throws UnauthorizedAccessException if authorization is missing
     * @throws ForbiddenAccessException    if user don't have permission to delete this playground
     * @throws ResourceNotFoundException   if playground not found
     */
    @DeleteMapping("/{id}")
    @ApiOperation("удалить площадку")
    public ResponseEntity<Void> delete(@PathVariable int id)
            throws UnauthorizedAccessException, ForbiddenAccessException, ResourceNotFoundException {
        playgroundService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
