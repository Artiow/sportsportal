package ru.vldf.sportsportal.service.dictionary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ru.vldf.sportsportal.domain.generic.AbstractDictionaryEntity;
import ru.vldf.sportsportal.dto.generic.DictionaryDTO;
import ru.vldf.sportsportal.dto.pagination.PageDTO;
import ru.vldf.sportsportal.dto.pagination.filters.generic.PageDividerDTO;
import ru.vldf.sportsportal.mapper.generic.AbstractDictionaryMapper;
import ru.vldf.sportsportal.repository.AbstractWordbookRepository;
import ru.vldf.sportsportal.service.generic.AbstractMessageService;
import ru.vldf.sportsportal.service.generic.ResourceNotFoundException;

import javax.persistence.EntityNotFoundException;

/**
 * @author Namednev Artem
 */
public abstract class AbstractDictionaryService<E extends AbstractDictionaryEntity, D extends DictionaryDTO> extends AbstractMessageService implements DictionaryService<E, D> {

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private AbstractWordbookRepository<E> repository;

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private AbstractDictionaryMapper<E, D> mapper;


    @Override
    @Transactional(readOnly = true)
    public D get(Integer id) throws ResourceNotFoundException {
        try {
            return mapper.toDTO(repository.getOne(id));
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(msg("sportsportal.dictionary.notExistById.message", id), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public D get(String code) throws ResourceNotFoundException {
        try {
            return mapper.toDTO(repository.findByCode(code));
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(msg("sportsportal.dictionary.notExistByCode.message", code), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PageDTO<D> getList(PageDividerDTO pageDividerDTO) {
        return PageDTO.from(repository.findAll(new PageDivider(pageDividerDTO).getPageRequest()).map(mapper::toDTO));
    }

    @Override
    @Transactional
    public Integer create(D t) {
        throw new UnsupportedOperationException(msg("sportsportal.handle.UnsupportedOperationException.message", "create"));
    }

    @Override
    @Transactional
    public void update(Integer id, D t) {
        throw new UnsupportedOperationException(msg("sportsportal.handle.UnsupportedOperationException.message", "update"));
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        throw new UnsupportedOperationException(msg("sportsportal.handle.UnsupportedOperationException.message", "delete"));
    }
}
