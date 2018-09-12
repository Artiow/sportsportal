package ru.vldf.sportsportal.service.dictionary.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vldf.sportsportal.domain.sectional.common.RoleEntity;
import ru.vldf.sportsportal.dto.sectional.common.RoleDTO;
import ru.vldf.sportsportal.mapper.sectional.common.RoleMapper;
import ru.vldf.sportsportal.repository.common.RoleRepository;
import ru.vldf.sportsportal.service.dictionary.AbstractDictionaryService;

@Service
public class RoleService extends AbstractDictionaryService<RoleEntity, RoleDTO> {

    @Autowired
    public RoleService(RoleRepository repository, RoleMapper mapper) {
        super(repository, mapper);
    }
}
