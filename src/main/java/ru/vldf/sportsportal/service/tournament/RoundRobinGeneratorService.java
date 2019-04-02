package ru.vldf.sportsportal.service.tournament;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.vldf.sportsportal.domain.sectional.tournament.*;
import ru.vldf.sportsportal.repository.tournament.TourBundleStructureRepository;
import ru.vldf.sportsportal.repository.tournament.TourBundleTypeRepository;
import ru.vldf.sportsportal.repository.tournament.TournamentRepository;
import ru.vldf.sportsportal.util.generators.RoundRobinGenerator;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author Namednev Artem
 */
@Service
public class RoundRobinGeneratorService {

    private final TourBundleTypeRepository tourBundleTypeRepository;
    private final TourBundleStructureRepository tourBundleStructureRepository;

    private final TournamentRepository tournamentRepository;


    @Autowired
    public RoundRobinGeneratorService(
            TourBundleTypeRepository tourBundleTypeRepository,
            TourBundleStructureRepository tourBundleStructureRepository,
            TournamentRepository tournamentRepository
    ) {
        this.tourBundleTypeRepository = tourBundleTypeRepository;
        this.tourBundleStructureRepository = tourBundleStructureRepository;
        this.tournamentRepository = tournamentRepository;
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public TournamentEntity create(String name, Collection<TeamEntity> teams) {
        TournamentEntity tournament = generate(teams);
        tournament.getBundle().setBundleStructure(structure());
        tournament.getBundle().setBundleType(type());
        tournament.getBundle().setTextLabel(name);
        tournament.getBundle().setNumericLabel(0);
        return tournamentRepository.save(tournament);
    }


    private TournamentEntity generate(Collection<TeamEntity> teams) {
        TournamentEntity tournament = new TournamentEntity();
        tournament.setBundle(RoundRobinGenerator.generateBundle(teams.stream().map(team -> participationFrom(tournament, team)).collect(Collectors.toSet())));
        tournament.getBundle().setTournament(tournament);
        return tournament;
    }

    private TeamParticipationEntity participationFrom(TournamentEntity tournament, TeamEntity team) {
        TeamParticipationEntity participation = new TeamParticipationEntity();
        participation.setParticipationName(team.getName());
        participation.setTournament(tournament);
        participation.setTeam(team);
        return participation;
    }


    private TourBundleTypeEntity type() {
        // todo: вынести в конфиги
        // noinspection OptionalGetWithoutIsPresent
        return tourBundleTypeRepository.findByCode("tournament").get();
    }

    private TourBundleStructureEntity structure() {
        // todo: вынести в конфиги
        // noinspection OptionalGetWithoutIsPresent
        return tourBundleStructureRepository.findByCode("round-robin").get();
    }
}
