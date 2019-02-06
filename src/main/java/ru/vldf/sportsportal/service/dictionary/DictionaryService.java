package ru.vldf.sportsportal.service.dictionary;

import ru.vldf.sportsportal.domain.generic.AbstractDictionaryEntity;
import ru.vldf.sportsportal.dto.generic.DictionaryDTO;
import ru.vldf.sportsportal.dto.pagination.PageDTO;
import ru.vldf.sportsportal.dto.pagination.filters.generic.PageDividerDTO;
import ru.vldf.sportsportal.service.generic.AbstractAuthorizationException;
import ru.vldf.sportsportal.service.generic.AbstractResourceException;
import ru.vldf.sportsportal.service.generic.CRUDService;

/**
 * @author Namednev Artem
 */
public interface DictionaryService<E extends AbstractDictionaryEntity, D extends DictionaryDTO> extends CRUDService<E, D> {

    D get(String code) throws AbstractAuthorizationException, AbstractResourceException;

    PageDTO<D> getList(PageDividerDTO pageDividerDTO);
}
