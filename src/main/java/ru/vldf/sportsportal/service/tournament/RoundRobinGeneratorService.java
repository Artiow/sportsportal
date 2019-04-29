package ru.vldf.sportsportal.service.tournament;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.vldf.sportsportal.domain.sectional.tournament.*;
import ru.vldf.sportsportal.repository.tournament.TourBundleStructureRepository;
import ru.vldf.sportsportal.repository.tournament.TourBundleTypeRepository;
import ru.vldf.sportsportal.util.generators.RoundRobinGenerator;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Namednev Artem
 */
@Service
public class RoundRobinGeneratorService {

    private final TourBundleTypeRepository tourBundleTypeRepository;
    private final TourBundleStructureRepository tourBundleStructureRepository;


    @Value("${code.tour-bundle.type.tournament}")
    private String typeCode;

    @Value("${code.tour-bundle.structure.round-robin}")
    private String structureCode;

    @Value("${template.tour-bundle.label.round-robin}")
    private String template;


    @Autowired
    public RoundRobinGeneratorService(
            TourBundleTypeRepository tourBundleTypeRepository,
            TourBundleStructureRepository tourBundleStructureRepository
    ) {
        this.tourBundleTypeRepository = tourBundleTypeRepository;
        this.tourBundleStructureRepository = tourBundleStructureRepository;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public TournamentEntity create(Collection<TeamEntity> teams) {
        TourBundleEntity bundle = generate(teams);
        bundle.setBundleStructure(structure());
        bundle.setBundleType(type());
        return bundle.getTournament();
    }


    private TourBundleEntity generate(Collection<TeamEntity> teams) {
        TournamentEntity tournament = new TournamentEntity();
        TourBundleEntity bundle = RoundRobinGenerator.generateBundle(participationFrom(tournament, teams), template);
        bundle.setTournament(tournament);
        tournament.setBundle(bundle);
        return bundle;
    }


    private Set<TeamParticipationEntity> participationFrom(TournamentEntity tournament, Collection<TeamEntity> teams) {
        return teams.stream().map(team -> participationFrom(tournament, team)).collect(Collectors.toSet());
    }

    private TeamParticipationEntity participationFrom(TournamentEntity tournament, TeamEntity team) {
        TeamParticipationEntity participation = new TeamParticipationEntity();
        participation.setParticipationName(team.getName());
        participation.setTournament(tournament);
        participation.setTeam(team);
        return participation;
    }


    private TourBundleTypeEntity type() {
        // noinspection OptionalGetWithoutIsPresent
        return tourBundleTypeRepository.findByCode(typeCode).get();
    }

    private TourBundleStructureEntity structure() {
        // noinspection OptionalGetWithoutIsPresent
        return tourBundleStructureRepository.findByCode(structureCode).get();
    }
}
