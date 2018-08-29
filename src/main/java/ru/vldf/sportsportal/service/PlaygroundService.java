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
import ru.vldf.sportsportal.dto.sectional.lease.specialized.ReservationListDTO;
import ru.vldf.sportsportal.mapper.sectional.lease.PlaygroundMapper;
import ru.vldf.sportsportal.repository.common.RoleRepository;
import ru.vldf.sportsportal.repository.common.UserRepository;
import ru.vldf.sportsportal.repository.lease.OrderRepository;
import ru.vldf.sportsportal.repository.lease.PlaygroundRepository;
import ru.vldf.sportsportal.repository.lease.ReservationRepository;
import ru.vldf.sportsportal.service.generic.*;

import javax.persistence.EntityNotFoundException;
import javax.persistence.OptimisticLockException;
import javax.persistence.criteria.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Service
public class PlaygroundService extends AbstractSecurityService implements AbstractCRUDService<PlaygroundEntity, PlaygroundDTO> {

    private OrderRepository orderRepository;
    private ReservationRepository reservationRepository;
    private PlaygroundRepository playgroundRepository;
    private PlaygroundMapper playgroundMapper;

    @Autowired
    public void setMessages(MessageContainer messages) {
        super.setMessages(messages);
    }

    @Autowired
    protected void setUserRepository(UserRepository userRepository) {
        super.setUserRepository(userRepository);
    }

    @Autowired
    protected void setRoleRepository(RoleRepository roleRepository) {
        super.setRoleRepository(roleRepository);
    }

    @Autowired
    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
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
     * @param id   {@link Integer} playground identifier
     * @param from {@link Date} first date of grid
     * @param to   {@link Date} last date of grid
     * @return {@link PlaygroundGridDTO}
     * @throws ResourceNotFoundException if playground not found
     */
    @Transactional(readOnly = true)
    public PlaygroundGridDTO getGrid(Integer id, LocalDate from, LocalDate to) throws ResourceNotFoundException {
        try {
            return playgroundMapper.makeSchedule(
                    playgroundMapper.toGridDTO(playgroundRepository.getOne(id)), LocalDateTime.now(), from, to,
                    reservationRepository.findAll(new ReservationFilter(id, from, to))
            );
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(mGetAndFormat("sportsportal.lease.Playground.notExistById.message", id), e);
        }
    }

    /**
     * Returns requested playground with short information.
     *
     * @param id {@link Integer} playground identifier
     * @return {@link PlaygroundShortDTO}
     * @throws ResourceNotFoundException if playground not found
     */
    @Transactional(readOnly = true)
    public PlaygroundShortDTO getShort(Integer id) throws ResourceNotFoundException {
        try {
            return playgroundMapper.toShortDTO(playgroundRepository.getOne(id));
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(mGetAndFormat("sportsportal.lease.Playground.notExistById.message", id), e);
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
            throw new ResourceNotFoundException(mGetAndFormat("sportsportal.lease.Playground.notExistById.message", id), e);
        }
    }

    /**
     * Reserve sent datetimes and returns new order id.
     *
     * @param id                 {@link Integer} playground identifier
     * @param reservationListDTO {@link ReservationListDTO} reservation info
     * @return new order {@link Integer} identifier
     * @throws ResourceNotFoundException if playground not found
     */
    @Transactional
    public Integer reserve(Integer id, ReservationListDTO reservationListDTO) throws ResourceNotFoundException, AuthorizationRequiredException {
        try {
            PlaygroundEntity playground = playgroundRepository.getOne(id);

            int expMinutes = 15;
            LocalDateTime now = LocalDateTime.now();
            OrderEntity order = new OrderEntity();
            order.setPlayground(playground);
            order.setCustomer(getCurrentUserEntity());
            order.setDatetime(Timestamp.valueOf(now));
            order.setExpiration(Timestamp.valueOf(now.plusMinutes(expMinutes)));

            int sumCost = 0;
            int cost = playground.getCost();
            Collection<LocalDateTime> datetimes = reservationListDTO.getReservations();
            Collection<ReservationEntity> reservations = new ArrayList<>(datetimes.size());
            for (LocalDateTime datetime : datetimes) {
                ReservationEntity reservation = new ReservationEntity();
                reservation.setDatetime(Timestamp.valueOf(datetime)); // todo: datetime support check!
                reservation.setOrder(order);
                reservation.setCost(cost);

                sumCost += cost;
                reservations.add(reservation);
            }

            order.setPaid(false);
            order.setCost(sumCost);
            order.setReservations(reservations);
            return orderRepository.saveAndFlush(order).getId();
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(mGetAndFormat("sportsportal.lease.Playground.notExistById.message", id), e);
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
            throw new ResourceNotFoundException(mGetAndFormat("sportsportal.lease.Playground.notExistById.message", id));
        } catch (OptimisticLockException | OptimisticLockingFailureException e) {
            throw new ResourceOptimisticLockException(mGet("sportsportal.lease.Playground.optimisticLock.message"), e);
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
            throw new ResourceNotFoundException(mGetAndFormat("sportsportal.lease.Playground.notExistById.message", id));
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
