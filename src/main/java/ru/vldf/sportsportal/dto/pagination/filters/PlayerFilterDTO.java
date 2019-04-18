package ru.vldf.sportsportal.dto.pagination.filters;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.dto.pagination.filters.generic.StringSearcherDTO;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
public class PlayerFilterDTO extends StringSearcherDTO {

    private Boolean isLocked;
    private Boolean isDisabled;
}
