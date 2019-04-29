package ru.vldf.sportsportal.service.general;

import io.jsonwebtoken.lang.Assert;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.vldf.sportsportal.domain.general.AbstractIdentifiedEntity;
import ru.vldf.sportsportal.domain.general.root.DomainObject;
import ru.vldf.sportsportal.dto.general.IdentifiedDTO;
import ru.vldf.sportsportal.dto.pagination.filters.generic.PageDividerDTO;
import ru.vldf.sportsportal.dto.pagination.filters.generic.StringSearcherDTO;
import ru.vldf.sportsportal.service.general.throwable.AbstractAuthorizationException;
import ru.vldf.sportsportal.service.general.throwable.AbstractResourceException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Namednev Artem
 */
public interface CRUDService<E extends AbstractIdentifiedEntity, D extends IdentifiedDTO> {

    D get(Integer id) throws AbstractAuthorizationException, AbstractResourceException;

    Integer create(D t) throws AbstractAuthorizationException, AbstractResourceException, MethodArgumentNotValidException;

    void update(Integer id, D t) throws AbstractAuthorizationException, AbstractResourceException, MethodArgumentNotValidException;

    void delete(Integer id) throws AbstractAuthorizationException, AbstractResourceException;


    class StringSearcher<E extends DomainObject> extends PageDivider implements Specification<E> {

        private String[] searchWords;
        private SingularAttribute<? super E, String>[] attributes;


        @SafeVarargs
        public StringSearcher(StringSearcherDTO dto, SingularAttribute<? super E, String>... attributes) {
            super(dto);
            Assert.notEmpty(attributes);
            this.attributes = attributes;
            configureSearchByString(dto);
        }

        private void configureSearchByString(StringSearcherDTO dto) {
            this.searchWords = StringUtils.hasText(dto.getSearchString()) ? dto.getSearchString().trim().toLowerCase().split(" ") : null;
        }

        @Override
        public Predicate toPredicate(Root<E> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            return searchWords != null ? searchByStringPredicate(root, cb) : null;
        }

        protected List<Predicate> toPredicateList(Root<E> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            List<Predicate> list = new ArrayList<>();
            Predicate element = toPredicate(root, query, cb);
            if (element != null) list.add(element);
            return list;
        }

        private Predicate searchByStringPredicate(Root<E> root, CriteriaBuilder cb) {
            return cb.and(Stream.of(searchWords).map(
                    word -> cb.or(Stream.of(attributes).map(
                            attribute -> cb.like(cb.lower(root.get(attribute)), ("%" + word + "%"))
                    ).toArray(Predicate[]::new))
            ).toArray(Predicate[]::new));
        }
    }


    class PageDivider {

        private Integer LIMIT = 120;
        private Integer pageSize;
        private Integer pageNum;

        public PageDivider(PageDividerDTO dto) {
            Integer pageSize = dto.getPageSize();
            Integer pageNum = dto.getPageNum();

            boolean def = ((pageSize == null) && (pageNum == null));
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
