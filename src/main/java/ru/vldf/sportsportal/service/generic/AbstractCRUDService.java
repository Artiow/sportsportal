package ru.vldf.sportsportal.service.generic;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import ru.vldf.sportsportal.config.messages.MessageContainer;
import ru.vldf.sportsportal.domain.generic.DomainObject;
import ru.vldf.sportsportal.dto.generic.DataTransferObject;
import ru.vldf.sportsportal.dto.pagination.filters.generic.PageDividerDTO;
import ru.vldf.sportsportal.dto.pagination.filters.generic.StringSearcherDTO;
import ru.vldf.sportsportal.mapper.generic.AbstractMapper;
import ru.vldf.sportsportal.repository.AbstractRepository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import java.io.Serializable;

public abstract class AbstractCRUDService<ID extends Serializable, E extends DomainObject, D extends DataTransferObject> {

    private MessageContainer messages;

    private AbstractRepository<E, ID> repository;
    private AbstractMapper<E, D> mapper;


    protected MessageContainer getMessages() {
        return messages;
    }

    protected void setMessages(MessageContainer messages) {
        this.messages = messages;
    }

    protected AbstractRepository<E, ID> getAbstractRepository() {
        return repository;
    }

    protected abstract <T extends AbstractRepository<E, ID>> T getRepository();

    protected <T extends AbstractRepository<E, ID>> void setRepository(T repository) {
        this.repository = repository;
    }

    protected AbstractMapper<E, D> getAbstractMapper() {
        return mapper;
    }

    protected abstract <T extends AbstractMapper<E, D>> T getMapper();

    protected <T extends AbstractMapper<E, D>> void setMapper(T mapper) {
        this.mapper = mapper;
    }


    protected abstract D get(Integer id) throws
            ResourceNotFoundException;

    protected abstract ID create(D t) throws
            ResourceCannotCreateException;

    protected abstract void update(ID id, D t) throws
            ResourceNotFoundException,
            ResourceCannotUpdateException,
            ResourceOptimisticLockException;

    protected abstract void delete(ID id) throws
            ResourceNotFoundException;


    public static class StringSearcher<E extends DomainObject> extends PageDivider implements Specification<E> {

        private String searchString;
        private SingularAttribute<? super E, String> attribute;


        public StringSearcher(StringSearcherDTO dto, SingularAttribute<? super E, String> attribute) {
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
        public Predicate toPredicate(Root<E> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            if (searchString != null) {
                return searchByStringPredicate(root, query, cb);
            } else {
                return null;
            }
        }

        private Predicate searchByStringPredicate(Root<E> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
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
