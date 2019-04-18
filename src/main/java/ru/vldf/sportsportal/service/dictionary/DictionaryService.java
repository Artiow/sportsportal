package ru.vldf.sportsportal.service.dictionary;

import ru.vldf.sportsportal.domain.general.AbstractDictionaryEntity;
import ru.vldf.sportsportal.dto.general.DictionaryDTO;
import ru.vldf.sportsportal.dto.pagination.PageDTO;
import ru.vldf.sportsportal.dto.pagination.filters.generic.PageDividerDTO;
import ru.vldf.sportsportal.service.general.CRUDService;
import ru.vldf.sportsportal.service.general.throwable.AbstractAuthorizationException;
import ru.vldf.sportsportal.service.general.throwable.AbstractResourceException;

/**
 * @author Namednev Artem
 */
public interface DictionaryService<E extends AbstractDictionaryEntity, D extends DictionaryDTO> extends CRUDService<E, D> {

    D get(String code) throws AbstractAuthorizationException, AbstractResourceException;

    PageDTO<D> getList(PageDividerDTO pageDividerDTO);
}
