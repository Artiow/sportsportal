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

    @NotNull(groups = IdCheck.class)
    @Min(value = 1, groups = IdCheck.class)
    @Null(groups = {FieldGr1Check.class, FieldGr2Check.class})
    private Integer id;

    @Trimmed(groups = {FieldGr1Check.class, FieldGr2Check.class})
    @NotBlank(groups = {FieldGr1Check.class, FieldGr2Check.class})
    @Size(min = 4, max = 45, groups = {FieldGr1Check.class, FieldGr2Check.class})
    private String name;

    private LocalDate startDate;

    private LocalDate finishDate;

    private Boolean isCompleted;

    private Boolean isFixed;

    private TourBundleDTO bundle;

    @Valid
    @NotEmpty(groups = FieldGr1Check.class)
    @Null(groups = FieldGr2Check.class)
    private List<TeamLinkDTO> teams;


    public interface IdCheck {

    }

    public interface GenerateCheck extends FieldGr1Check {

    }

    public interface CreateCheck extends FieldGr2Check {

    }

    public interface UpdateCheck extends FieldGr2Check {

    }

    private interface FieldGr1Check extends LinkedDTO.LinkCheck {

    }

    private interface FieldGr2Check {

    }
}
