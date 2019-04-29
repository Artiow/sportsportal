package ru.vldf.sportsportal.dto.sectional.tournament;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.dto.general.IdentifiedDTO;
import ru.vldf.sportsportal.dto.general.LinkedDTO;
import ru.vldf.sportsportal.dto.sectional.tournament.links.TeamLinkDTO;
import ru.vldf.sportsportal.dto.validation.annotations.Trimmed;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Artem Namednev
 */
@Getter
@Setter
public class TournamentDTO implements IdentifiedDTO {

    @Null(groups = SoftFieldCheck.class)
    @NotNull(groups = IdCheck.class)
    @Min(value = 1, groups = IdCheck.class)
    private Integer id;

    @Trimmed(groups = SoftFieldCheck.class)
    @NotBlank(groups = SoftFieldCheck.class)
    @Size(min = 4, max = 45, groups = SoftFieldCheck.class)
    private String name;

    private LocalDate startDate;

    private LocalDate finishDate;

    private Boolean isCompleted;

    @Valid
    @NotNull(groups = HardFieldCheck.class)
    private TourBundleDTO bundle;

    @Valid
    @NotEmpty(groups = SoftFieldCheck.class)
    private List<TeamLinkDTO> teams;


    public interface IdCheck {

    }

    public interface GenerateCheck extends SoftFieldCheck, LinkedDTO.LinkCheck {

    }

    public interface CreateCheck extends HardFieldCheck {

    }

    public interface UpdateCheck extends HardFieldCheck {

    }

    private interface HardFieldCheck extends SoftFieldCheck {

    }

    private interface SoftFieldCheck {

    }
}
