package ru.vldf.sportsportal.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.vldf.sportsportal.dto.pagination.PageDTO;
import ru.vldf.sportsportal.dto.pagination.filters.TeamFilterDTO;
import ru.vldf.sportsportal.dto.sectional.tournament.TeamDTO;
import ru.vldf.sportsportal.dto.sectional.tournament.shortcut.TeamShortDTO;
import ru.vldf.sportsportal.service.TeamService;
import ru.vldf.sportsportal.service.generic.ForbiddenAccessException;
import ru.vldf.sportsportal.service.generic.ResourceNotFoundException;
import ru.vldf.sportsportal.service.generic.UnauthorizedAccessException;

import java.util.Collection;

import static ru.vldf.sportsportal.util.ResourceLocationBuilder.buildURL;

/**
 * @author Namednev Artem
 */
@RestController
@Api(tags = {"Team"})
@RequestMapping("${api.path.tournament.team}")
public class TeamController {

    private final TeamService teamService;


    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }


    /**
     * Returns requested page with teams for current team filter.
     *
     * @param isLocked        the locking flag.
     * @param isDisabled      the disabling flag.
     * @param captainsIds     the list of captains identifiers.
     * @param mainCaptainsIds the list of main captains identifiers.
     * @param viceCaptainsIds the list of vice captains identifiers.
     * @param searchString    the search string.
     * @param pageSize        the page size.
     * @param pageNum         the page number.
     * @return page with short playgrounds details.
     */
    @GetMapping("/list")
    @ApiOperation("получить страницу с командами")
    public PageDTO<TeamShortDTO> getList(
            @RequestParam(required = false) Boolean isLocked,
            @RequestParam(required = false) Boolean isDisabled,
            @RequestParam(required = false) Collection<Integer> captainsIds,
            @RequestParam(required = false) Collection<Integer> mainCaptainsIds,
            @RequestParam(required = false) Collection<Integer> viceCaptainsIds,
            @RequestParam(required = false) String searchString,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) Integer pageNum
    ) {
        TeamFilterDTO teamFilterDTO = new TeamFilterDTO();
        teamFilterDTO.setIsLocked(isLocked);
        teamFilterDTO.setIsDisabled(isDisabled);
        teamFilterDTO.setCaptainsIds(captainsIds);
        teamFilterDTO.setMainCaptainsIds(mainCaptainsIds);
        teamFilterDTO.setViceCaptainsIds(viceCaptainsIds);
        teamFilterDTO.setSearchString(searchString);
        teamFilterDTO.setPageSize(pageSize);
        teamFilterDTO.setPageNum(pageNum);
        return teamService.getList(teamFilterDTO);
    }

    /**
     * Returns team by identifier with full information.
     *
     * @param id the team identifier.
     * @return requested team.
     * @throws ResourceNotFoundException if requested team not found.
     */
    @GetMapping("/{id}")
    @ApiOperation("получить команду")
    public TeamDTO get(
            @PathVariable int id
    ) throws ResourceNotFoundException {
        return teamService.get(id);
    }

    /**
     * Returns team by identifier with short information.
     *
     * @param id the team identifier.
     * @return requested team.
     * @throws ResourceNotFoundException if requested team not found.
     */
    @GetMapping("/{id}/short")
    @ApiOperation("получить команду c краткой информацией")
    public TeamShortDTO getShort(
            @PathVariable int id
    ) throws ResourceNotFoundException {
        return teamService.getShort(id);
    }

    /**
     * Create and save new team and returns its location.
     *
     * @param teamDTO the new team details.
     * @return created team location.
     * @throws UnauthorizedAccessException     if authorization is missing.
     * @throws MethodArgumentNotValidException if method argument not valid.
     */
    @PostMapping
    @ApiOperation("создать команду")
    public ResponseEntity<Void> create(
            @RequestBody @Validated(TeamDTO.CreateCheck.class) TeamDTO teamDTO
    ) throws UnauthorizedAccessException, MethodArgumentNotValidException {
        return ResponseEntity.created(buildURL(teamService.create(teamDTO))).build();
    }

    /**
     * Update and save team details by team identifier.
     *
     * @param id      the team identifier.
     * @param teamDTO the team new details.
     * @return no content.
     * @throws UnauthorizedAccessException     if authorization is missing.
     * @throws ForbiddenAccessException        if user don't have permission to update this team details.
     * @throws MethodArgumentNotValidException if method argument not valid.
     * @throws ResourceNotFoundException       if team not found.
     */
    @PutMapping("/{id}")
    @ApiOperation("редактировать команду")
    public ResponseEntity<Void> update(
            @PathVariable int id, @RequestBody @Validated(TeamDTO.UpdateCheck.class) TeamDTO teamDTO
    ) throws ForbiddenAccessException, UnauthorizedAccessException, MethodArgumentNotValidException, ResourceNotFoundException {
        teamService.update(id, teamDTO);
        return ResponseEntity.noContent().build();
    }

    /**
     * Delete team by team identifier.
     *
     * @param id the team identifier.
     * @return no content.
     * @throws UnauthorizedAccessException if authorization is missing.
     * @throws ForbiddenAccessException    if user don't have permission to delete this team.
     * @throws ResourceNotFoundException   if team not found.
     */
    @DeleteMapping("/{id}")
    @ApiOperation("удалить команду")
    public ResponseEntity<Void> delete(
            @PathVariable int id
    ) throws UnauthorizedAccessException, ForbiddenAccessException, ResourceNotFoundException {
        teamService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
