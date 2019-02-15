package ru.vldf.sportsportal.dto.pagination.filters;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.dto.pagination.filters.generic.StringSearcherDTO;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Collection;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
public class PlaygroundFilterDTO extends StringSearcherDTO {

    private Collection<String> featureCodes;
    private Collection<String> sportCodes;
    private BigDecimal startPrice;
    private BigDecimal endPrice;
    private LocalTime opening;
    private LocalTime closing;
    private Integer minRate;
}
