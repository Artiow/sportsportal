package ru.vldf.sportsportal.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.vldf.sportsportal.dto.sectional.tournament.TeamDTO;
import ru.vldf.sportsportal.service.TeamService;
import ru.vldf.sportsportal.service.generic.ForbiddenAccessException;
import ru.vldf.sportsportal.service.generic.ResourceNotFoundException;
import ru.vldf.sportsportal.service.generic.UnauthorizedAccessException;

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

    @DeleteMapping("/{id}")
    @ApiOperation("удалить команду")
    public ResponseEntity<Void> delete(
            @PathVariable int id
    ) {
        throw new UnsupportedOperationException();
    }
}
