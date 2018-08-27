package ru.vldf.sportsportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vldf.sportsportal.config.messages.MessageContainer;
import ru.vldf.sportsportal.domain.sectional.lease.*;
import ru.vldf.sportsportal.dto.pagination.PageDTO;
import ru.vldf.sportsportal.dto.pagination.filters.generic.PageDividerDTO;
import ru.vldf.sportsportal.dto.sectional.lease.PlaygroundDTO;
import ru.vldf.sportsportal.dto.sectional.lease.shortcut.PlaygroundShortDTO;
import ru.vldf.sportsportal.dto.sectional.lease.specialized.PlaygroundGridDTO;
import ru.vldf.sportsportal.mapper.sectional.lease.PlaygroundMapper;
import ru.vldf.sportsportal.repository.lease.PlaygroundRepository;
import ru.vldf.sportsportal.repository.lease.ReservationRepository;
import ru.vldf.sportsportal.service.generic.AbstractCRUDService;
import ru.vldf.sportsportal.service.generic.ResourceNotFoundException;
import ru.vldf.sportsportal.service.generic.ResourceOptimisticLockException;

import javax.persistence.EntityNotFoundException;
import javax.persistence.OptimisticLockException;
import javax.persistence.criteria.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class PlaygroundService extends AbstractCRUDService<PlaygroundEntity, PlaygroundDTO> {

    private MessageContainer messages;

    private ReservationRepository reservationRepository;

    private PlaygroundRepository playgroundRepository;
    private PlaygroundMapper playgroundMapper;

    @Autowired
    public void setMessages(MessageContainer messages) {
        this.messages = messages;
    }

    @Autowired
    public void setReservationRepository(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
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
     * @param dividerDTO {@link PageDividerDTO} pagination data
     * @return {@link PageDTO} with {@link PlaygroundShortDTO}
     */
    @Transactional(readOnly = true)
    public PageDTO<PlaygroundShortDTO> getList(PageDividerDTO dividerDTO) {
        PageDivider divider = new PageDivider(dividerDTO); // todo: set filter here!
        return new PageDTO<>(playgroundRepository.findAll(divider.getPageRequest()).map(playgroundMapper::toShortDTO));
    }

    /**
     * Returns requested playground with time grid info.
     *
     * @param id        {@link Integer} playground identifier
     * @param startDate {@link LocalDate} first date of grid
     * @param endDate   {@link LocalDate} last date of grid
     * @return {@link PlaygroundGridDTO}
     * @throws ResourceNotFoundException if playground not found
     */
    @Transactional(readOnly = true)
    public PlaygroundGridDTO getGrid(Integer id, LocalDate startDate, LocalDate endDate) throws ResourceNotFoundException {
        try {
            return playgroundMapper.setGrid(
                    playgroundMapper.toGridDTO(playgroundRepository.getOne(id)), startDate, endDate,
                    reservationRepository.findAll(new ReservationFilter(id, startDate, endDate))
            );
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(messages.getAndFormat("sportsportal.lease.Playground.notExistById.message", id), e);
        }
    }

    /**
     * Returns requested playground.
     *
     * @param id {@link Integer} playground identifier
     * @return {@link PlaygroundDTO}
     * @throws ResourceNotFoundException if playground not found
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
     * @param playgroundDTO {@link PlaygroundDTO} with playground data
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
     * @param id            {@link Integer} playground identifier
     * @param playgroundDTO {@link PlaygroundDTO} with new playground data
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
     * @param id {@link Integer} playground identifier
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


    public static class ReservationFilter implements Specification<ReservationEntity> {

        private Integer playgroundId;
        private Timestamp start;
        private Timestamp end;


        public ReservationFilter(Integer playgroundId, LocalDate startDate, LocalDate endDate) {
            this.playgroundId = playgroundId;
            if (startDate.isBefore(endDate)) {
                this.start = toTimestamp(startDate);
                this.end = toTimestamp(endDate);
            } else {
                this.start = toTimestamp(endDate);
                this.end = toTimestamp(startDate);
            }
        }


        private Timestamp toTimestamp(LocalDate localDate) {
            return Timestamp.valueOf(LocalDateTime.of(localDate, LocalTime.MIN));
        }


        @Override
        public Predicate toPredicate(Root<ReservationEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            Path<Integer> playgroundId = root.get(ReservationEntity_.pk).get(ReservationEntityPK_.order).get(OrderEntity_.playground).get(PlaygroundEntity_.id);
            Path<Timestamp> datetime = root.get(ReservationEntity_.pk).get(ReservationEntityPK_.datetime);
            return query.where(cb.and(
                    cb.equal(playgroundId, this.playgroundId),
                    cb.greaterThanOrEqualTo(datetime, start),
                    cb.lessThanOrEqualTo(datetime, end)
            )).distinct(true).getRestriction();
        }
    }
}
