package ru.vldf.sportsportal.util.generators;

import org.springframework.util.Assert;
import ru.vldf.sportsportal.domain.sectional.tournament.GameEntity;
import ru.vldf.sportsportal.domain.sectional.tournament.TeamParticipationEntity;
import ru.vldf.sportsportal.domain.sectional.tournament.TourBundleEntity;
import ru.vldf.sportsportal.domain.sectional.tournament.TourEntity;

import java.util.*;

/**
 * @author Namednev Artem
 */
public final class RoundRobinGenerator {

    /**
     * Returns auto-generated tour bundle by <a href="https://en.wikipedia.org/wiki/Round-robin_tournament">round-robin system</a>.
     *
     * @param source the collection of team participations entities.
     * @return new auto-generated tour bundle entity.
     */
    public static TourBundleEntity generateBundle(Collection<TeamParticipationEntity> source) {
        Assert.notEmpty(source, "empty collection");

        // pre-calculating
        List<TeamParticipationEntity> teams = new ArrayList<>(source);
        int toursNum = teams.size() + teams.size() % 2 - 1;
        if (toursNum == teams.size()) {
            // adding a phantom team at the start
            teams.add(0, null);
        }

        // bundle creating
        TourBundleEntity bundle = new TourBundleEntity();
        bundle.setTours(new HashSet<>(toursNum));

        // bundle filling
        for (int offset = 0; offset < toursNum; offset++) {
            TourEntity tour = generateTour(teams, offset);
            bundle.getTours().add(tour);
            tour.setBundle(bundle);
        }

        // result
        return bundle;
    }


    private static TourEntity generateTour(List<TeamParticipationEntity> teams, int offset) {
        // pre-calculating
        int gamesNum = teams.size() / 2;

        // tour creating
        TourEntity tour = new TourEntity();
        tour.setGames(new HashSet<>(gamesNum));

        // tour filling (zero stage)
        if (Objects.nonNull(teams.get(0))) {
            GameEntity game = generateGame(teams, offset);
            tour.getGames().add(game);
            game.setTour(tour);
        }

        // tour filling (other stages)
        for (int stage = 1; stage < gamesNum; stage++) {
            GameEntity game = generateGame(teams, offset, stage);
            tour.getGames().add(game);
            game.setTour(tour);
        }

        // result
        return tour;
    }

    private static GameEntity generateGame(List<TeamParticipationEntity> teams, int offset) {
        return generateGame(teams, offset, 0);
    }

    private static GameEntity generateGame(List<TeamParticipationEntity> teams, int offset, int stage) {
        GameEntity game = new GameEntity();
        game.setRedTeamParticipation(getFrom(teams, offset, (stage)));
        game.setBlueTeamParticipation(getFrom(teams, offset, (teams.size() - stage - 1)));
        return game;
    }

    private static <T> T getFrom(List<T> list, int offset, int index) {
        if (index == 0) {
            return list.get(0);
        } else {
            int size = list.size();
            Assert.isTrue((0 <= index) && (index < size), "invalid index");
            Assert.isTrue((0 <= offset) && (offset < size - 1), "invalid offset");
            int i = index - offset;
            return list.get(i > 0 ? i : i + (list.size() - 1));
        }
    }
}
