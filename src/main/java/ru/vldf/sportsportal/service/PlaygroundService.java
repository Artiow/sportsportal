package ru.vldf.sportsportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vldf.sportsportal.config.messages.MessageContainer;
import ru.vldf.sportsportal.domain.sectional.lease.*;
import ru.vldf.sportsportal.dto.pagination.PageDTO;
import ru.vldf.sportsportal.dto.pagination.filters.PlaygroundFilterDTO;
import ru.vldf.sportsportal.dto.sectional.lease.PlaygroundDTO;
import ru.vldf.sportsportal.dto.sectional.lease.shortcut.PlaygroundShortDTO;
import ru.vldf.sportsportal.dto.sectional.lease.specialized.PlaygroundGridDTO;
import ru.vldf.sportsportal.dto.sectional.lease.specialized.ReservationListDTO;
import ru.vldf.sportsportal.mapper.generic.DataCorruptedException;
import ru.vldf.sportsportal.mapper.manual.JavaTimeMapper;
import ru.vldf.sportsportal.mapper.sectional.lease.PlaygroundMapper;
import ru.vldf.sportsportal.repository.common.RoleRepository;
import ru.vldf.sportsportal.repository.common.UserRepository;
import ru.vldf.sportsportal.repository.lease.OrderRepository;
import ru.vldf.sportsportal.repository.lease.PlaygroundRepository;
import ru.vldf.sportsportal.repository.lease.ReservationRepository;
import ru.vldf.sportsportal.service.generic.*;
import ru.vldf.sportsportal.util.LocalDateTimeNormalizer;

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
     * @param filterDTO {@link PlaygroundFilterDTO} filter data
     * @return {@link PageDTO} with {@link PlaygroundShortDTO}
     */
    @Transactional(readOnly = true)
    public PageDTO<PlaygroundShortDTO> getList(PlaygroundFilterDTO filterDTO) {
        PlaygroundFilter filter = new PlaygroundFilter(filterDTO);
        return new PageDTO<>(playgroundRepository.findAll(filter, filter.getPageRequest()).map(playgroundMapper::toShortDTO));
    }

    /**
     * Returns requested playground with time grid info.
     *
     * @param id   {@link Integer} playground identifier
     * @param from {@link Date} first date of grid
     * @param to   {@link Date} last date of grid
     * @return {@link PlaygroundGridDTO}
     * @throws ResourceNotFoundException  if playground not found
     * @throws ResourceCorruptedException if playground data corrupted
     */
    @Transactional(readOnly = true)
    public PlaygroundGridDTO getGrid(Integer id, LocalDate from, LocalDate to) throws ResourceNotFoundException, ResourceCorruptedException {
        try {
            return playgroundMapper.makeSchedule(
                    playgroundMapper.toGridDTO(playgroundRepository.getOne(id)), LocalDateTime.now(), from, to,
                    reservationRepository.findAll(new ReservationFilter(id, from, to))
            );
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(mGetAndFormat("sportsportal.lease.Playground.notExistById.message", id), e);
        } catch (DataCorruptedException e) {
            throw new ResourceCorruptedException(mGetAndFormat("sportsportal.lease.Playground.dataCorrupted.message", id), e);
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
     * @throws AuthorizationRequiredException if authorization is missing
     * @throws ResourceNotFoundException      if playground not found
     * @throws ResourceCannotCreateException  if playground cannot create
     */
    @Transactional
    public Integer reserve(Integer id, ReservationListDTO reservationListDTO) throws AuthorizationRequiredException, ResourceNotFoundException, ResourceCannotCreateException {
        try {
            PlaygroundEntity playground = playgroundRepository.getOne(id);

            int expMinutes = 15;
            LocalDateTime now = LocalDateTime.now();
            OrderEntity order = new OrderEntity();
            order.setCustomer(getCurrentUserEntity());
            order.setDatetime(Timestamp.valueOf(now));
            order.setExpiration(Timestamp.valueOf(now.plusMinutes(expMinutes)));

            int sumCost = 0;
            int cost = playground.getCost();
            Collection<LocalDateTime> datetimes = reservationListDTO.getReservations();
            if (!LocalDateTimeNormalizer.check(datetimes, playground.getHalfHourAvailable())) {
                throw new ResourceCannotCreateException(mGet("sportsportal.lease.Playground.notSupportedTime.message"));
            }

            Collection<ReservationEntity> reservations = new ArrayList<>(datetimes.size());
            for (LocalDateTime datetime : datetimes) {
                Timestamp reservedTime = Timestamp.valueOf(LocalDateTime.of(LocalDate.of(1, 1, 1), datetime.toLocalTime()));
                if ((reservedTime.before(playground.getOpening())) || (!reservedTime.before(playground.getClosing()))) {
                    throw new ResourceCannotCreateException(mGet("sportsportal.lease.Playground.notSupportedTime.message"));
                }
                Timestamp reservedDatetime = Timestamp.valueOf(datetime);
                if (reservationRepository.existsByPkPlaygroundAndPkDatetime(playground, reservedDatetime)) {
                    throw new ResourceCannotCreateException(mGet("sportsportal.lease.Playground.alreadyReservedTime.message"));
                }

                ReservationEntity reservation = new ReservationEntity();
                reservation.setDatetime(reservedDatetime);
                reservation.setPlayground(playground);
                reservation.setOrder(order);
                reservation.setCost(cost);

                sumCost += cost;
                reservations.add(reservation);
            }

            order.setPaid(false);
            order.setCost(sumCost);
            order.setReservations(reservations);
            return orderRepository.save(order).getId();
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


    public static class PlaygroundFilter extends StringSearcher<PlaygroundEntity> {

        private Collection<String> featureCodes;
        private Collection<String> sportCodes;
        private Timestamp opening;
        private Timestamp closing;
        private Integer startCost;
        private Integer endCost;
        private Integer minRate;

        public PlaygroundFilter(PlaygroundFilterDTO dto) {
            super(dto, PlaygroundEntity_.name);
            configureSearchByFeatures(dto);
            configureSearchBySports(dto);
            configureSearchByWorkTime(dto);
            configureSearchByCost(dto);
            configureSearchByRate(dto);
        }

        private void configureSearchByFeatures(PlaygroundFilterDTO dto) {
            Collection<String> featureCodes = dto.getFeatureCodes();
            if ((featureCodes != null) && (!featureCodes.isEmpty())) {
                this.featureCodes = new ArrayList<>(featureCodes);
            }
        }

        private void configureSearchBySports(PlaygroundFilterDTO dto) {
            Collection<String> sportCodes = dto.getSportCodes();
            if ((sportCodes != null) && (!sportCodes.isEmpty())) {
                this.sportCodes = new ArrayList<>(sportCodes);
            }
        }

        private void configureSearchByWorkTime(PlaygroundFilterDTO dto) {
            LocalTime opening = dto.getOpening();
            LocalTime closing = dto.getClosing();
            if ((opening != null) && (closing != null)) {
                final JavaTimeMapper jtMapper = new JavaTimeMapper();
                this.opening = jtMapper.toTimestamp(opening);
                this.closing = jtMapper.toTimestamp(closing);
            }
        }

        private void configureSearchByCost(PlaygroundFilterDTO dto) {
            Integer startCost = dto.getStartCost();
            Integer endCost = dto.getEndCost();
            if ((startCost != null) && (endCost != null)) {
                this.startCost = startCost;
                this.endCost = endCost;
            }
        }

        private void configureSearchByRate(PlaygroundFilterDTO dto) {
            this.minRate = dto.getMinRate();
        }

        @Override
        public Predicate toPredicate(Root<PlaygroundEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            Collection<Predicate> predicates = new ArrayList<>();
            Predicate rootPredicate = super.toPredicate(root, query, cb);
            if (rootPredicate != null) {
                predicates.add(rootPredicate);
            }
            if (featureCodes != null) {
                predicates.add(searchByFeaturesPredicate(root, cb));
            }
            if (sportCodes != null) {
                predicates.add(searchBySportsPredicate(root, cb));
            }
            if ((opening != null) && (closing != null)) {
                predicates.add(searchByWorkTimePredicate(root, cb));
            }
            if ((startCost != null) && (endCost != null)) {
                predicates.add(searchByCostPredicate(root, cb));
            }
            if (minRate != null) {
                predicates.add(searchByRatePredicate(root, cb));
            }

            return query
                    .where(cb.and(predicates.toArray(new Predicate[0])))
                    .distinct(true).getRestriction();
        }

        private Predicate searchByFeaturesPredicate(Root<PlaygroundEntity> root, CriteriaBuilder cb) {
            return null; // todo: predicate
        }

        private Predicate searchBySportsPredicate(Root<PlaygroundEntity> root, CriteriaBuilder cb) {
            return null; // todo: predicate
        }

        private Predicate searchByWorkTimePredicate(Root<PlaygroundEntity> root, CriteriaBuilder cb) {
            return cb.and(
                    cb.greaterThanOrEqualTo(root.get(PlaygroundEntity_.closing), closing),
                    cb.lessThanOrEqualTo(root.get(PlaygroundEntity_.opening), opening)
            );
        }

        private Predicate searchByCostPredicate(Root<PlaygroundEntity> root, CriteriaBuilder cb) {
            Path<Integer> sought = root.get(PlaygroundEntity_.cost);
            return cb.and(
                    cb.greaterThanOrEqualTo(sought, startCost),
                    cb.lessThanOrEqualTo(sought, endCost)
            );
        }

        private Predicate searchByRatePredicate(Root<PlaygroundEntity> root, CriteriaBuilder cb) {
            return cb.greaterThanOrEqualTo(root.get(PlaygroundEntity_.rate), minRate);
        }
    }

    public static class ReservationFilter implements Specification<ReservationEntity> {

        private Integer playgroundId;
        private Timestamp start;
        private Timestamp end;


        public ReservationFilter(Integer playgroundId, LocalDate startDate, LocalDate endDate) {
            this.playgroundId = playgroundId;
            if (!startDate.isAfter(endDate)) {
                this.start = toTimestamp(startDate);
                this.end = toTimestamp(endDate.plusDays(1));
            } else {
                this.start = toTimestamp(endDate);
                this.end = toTimestamp(startDate.plusDays(1));
            }
        }


        private Timestamp toTimestamp(LocalDate localDate) {
            return Timestamp.valueOf(LocalDateTime.of(localDate, LocalTime.MIN));
        }


        @Override
        public Predicate toPredicate(Root<ReservationEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            Path<Integer> playgroundId = root.get(ReservationEntity_.pk).get(ReservationEntityPK_.playground).get(PlaygroundEntity_.id);
            Path<Timestamp> datetime = root.get(ReservationEntity_.pk).get(ReservationEntityPK_.datetime);
            return query.where(cb.and(
                    cb.equal(playgroundId, this.playgroundId),
                    cb.greaterThanOrEqualTo(datetime, start),
                    cb.lessThan(datetime, end)
            )).distinct(true).getRestriction();
        }
    }
}
