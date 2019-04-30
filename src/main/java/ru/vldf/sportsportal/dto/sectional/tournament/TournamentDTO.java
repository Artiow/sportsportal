package ru.vldf.sportsportal.dto.sectional.tournament;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.dto.general.IdentifiedDTO;
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

    @Null(groups = BasicCheck.class)
    @NotNull(groups = IdCheck.class)
    @Min(value = 1, groups = IdCheck.class)
    private Integer id;

    @Trimmed(groups = BasicCheck.class)
    @NotBlank(groups = BasicCheck.class)
    @Size(min = 4, max = 45, groups = BasicCheck.class)
    private String name;

    private LocalDate startDate;

    private LocalDate finishDate;

    private Boolean isCompleted;

    private Boolean isFixed;

    @Valid
    @Null(groups = FieldCheck.class)
    @NotEmpty(groups = GenerateCheck.class)
    private List<TeamLinkDTO> teams;


    public interface IdCheck {

    }

    public interface GenerateCheck extends BasicCheck {

    }

    public interface CreateCheck extends FieldCheck {

    }

    public interface UpdateCheck extends FieldCheck {

    }

    private interface FieldCheck extends BasicCheck {

    }

    private interface BasicCheck {

    }
}
