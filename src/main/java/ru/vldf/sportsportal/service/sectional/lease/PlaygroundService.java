package ru.vldf.sportsportal.service.sectional.lease;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vldf.sportsportal.config.messages.MessageContainer;
import ru.vldf.sportsportal.domain.sectional.lease.PlaygroundEntity;
import ru.vldf.sportsportal.dto.pagination.PageDTO;
import ru.vldf.sportsportal.dto.pagination.filters.generic.PageDividerDTO;
import ru.vldf.sportsportal.dto.sectional.lease.PlaygroundDTO;
import ru.vldf.sportsportal.mapper.sectional.lease.PlaygroundMapper;
import ru.vldf.sportsportal.repository.lease.PlaygroundRepository;
import ru.vldf.sportsportal.service.generic.AbstractCRUDService;
import ru.vldf.sportsportal.service.generic.ResourceNotFoundException;
import ru.vldf.sportsportal.service.generic.ResourceOptimisticLockException;

import javax.persistence.EntityNotFoundException;
import javax.persistence.OptimisticLockException;

@Service
public class PlaygroundService extends AbstractCRUDService<PlaygroundEntity, PlaygroundDTO> {

    private MessageContainer messages;

    private PlaygroundRepository playgroundRepository;
    private PlaygroundMapper playgroundMapper;

    @Autowired
    public void setMessages(MessageContainer messages) {
        this.messages = messages;
    }

    @Autowired
    public void setPlaygroundRepository(PlaygroundRepository playgroundRepository) {
        this.playgroundRepository = playgroundRepository;
    }

    @Autowired
    public void setPlaygroundMapper(PlaygroundMapper playgroundMapper) {
        this.playgroundMapper = playgroundMapper;
    }


    /**
     * Returns requested page with playgrounds.
     *
     * @param pageDividerDTO pagination data
     * @return {@link PageDTO} with {@link PlaygroundDTO}
     */
    @Transactional(readOnly = true)
    public PageDTO<PlaygroundDTO> getList(PageDividerDTO pageDividerDTO) {
        PageDivider divider = new PageDivider(pageDividerDTO); // todo: add filter!
        return new PageDTO<>(playgroundRepository.findAll(divider.getPageRequest()).map(playgroundMapper::toDTO));
    }

    /**
     * Returns requested playground.
     *
     * @param id playground identifier
     * @return {@link PlaygroundDTO}
     * @throws ResourceNotFoundException if singer not found
     */
    @Override
    @Transactional(readOnly = true)
    public PlaygroundDTO get(Integer id) throws ResourceNotFoundException {
        try {
            return playgroundMapper.toDTO(playgroundRepository.getOne(id));
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(messages.getAndFormat("sportsportal.lease.Playground.notExistById.message", id), e);
        }
    }

    /**
     * Saves new playground.
     *
     * @param playgroundDTO playground data
     * @return new playground {@link Integer} identifier
     */
    @Override
    @Transactional
    public Integer create(PlaygroundDTO playgroundDTO) {
        return playgroundRepository.save(playgroundMapper.toEntity(playgroundDTO)).getId();
    }

    /**
     * Update playground data.
     *
     * @param id            playground identifier
     * @param playgroundDTO new playground data
     * @throws ResourceNotFoundException       if playground not found
     * @throws ResourceOptimisticLockException if playground was already updated
     */
    @Override
    @Transactional
    public void update(Integer id, PlaygroundDTO playgroundDTO) throws ResourceNotFoundException, ResourceOptimisticLockException {
        try {
            playgroundRepository.saveAndFlush(playgroundMapper.merge(playgroundRepository.getOne(id), playgroundMapper.toEntity(playgroundDTO)));
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(messages.getAndFormat("sportsportal.lease.Playground.notExistById.message", id));
        } catch (OptimisticLockException | OptimisticLockingFailureException e) {
            throw new ResourceOptimisticLockException(messages.get("sportsportal.lease.Playground.optimisticLock.message"), e);
        }
    }

    /**
     * Delete playground.
     *
     * @param id playground identifier
     * @throws ResourceNotFoundException if playground not found
     */
    @Override
    @Transactional
    public void delete(Integer id) throws ResourceNotFoundException {
        if (!playgroundRepository.existsById(id)) {
            throw new ResourceNotFoundException(messages.getAndFormat("sportsportal.lease.Playground.notExistById.message", id));
        }

        playgroundRepository.deleteById(id);
    }
}
