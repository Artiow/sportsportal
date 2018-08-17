package ru.vldf.sportsportal.service.sectional.lease;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vldf.sportsportal.config.messages.MessageContainer;
import ru.vldf.sportsportal.domain.sectional.lease.PlaygroundEntity;
import ru.vldf.sportsportal.dto.sectional.lease.PlaygroundDTO;
import ru.vldf.sportsportal.mapper.sectional.lease.PlaygroundMapper;
import ru.vldf.sportsportal.repository.lease.PlaygroundRepository;
import ru.vldf.sportsportal.service.generic.*;

@Service
public class PlaygroundService extends AbstractCRUDService<Integer, PlaygroundEntity, PlaygroundDTO> {

    @Autowired
    public void setMessages(MessageContainer messages) {
        super.setMessages(messages);
    }


    @Override
    protected PlaygroundDTO get(Integer id) throws ResourceNotFoundException {
        return null;
    }

    @Override
    protected Integer create(PlaygroundDTO t) throws ResourceCannotCreateException {
        return null;
    }

    @Override
    protected void update(Integer integer, PlaygroundDTO t) throws ResourceNotFoundException, ResourceCannotUpdateException, ResourceOptimisticLockException {

    }

    @Override
    protected void delete(Integer integer) throws ResourceNotFoundException {

    }


    @Override
    protected PlaygroundRepository getRepository() {
        return (PlaygroundRepository) super.getRepository();
    }

    @Autowired
    public void setRepository(PlaygroundRepository repository) {
        super.setRepository(repository);
    }

    @Override
    protected PlaygroundMapper getMapper() {
        return (PlaygroundMapper) super.getMapper();
    }

    @Autowired
    public void setMapper(PlaygroundMapper mapper) {
        super.setMapper(mapper);
    }
}
