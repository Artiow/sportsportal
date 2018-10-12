package ru.vldf.sportsportal.service.dictionary.lease;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vldf.sportsportal.config.messages.MessageContainer;
import ru.vldf.sportsportal.domain.sectional.lease.SportEntity;
import ru.vldf.sportsportal.dto.sectional.lease.SportDTO;
import ru.vldf.sportsportal.mapper.sectional.lease.SportMapper;
import ru.vldf.sportsportal.repository.lease.SportRepository;
import ru.vldf.sportsportal.service.dictionary.AbstractDictionaryService;

@Service
public class SportService extends AbstractDictionaryService<SportEntity, SportDTO> {

    @Autowired
    public SportService(MessageContainer messages, SportRepository repository, SportMapper mapper) {
        super(messages, repository, mapper);
    }
}
