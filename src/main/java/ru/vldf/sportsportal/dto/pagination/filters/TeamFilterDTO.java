package ru.vldf.sportsportal.dto.pagination.filters;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.dto.pagination.filters.generic.StringSearcherDTO;

import java.util.Collection;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
public class TeamFilterDTO extends StringSearcherDTO {

    private Boolean isLocked;
    private Boolean isDisabled;
    private Collection<Integer> captainsIds;
    private Collection<Integer> mainCaptainsIds;
    private Collection<Integer> viceCaptainsIds;
}
