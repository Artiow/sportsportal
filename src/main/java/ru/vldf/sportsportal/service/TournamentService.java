package ru.vldf.sportsportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vldf.sportsportal.dto.general.AbstractIdentifiedLinkDTO;
import ru.vldf.sportsportal.dto.sectional.tournament.links.TeamLinkDTO;
import ru.vldf.sportsportal.repository.tournament.TeamRepository;
import ru.vldf.sportsportal.service.general.AbstractSecurityService;
import ru.vldf.sportsportal.service.tournament.RoundRobinGeneratorService;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.stream.Collectors;

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
    public Integer create(String name, Collection<TeamLinkDTO> teams) {
        return roundRobinGeneratorService.create(
                name, teamRepository.findAllById(teams.stream().map(AbstractIdentifiedLinkDTO::getId).collect(Collectors.toSet()))
        ).getId();
    }
}
