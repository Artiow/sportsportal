package ru.vldf.sportsportal.dto.sectional.tournament.shortcut;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.dto.general.AbstractIdentifiedShortDTO;

import java.net.URI;
import java.time.LocalDate;

/**
 * @author Artem Namednev
 */
@Getter
@Setter
public class TournamentShortDTO extends AbstractIdentifiedShortDTO {

    private String name;
    private LocalDate startDate;
    private LocalDate finishDate;
    private Boolean isCompleted;
    private Boolean isFixed;
    private URI tournamentURL;
}
