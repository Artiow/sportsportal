package ru.vldf.sportsportal.dto.sectional.tournament;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.dto.general.IdentifiedDTO;
import ru.vldf.sportsportal.dto.validation.annotations.Trimmed;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Artem Namednev
 */
@Getter
@Setter
public class TourBundleDTO implements IdentifiedDTO {

    @NotNull(groups = IdCheck.class)
    @Min(value = 1, groups = IdCheck.class)
    private Integer id;

    @Trimmed(groups = FieldCheck.class)
    @NotBlank(groups = FieldCheck.class)
    @Size(min = 4, max = 45, groups = FieldCheck.class)
    private String textLabel;

    @NotNull(groups = FieldCheck.class)
    @Min(value = 0, groups = FieldCheck.class)
    private Integer numericLabel;

    @NotNull(groups = FieldCheck.class)
    private Boolean isCompleted;


    public interface IdCheck {

    }

    public interface CreateCheck extends FieldCheck {

    }

    public interface UpdateCheck extends FieldCheck {

    }

    private interface FieldCheck {

    }
}
