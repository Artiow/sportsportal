package ru.vldf.sportsportal.service.generic;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.vldf.sportsportal.dto.generic.DataTransferObject;
import ru.vldf.sportsportal.dto.pagination.PageDividerDTO;

public abstract class AbstractService<T extends DataTransferObject> {

    public abstract T get(Integer id) throws
            ResourceNotFoundException;

    public abstract Integer create(T t) throws
            ResourceCannotCreateException;

    public abstract void update(Integer id, T t) throws
            ResourceNotFoundException,
            ResourceCannotUpdateException,
            ResourceOptimisticLockException;

    public abstract void delete(Integer id) throws
            ResourceNotFoundException;


    public static class PageDivider {

        private Integer LIMIT = 150;
        private Integer pageSize;
        private Integer pageNum;

        public PageDivider(PageDividerDTO dto) {
            Integer pageSize = dto.getPageSize();
            Integer pageNum = dto.getPageNum();

            Boolean def = ((pageSize == null) && (pageNum == null));
            if ((!def) && (pageSize != null) && (pageNum != null)) {
                def = ((pageSize == 0) && (pageNum == 0));
            }

            // todo: getting messages from message container?
            if (!def) {
                if (pageSize == null) {
                    throw new IllegalArgumentException("Page size must not be null! If you want to set the default value, set \"pageNum\" to null also.");
                } else if (pageNum == null) {
                    throw new IllegalArgumentException("Page index must not be null! If you want to set the default value, set \"pageSize\" to null also.");
                } else if (pageSize > LIMIT) {
                    throw new IllegalArgumentException("Page size must not be more than the limit value! ");
                } else if (pageSize < 1) {
                    throw new IllegalArgumentException("Page size must not be less than one! ");
                } else if (pageNum < 0) {
                    throw new IllegalArgumentException("Page index must not be less than zero!");
                }

                this.pageSize = pageSize;
                this.pageNum = pageNum;
            } else {
                this.pageSize = LIMIT;
                this.pageNum = 0;
            }
        }

        public Pageable getPageRequest() {
            if (pageSize == null) {
                return Pageable.unpaged();
            }

            return PageRequest.of(pageNum, pageSize);
        }
    }
}
