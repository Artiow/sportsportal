package ru.vldf.sportsportal.dto.pagination.filters;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.dto.pagination.filters.generic.StringSearcherDTO;

import java.time.LocalDate;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
public class PlayerFilterDTO extends StringSearcherDTO {

    private String name;
    private String surname;
    private String patronymic;
    private LocalDate minBirthdate;
    private LocalDate maxBirthdate;
    private Boolean isLocked;
    private Boolean isDisabled;
}
