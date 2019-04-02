package ru.vldf.sportsportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vldf.sportsportal.repository.tournament.TeamRepository;
import ru.vldf.sportsportal.service.generic.AbstractSecurityService;
import ru.vldf.sportsportal.service.tournament.RoundRobinGeneratorService;

import java.util.Collection;

/**
 * @author Namednev Artem
 */
@Service
public class TournamentService extends AbstractSecurityService {

    private final RoundRobinGeneratorService roundRobinGeneratorService;

    private final TeamRepository teamRepository;


    @Autowired
    public TournamentService(
            RoundRobinGeneratorService roundRobinGeneratorService,
            TeamRepository teamRepository
    ) {
        this.roundRobinGeneratorService = roundRobinGeneratorService;
        this.teamRepository = teamRepository;
    }


    @Transactional
    public Integer create(String name, Collection<Integer> teamIds) {
        return roundRobinGeneratorService.create(name, teamRepository.findAllById(teamIds)).getId();
    }
}
