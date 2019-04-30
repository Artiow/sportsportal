package ru.vldf.sportsportal.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.vldf.sportsportal.dto.sectional.tournament.TournamentDTO;
import ru.vldf.sportsportal.service.TournamentService;
import ru.vldf.sportsportal.service.general.throwable.MethodArgumentNotAcceptableException;
import ru.vldf.sportsportal.service.general.throwable.ResourceNotFoundException;

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


    /**
     * Returns requested tournament by tournament identifier.
     *
     * @param id the tournament identifier.
     * @return requested tournament data.
     * @throws ResourceNotFoundException if requested tournament not found.
     */
    @GetMapping("/{id}")
    @ApiOperation("получить турнир")
    public TournamentDTO get(
            @PathVariable int id
    ) throws ResourceNotFoundException {
        return tournamentService.get(id);
    }

    /**
     * Generate and save new round-robin tournament returns its identifier.
     *
     * @param tournamentDTO the new tournament details.
     * @return generated tournament identifier.
     * @throws MethodArgumentNotAcceptableException if method argument not acceptable.
     */
    @PostMapping("/generating")
    @ApiOperation("сгенерировать турнир (круговой)")
    public ResponseEntity<Void> generate(
            @RequestBody @Validated(TournamentDTO.GenerateCheck.class) TournamentDTO tournamentDTO
    ) throws MethodArgumentNotAcceptableException {
        return ResponseEntity.created(buildURL(tournamentService.generate(tournamentDTO))).build();
    }

    /**
     * Update and save tournament details by tournament identifier.
     *
     * @param id            the tournament identifier.
     * @param tournamentDTO the tournament new details.
     * @return no content.
     * @throws MethodArgumentNotAcceptableException if method argument not acceptable.
     * @throws ResourceNotFoundException            if tournament not found.
     */
    @PostMapping("/{id}")
    @ApiOperation("редактировать турнир")
    public ResponseEntity<Void> generate(
            @PathVariable int id, @RequestBody @Validated(TournamentDTO.UpdateCheck.class) TournamentDTO tournamentDTO
    ) throws MethodArgumentNotAcceptableException, ResourceNotFoundException {
        tournamentService.update(id, tournamentDTO);
        return ResponseEntity.noContent().build();
    }
}
