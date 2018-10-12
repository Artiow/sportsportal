package ru.vldf.sportsportal.service.dictionary.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vldf.sportsportal.config.messages.MessageContainer;
import ru.vldf.sportsportal.domain.sectional.common.PictureSizeEntity;
import ru.vldf.sportsportal.dto.sectional.common.PictureSizeDTO;
import ru.vldf.sportsportal.mapper.sectional.common.PictureSizeMapper;
import ru.vldf.sportsportal.repository.common.PictureSizeRepository;
import ru.vldf.sportsportal.service.dictionary.AbstractDictionaryService;

@Service
public class PictureSizeService extends AbstractDictionaryService<PictureSizeEntity, PictureSizeDTO> {

    @Autowired
    public PictureSizeService(MessageContainer messages, PictureSizeRepository repository, PictureSizeMapper mapper) {
        super(messages, repository, mapper);
    }
}
