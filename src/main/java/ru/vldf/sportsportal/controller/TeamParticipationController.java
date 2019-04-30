package ru.vldf.sportsportal.controller;

import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vldf.sportsportal.dto.sectional.tournament.specialized.TeamParticipationDTO;

import java.util.Collection;

/**
 * @author Artem Namednev
 */
@RestController
@Api(tags = {"Team Participation"})
@RequestMapping("${api.path.tournament.team}")
public class TeamParticipationController {

    @GetMapping("/{teamId}/tournament/{tournamentId}")
    public ResponseEntity<TeamParticipationDTO> get(
            @PathVariable int teamId, @PathVariable int tournamentId
    ) {
        throw new UnsupportedOperationException();
    }

    @PutMapping("/{teamId}/tournament/{tournamentId}")
    public ResponseEntity<Void> update(
            @PathVariable int teamId, @PathVariable int tournamentId, @RequestBody TeamParticipationDTO teamParticipationDTO
    ) {
        throw new UnsupportedOperationException();
    }

    @DeleteMapping("/{teamId}/tournament/{tournamentId}")
    public ResponseEntity<Void> delete(
            @PathVariable int teamId, @PathVariable int tournamentId
    ) {
        throw new UnsupportedOperationException();
    }


    @PutMapping("/{teamId}/tournament/{tournamentId}/player-adding")
    public ResponseEntity<Void> addPlayers(@RequestParam Collection<Integer> playerIds) {
        throw new UnsupportedOperationException();
    }

    @PutMapping("/{teamId}/tournament/{tournamentId}/player-removing")
    public ResponseEntity<Void> removePlayers(@RequestParam Collection<Integer> playerIds) {
        throw new UnsupportedOperationException();
    }
}
