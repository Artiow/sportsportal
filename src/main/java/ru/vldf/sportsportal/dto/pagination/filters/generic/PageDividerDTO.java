package ru.vldf.sportsportal.dto.pagination.filters.generic;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.dto.generic.DataTransferObject;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
public class PageDividerDTO implements DataTransferObject {

    private Integer pageSize;
    private Integer pageNum;
}
