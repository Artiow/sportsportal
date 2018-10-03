package ru.vldf.sportsportal.service.dictionary;

import ru.vldf.sportsportal.domain.generic.AbstractDictionaryEntity;
import ru.vldf.sportsportal.dto.generic.AbstractDictionaryDTO;
import ru.vldf.sportsportal.dto.pagination.PageDTO;
import ru.vldf.sportsportal.dto.pagination.filters.generic.PageDividerDTO;
import ru.vldf.sportsportal.service.generic.AbstractCRUDService;
import ru.vldf.sportsportal.service.generic.ResourceNotFoundException;

public interface AbstractDictionaryCRUDService<E extends AbstractDictionaryEntity, D extends AbstractDictionaryDTO> extends AbstractCRUDService<E, D> {

    D get(String code)
            throws ResourceNotFoundException;

    PageDTO<D> getList(PageDividerDTO pageDividerDTO);
}
