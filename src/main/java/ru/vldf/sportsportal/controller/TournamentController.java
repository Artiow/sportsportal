package ru.vldf.sportsportal.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vldf.sportsportal.service.TournamentService;

import java.util.Arrays;

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
    @ApiOperation("создать турнир (тест)")
    public ResponseEntity<Void> create() {
        return ResponseEntity.created(buildURL(tournamentService.create("test", Arrays.asList(1, 2, 3)))).build();
    }
}
