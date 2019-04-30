package ru.vldf.sportsportal.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Artem Namednev
 */
@RestController
@Api(tags = {"Team Participation"})
@RequestMapping("${api.path.tournament.team}")
public class TeamParticipationController {

    @GetMapping("/{id}/tournament/player/list")
    public void getList() {
        throw new UnsupportedOperationException();
    }

    @PutMapping("/{id}/tournament/player/list")
    public void updateList() {
        throw new UnsupportedOperationException();
    }
}
