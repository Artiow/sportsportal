package ru.vldf.sportsportal.service.dictionary.lease;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vldf.sportsportal.domain.sectional.lease.SportEntity;
import ru.vldf.sportsportal.dto.sectional.lease.SportDTO;
import ru.vldf.sportsportal.mapper.sectional.lease.SportMapper;
import ru.vldf.sportsportal.repository.lease.SportRepository;
import ru.vldf.sportsportal.service.dictionary.AbstractDictionaryService;

@Service
public class SportService extends AbstractDictionaryService<SportEntity, SportDTO> {

    @Autowired
    public SportService(SportRepository repository, SportMapper mapper) {
        super(repository, mapper);
    }
}
