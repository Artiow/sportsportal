package ru.vldf.sportsportal.service.generic;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import ru.vldf.sportsportal.domain.generic.DomainObject;
import ru.vldf.sportsportal.dto.generic.DataTransferObject;
import ru.vldf.sportsportal.dto.pagination.filters.generic.PageDividerDTO;
import ru.vldf.sportsportal.dto.pagination.filters.generic.StringSearcherDTO;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

public abstract class AbstractCRUDService<T extends DataTransferObject> {

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


    public static class StringSearcher<T extends DomainObject> extends PageDivider implements Specification<T> {

        private String searchString;
        private SingularAttribute<? super T, String> attribute;


        public StringSearcher(StringSearcherDTO dto, SingularAttribute<? super T, String> attribute) {
            super(dto);

            this.attribute = attribute;
            configureSearchByString(dto);
        }

        private void configureSearchByString(StringSearcherDTO dto) {
            String searchString = dto.getSearchString();
            if ((searchString != null) && (!searchString.equals(""))) {
                this.searchString = searchString.trim().toLowerCase() + "%";
            }
        }


        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            if (searchString != null) {
                return searchByStringPredicate(root, query, cb);
            } else {
                return null;
            }
        }

        private Predicate searchByStringPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            return cb.like(cb.lower(root.get(attribute)), searchString);
        }
    }


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
