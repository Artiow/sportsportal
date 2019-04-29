package ru.vldf.sportsportal.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Artem Namednev
 */
@RestController
@Api(tags = {"Team Participation"})
@RequestMapping("${api.path.tournament.team}")
public class TeamParticipationController {

}
