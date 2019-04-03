package ru.vldf.sportsportal.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Namednev Artem
 */
@RestController
@Api(tags = {"Tournament"})
@RequestMapping("${api.path.tournament.tournament}")
public class TournamentController {

}
