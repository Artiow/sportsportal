package ru.vldf.sportsportal.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vldf.sportsportal.dto.sectional.tournament.links.TeamLinkDTO;
import ru.vldf.sportsportal.service.TournamentService;

import java.util.Collection;

import static ru.vldf.sportsportal.util.ResourceLocationBuilder.buildURL;

/**
 * @author Namednev Artem
 */
@RestController
@Api(tags = {"Tournament"})
@RequestMapping("${api.path.tournament.tournament}")
public class TournamentController {

    private final TournamentService tournamentService;

    @Autowired
    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @PostMapping
    @ApiOperation("создать турнир (круговой)")
    // todo: validation with tournament object!
    public ResponseEntity<Void> create(
            @RequestParam String name,
            @RequestBody Collection<TeamLinkDTO> teams
    ) {
        return ResponseEntity.created(buildURL(tournamentService.create(name, teams))).build();
    }
}
