package ru.vldf.sportsportal.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vldf.sportsportal.dto.sectional.tournament.TeamDTO;
import ru.vldf.sportsportal.service.TeamService;
import ru.vldf.sportsportal.service.generic.ResourceNotFoundException;

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
    public TeamDTO get(@PathVariable int id) throws ResourceNotFoundException {
        return teamService.get(id);
    }
}
