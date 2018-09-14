package ru.vldf.sportsportal.service.generic;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import ru.vldf.sportsportal.domain.generic.AbstractIdentifiedEntity;
import ru.vldf.sportsportal.domain.generic.DomainObject;
import ru.vldf.sportsportal.dto.generic.AbstractIdentifiedDTO;
import ru.vldf.sportsportal.dto.pagination.filters.generic.PageDividerDTO;
import ru.vldf.sportsportal.dto.pagination.filters.generic.StringSearcherDTO;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import java.util.ArrayList;
import java.util.Collection;

public interface AbstractCRUDService<E extends AbstractIdentifiedEntity, D extends AbstractIdentifiedDTO> {

    D get(Integer id) throws
            ResourceNotFoundException;

    Integer create(D t) throws
            ResourceCannotCreateException;

    void update(Integer id, D t) throws
            ResourceNotFoundException,
            ResourceCannotUpdateException,
            ResourceOptimisticLockException;

    void delete(Integer id) throws
            ResourceNotFoundException;


    class StringSearcher<E extends DomainObject> extends PageDivider implements Specification<E> {

        private String[] searchWords;
        private SingularAttribute<? super E, String> attribute;


        public StringSearcher(StringSearcherDTO dto, SingularAttribute<? super E, String> attribute) {
            super(dto);

            this.attribute = attribute;
            configureSearchByString(dto);
        }

        private void configureSearchByString(StringSearcherDTO dto) {
            String searchString = dto.getSearchString();
            if (searchString != null) {
                searchString = searchString.trim();
                if (!searchString.equals("")) {
                    this.searchWords = searchString.toLowerCase().split(" ");
                }
            }
        }


        @Override
        public Predicate toPredicate(Root<E> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            if (searchWords != null) {
                return searchByStringPredicate(root, query, cb);
            } else {
                return null;
            }
        }

        private Predicate searchByStringPredicate(Root<E> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            Collection<Predicate> occurrences = new ArrayList<>();
            for (String searchWord : searchWords) {
                occurrences.add(cb.like(cb.lower(root.get(attribute)), ("%" + searchWord + "%")));
            }
            return cb.and(occurrences.toArray(new Predicate[0]));
        }
    }


    class PageDivider {

        private Integer LIMIT = 120;
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
                    throw new IllegalArgumentException("Page size must not be null! If you want to set the default value, set page index to null also.");
                } else if (pageNum == null) {
                    throw new IllegalArgumentException("Page index must not be null! If you want to set the default value, set page size to null also.");
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
