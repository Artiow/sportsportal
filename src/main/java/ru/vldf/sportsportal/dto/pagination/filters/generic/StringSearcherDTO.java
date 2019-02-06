package ru.vldf.sportsportal.dto.pagination.filters.generic;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
public class StringSearcherDTO extends PageDividerDTO {

    private String searchString;
}
