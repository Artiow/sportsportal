package ru.vldf.sportsportal.service.dictionary.lease;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vldf.sportsportal.domain.sectional.lease.FeatureEntity;
import ru.vldf.sportsportal.dto.sectional.lease.FeatureDTO;
import ru.vldf.sportsportal.mapper.sectional.lease.FeatureMapper;
import ru.vldf.sportsportal.repository.lease.FeatureRepository;
import ru.vldf.sportsportal.service.dictionary.AbstractDictionaryService;

@Service
public class FeatureService extends AbstractDictionaryService<FeatureEntity, FeatureDTO> {

    @Autowired
    public FeatureService(FeatureRepository repository, FeatureMapper mapper) {
        super(repository, mapper);
    }
}
