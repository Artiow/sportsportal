package ru.vldf.sportsportal.dto.pagination;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import ru.vldf.sportsportal.dto.generic.DataTransferObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
public class PageDTO<T> implements DataTransferObject {

    private List<T> content;
    private Integer pageNumber;
    private Integer numberOfElements;
    private Long totalElements;
    private Integer totalPages;

    private PageDTO(Page<T> page) {
        this.content = new ArrayList<>(page.getContent());
        this.pageNumber = page.getNumber();
        this.numberOfElements = page.getNumberOfElements();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
    }

    public static <T> PageDTO<T> from(Page<T> page) {
        return new PageDTO<>(page);
    }
}
