package ru.vldf.sportsportal.dto.sectional.tournament;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.dto.generic.DictionaryDTO;

import javax.validation.constraints.*;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
public class TourBundleStructureDTO implements DictionaryDTO {

    @Null(groups = CodeCheck.class)
    @NotNull(groups = IdCheck.class)
    @Min(value = 1, groups = IdCheck.class)
    private Integer id;

    @NotBlank(groups = CodeCheck.class)
    @Size(min = 1, max = 45, groups = CodeCheck.class)
    private String code;

    @Null(groups = CodeCheck.class)
    private String name;

    @Null(groups = CodeCheck.class)
    private String description;

    @Null(groups = CodeCheck.class)
    private Boolean tourless;

    @Null(groups = CodeCheck.class)
    private Boolean immutable;


    public interface IdCheck {

    }

    public interface CodeCheck {

    }
}
