package ru.vldf.sportsportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vldf.sportsportal.config.messages.MessageContainer;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;
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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class PlaygroundService extends AbstractSecurityService implements AbstractCRUDService<PlaygroundEntity, PlaygroundDTO> {

    @Value("${code.role.admin}")
    private String adminRoleCode;

    @Value("${order.expiration.amount}")
    private Integer orderExpirationAmount;

    @Value("${order.expiration.unit}")
    private String orderExpirationUnit;

    private OrderRepository orderRepository;
    private ReservationRepository reservationRepository;
    private PlaygroundRepository playgroundRepository;
    private PlaygroundMapper playgroundMapper;
    private JavaTimeMapper javaTimeMapper;


    @Autowired
    public PlaygroundService(MessageContainer messages, UserRepository userRepository, RoleRepository roleRepository) {
        super(messages, userRepository, roleRepository);
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

    @Autowired
    public void setJavaTimeMapper(JavaTimeMapper javaTimeMapper) {
        this.javaTimeMapper = javaTimeMapper;
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
     * Returns requested playground.
     *
     * @param id {@link Integer} playground identifier
     * @return {@link PlaygroundDTO} playground
     * @throws ResourceNotFoundException if playground not found
     */
    @Override
    @Transactional(
            readOnly = true,
            rollbackFor = {ResourceNotFoundException.class},
            noRollbackFor = {EntityNotFoundException.class}
    )
    public PlaygroundDTO get(Integer id) throws ResourceNotFoundException {
        try {
            return playgroundMapper.toDTO(playgroundRepository.getOne(id));
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(msg("sportsportal.lease.Playground.notExistById.message", id), e);
        }
    }

    /**
     * Returns requested playground with short information.
     *
     * @param id {@link Integer} playground identifier
     * @return {@link PlaygroundShortDTO}
     * @throws ResourceNotFoundException if playground not found
     */
    @Transactional(
            readOnly = true,
            rollbackFor = {ResourceNotFoundException.class},
            noRollbackFor = {EntityNotFoundException.class}
    )
    public PlaygroundShortDTO getShort(Integer id) throws ResourceNotFoundException {
        try {
            return playgroundMapper.toShortDTO(playgroundRepository.getOne(id));
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(msg("sportsportal.lease.Playground.notExistById.message", id), e);
        }
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
    @Transactional(
            readOnly = true,
            rollbackFor = {ResourceNotFoundException.class, ResourceCorruptedException.class},
            noRollbackFor = {EntityNotFoundException.class, DataCorruptedException.class}
    )
    public PlaygroundGridDTO getGrid(Integer id, LocalDate from, LocalDate to) throws ResourceNotFoundException, ResourceCorruptedException {
        try {
            return playgroundMapper.makeSchedule(
                    playgroundMapper.toGridDTO(playgroundRepository.getOne(id)), LocalDateTime.now(), from, to,
                    reservationRepository.findAll(new ReservationFilter(id, from, to))
            );
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(msg("sportsportal.lease.Playground.notExistById.message", id), e);
        } catch (DataCorruptedException e) {
            throw new ResourceCorruptedException(msg("sportsportal.lease.Playground.dataCorrupted.message", id), e);
        }
    }

    /**
     * Returns available for reservation times for playground by identifier and collections of checked
     * reservation times.
     *
     * @param id           {@link Integer} playground identifier
     * @param version      {@link Long} playground version
     * @param reservations {@link Collection<LocalDateTime>} checked reservation times
     * @return {@link ReservationListDTO} with available for reservation times
     * @throws ResourceNotFoundException if playground not found
     */
    @Transactional(
            readOnly = true,
            rollbackFor = {ResourceNotFoundException.class},
            noRollbackFor = {EntityNotFoundException.class}
    )
    public ReservationListDTO check(Integer id, Long version, Collection<LocalDateTime> reservations) throws ResourceNotFoundException {
        try {
            PlaygroundEntity playgroundEntity = playgroundRepository.getOne(id);
            if (!version.equals(playgroundEntity.getVersion())) {
                return new ReservationListDTO().setReservations(new ArrayList<>(0));
            } else {
                if (!(reservations instanceof List)) reservations = new ArrayList<>(reservations);
                Collections.sort((List<LocalDateTime>) reservations);
                Iterator<LocalDateTime> iterator = reservations.iterator();
                while (iterator.hasNext()) {
                    LocalDateTime localDateTime = iterator.next();
                    Timestamp reservedTime = javaTimeMapper.toTimestamp(localDateTime.toLocalTime());
                    if ((reservedTime.before(playgroundEntity.getOpening())) || (!reservedTime.before(playgroundEntity.getClosing()))) {
                        iterator.remove();
                    } else if (reservationRepository.existsByPkPlaygroundAndPkDatetime(playgroundEntity, Timestamp.valueOf(localDateTime))) {
                        iterator.remove();
                    }
                }
                reservations = LocalDateTimeNormalizer.advancedCheck(reservations, playgroundEntity.getHalfHourAvailable(), playgroundEntity.getFullHourRequired());
                return new ReservationListDTO().setReservations((reservations instanceof List) ? (List<LocalDateTime>) reservations : new ArrayList<>(reservations));
            }
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(msg("sportsportal.lease.Playground.notExistById.message", id), e);
        }
    }

    /**
     * Reserve sent datetimes and returns new order id.
     *
     * @param id                 {@link Integer} playground identifier
     * @param reservationListDTO {@link ReservationListDTO} reservation info
     * @return {@link Integer} new order identifier
     * @throws UnauthorizedAccessException   if authorization is missing
     * @throws ResourceNotFoundException     if playground not found
     * @throws ResourceCannotCreateException if reservation cannot create
     */
    @Transactional(
            rollbackFor = {UnauthorizedAccessException.class, ResourceNotFoundException.class, ResourceCannotCreateException.class},
            noRollbackFor = {EntityNotFoundException.class}
    )
    public Integer reserve(Integer id, ReservationListDTO reservationListDTO) throws UnauthorizedAccessException, ResourceNotFoundException, ResourceCannotCreateException {
        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expiration = now.plus(orderExpirationAmount, ChronoUnit.valueOf(orderExpirationUnit));

            UserEntity currentUser = getCurrentUserEntity();
            PlaygroundEntity playground = playgroundRepository.getOne(id);
            boolean isOwner = (playground.getOwners().contains(currentUser));

            OrderEntity order = new OrderEntity();
            order.setCustomer(currentUser);
            order.setDatetime(Timestamp.valueOf(now));
            order.setExpiration(!isOwner ? Timestamp.valueOf(expiration) : null);

            List<LocalDateTime> datetimes = new ArrayList<>(reservationListDTO.getReservations());
            Collections.sort(datetimes);
            if (!LocalDateTimeNormalizer.check(datetimes, playground.getHalfHourAvailable(), playground.getFullHourRequired())) {
                throw new ResourceCannotCreateException(msg("sportsportal.lease.Playground.notSupportedTime.message"));
            }

            BigDecimal sumPrice = BigDecimal.valueOf(0, 2);
            BigDecimal playgroundPrice = playground.getPrice();
            BigDecimal price = (playground.getHalfHourAvailable())
                    ? playgroundPrice.divide(BigDecimal.valueOf(200, 2), RoundingMode.HALF_UP)
                    : playgroundPrice;

            Collection<ReservationEntity> reservations = new ArrayList<>(datetimes.size());
            for (LocalDateTime datetime : datetimes) {
                Timestamp reservedTime = javaTimeMapper.toTimestamp(datetime.toLocalTime());
                if ((reservedTime.before(playground.getOpening())) || (!reservedTime.before(playground.getClosing()))) {
                    throw new ResourceCannotCreateException(msg("sportsportal.lease.Playground.notWorkingTime.message"));
                }
                Timestamp reservedDatetime = Timestamp.valueOf(datetime);
                if (reservationRepository.existsByPkPlaygroundAndPkDatetime(playground, reservedDatetime)) {
                    throw new ResourceCannotCreateException(msg("sportsportal.lease.Playground.alreadyReservedTime.message"));
                }

                ReservationEntity reservation = new ReservationEntity();
                reservation.setDatetime(reservedDatetime);
                reservation.setPlayground(playground);
                reservation.setOrder(order);
                if (!isOwner) {
                    reservation.setPrice(price);
                    sumPrice = sumPrice.add(price);
                }

                reservations.add(reservation);
            }

            order.setPaid(isOwner);
            order.setSum(sumPrice);
            order.setByOwner(isOwner);
            order.setReservations(reservations);

            OrderEntity newOrderEntity = orderRepository.save(order);
            Integer newOrderId = newOrderEntity.getId();

            // if expiration date set, do scheduled job
            if (Optional.ofNullable(newOrderEntity.getExpiration()).isPresent()) {
                ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
                executorService.schedule(() -> orderRepository.deleteById(newOrderId), ChronoUnit.MILLIS.between(LocalDateTime.now(), expiration), TimeUnit.MILLISECONDS);
            }

            return newOrderId;
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(msg("sportsportal.lease.Playground.notExistById.message", id), e);
        }
    }

    /**
     * Saves new playground.
     *
     * @param playgroundDTO {@link PlaygroundDTO} with playground data
     * @return new playground {@link Integer} identifier
     * @throws UnauthorizedAccessException   if authorization is missing
     * @throws ResourceCannotCreateException if playground cannot create
     */
    @Override
    @Transactional(
            rollbackFor = {UnauthorizedAccessException.class, ResourceCannotCreateException.class},
            noRollbackFor = {JpaObjectRetrievalFailureException.class}
    )
    public Integer create(PlaygroundDTO playgroundDTO) throws UnauthorizedAccessException, ResourceCannotCreateException {
        try {
            PlaygroundEntity playgroundEntity = playgroundMapper.toEntity(playgroundDTO);
            playgroundEntity.setOwners(Collections.singletonList(getCurrentUserEntity()));
            playgroundEntity.setPhotos(Collections.emptyList());
            return playgroundRepository.save(playgroundEntity).getId();
        } catch (JpaObjectRetrievalFailureException e) {
            throw new ResourceCannotCreateException(msg("sportsportal.lease.Playground.cannotCreate.message"), e);
        }
    }

    /**
     * Update playground data.
     *
     * @param id            {@link Integer} playground identifier
     * @param playgroundDTO {@link PlaygroundDTO} with new playground data
     * @throws UnauthorizedAccessException     if authorization is missing
     * @throws ForbiddenAccessException        if user don't have permission to update this playground
     * @throws ResourceNotFoundException       if playground not found
     * @throws ResourceCannotUpdateException   if playground cannot update
     * @throws ResourceOptimisticLockException if playground was already updated
     */
    @Override
    @Transactional(
            rollbackFor = {UnauthorizedAccessException.class, ForbiddenAccessException.class, ResourceNotFoundException.class, ResourceCannotUpdateException.class, ResourceOptimisticLockException.class},
            noRollbackFor = {EntityNotFoundException.class, OptimisticLockException.class, OptimisticLockingFailureException.class, DataAccessException.class}
    )
    public void update(Integer id, PlaygroundDTO playgroundDTO)
            throws UnauthorizedAccessException, ForbiddenAccessException, ResourceNotFoundException, ResourceCannotUpdateException, ResourceOptimisticLockException {
        try {
            PlaygroundEntity playgroundEntity = playgroundRepository.getOne(id);
            if (!currentUserHasRoleByCode(adminRoleCode) && (!isContainCurrentUser(playgroundEntity.getOwners()))) {
                throw new ForbiddenAccessException(msg("sportsportal.lease.Playground.forbidden.message"));
            } else {
                playgroundRepository.save(playgroundMapper.merge(playgroundEntity, playgroundMapper.toEntity(playgroundDTO)));
            }
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(msg("sportsportal.lease.Playground.notExistById.message", id), e);
        } catch (OptimisticLockException | OptimisticLockingFailureException e) {
            throw new ResourceOptimisticLockException(msg("sportsportal.lease.Playground.optimisticLock.message"), e);
        } catch (DataAccessException e) {
            throw new ResourceCannotUpdateException(msg("sportsportal.lease.Playground.cannotUpdate.message"), e);
        }
    }

    /**
     * Delete playground.
     *
     * @param id {@link Integer} playground identifier
     * @throws UnauthorizedAccessException if authorization is missing
     * @throws ForbiddenAccessException    if user don't have permission to delete this playground
     * @throws ResourceNotFoundException   if playground not found
     */
    @Override
    @Transactional(
            rollbackFor = {UnauthorizedAccessException.class, ForbiddenAccessException.class, ResourceNotFoundException.class},
            noRollbackFor = {EntityNotFoundException.class}
    )
    public void delete(Integer id) throws UnauthorizedAccessException, ForbiddenAccessException, ResourceNotFoundException {
        try {
            PlaygroundEntity playgroundEntity = playgroundRepository.getOne(id);
            if (!currentUserHasRoleByCode(adminRoleCode) && (!isContainCurrentUser(playgroundEntity.getOwners()))) {
                throw new ForbiddenAccessException(msg("sportsportal.lease.Playground.forbidden.message"));
            } else {
                playgroundRepository.delete(playgroundEntity);
            }
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(msg("sportsportal.lease.Playground.notExistById.message", id), e);
        }
    }


    public static class PlaygroundFilter extends StringSearcher<PlaygroundEntity> {

        private Collection<String> featureCodes;
        private Collection<String> sportCodes;
        private BigDecimal startPrice;
        private BigDecimal endPrice;
        private Timestamp opening;
        private Timestamp closing;
        private Integer minRate;


        public PlaygroundFilter(PlaygroundFilterDTO dto) {
            super(dto, PlaygroundEntity_.name);
            configureSearchByFeatures(dto);
            configureSearchBySports(dto);
            configureSearchByWorkTime(dto);
            configureSearchByPrice(dto);
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
                boolean toMidnight = closing.equals(LocalTime.MIN);
                final JavaTimeMapper jtMapper = new JavaTimeMapper();
                if ((toMidnight) || (!opening.isAfter(closing))) {
                    this.opening = jtMapper.toTimestamp(opening);
                    this.closing = (!toMidnight) ? jtMapper.toTimestamp(closing) : null;
                } else {
                    this.opening = jtMapper.toTimestamp(closing);
                    this.closing = (!opening.equals(LocalTime.MIN)) ? jtMapper.toTimestamp(opening) : null;
                }
            }
        }

        private void configureSearchByPrice(PlaygroundFilterDTO dto) {
            BigDecimal startPrice = dto.getStartPrice();
            BigDecimal endPrice = dto.getEndPrice();
            if ((startPrice != null) && (endPrice != null)) {
                if (startPrice.compareTo(endPrice) <= 0) {
                    this.startPrice = startPrice;
                    this.endPrice = endPrice;
                } else {
                    this.startPrice = endPrice;
                    this.endPrice = startPrice;
                }
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
            if (opening != null) {
                predicates.add(searchByWorkTimePredicate(root, cb));
            }
            if (startPrice != null) {
                predicates.add(searchByCostPredicate(root, cb));
            }
            if (minRate != null) {
                predicates.add(searchByRatePredicate(root, cb));
            }

            return query
                    .where(cb.and(predicates.toArray(new Predicate[0])))
                    .orderBy(cb.desc(root.get(PlaygroundEntity_.rate)))
                    .distinct(true).getRestriction();
        }

        private Predicate searchByFeaturesPredicate(Root<PlaygroundEntity> root, CriteriaBuilder cb) {
            List<Predicate> predicates = new ArrayList<>(featureCodes.size());
            for (String featureCode : featureCodes) {
                predicates.add(
                        cb.equal(root.join(PlaygroundEntity_.capabilities).get(FeatureEntity_.code), featureCode)
                );
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        }

        private Predicate searchBySportsPredicate(Root<PlaygroundEntity> root, CriteriaBuilder cb) {
            List<Predicate> predicates = new ArrayList<>(sportCodes.size());
            for (String sportCode : sportCodes) {
                predicates.add(
                        cb.equal(root.join(PlaygroundEntity_.specializations).get(SportEntity_.code), sportCode)
                );
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        }

        private Predicate searchByWorkTimePredicate(Root<PlaygroundEntity> root, CriteriaBuilder cb) {
            final Path<Timestamp> playgroundOpening = root.get(PlaygroundEntity_.opening);
            final Path<Timestamp> playgroundClosing = root.get(PlaygroundEntity_.closing);
            final Predicate openingMatch = cb.lessThan(playgroundOpening, closing);
            final Predicate closingMatch = cb.greaterThan(playgroundClosing, opening);
            final Predicate closeOnMidnight = cb.equal(playgroundClosing, (new JavaTimeMapper().toTimestamp(LocalTime.MIN)));
            return (closing != null)
                    ? cb.or(cb.and(closingMatch, openingMatch), cb.and(closeOnMidnight, openingMatch))
                    : cb.or(closingMatch, closeOnMidnight);
        }

        private Predicate searchByCostPredicate(Root<PlaygroundEntity> root, CriteriaBuilder cb) {
            Path<BigDecimal> sought = root.get(PlaygroundEntity_.price);
            return cb.and(
                    cb.greaterThanOrEqualTo(sought, startPrice),
                    cb.lessThanOrEqualTo(sought, endPrice)
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
            final JavaTimeMapper jtMapper = new JavaTimeMapper();
            if (!startDate.isAfter(endDate)) {
                this.start = jtMapper.toTimestamp(startDate);
                this.end = jtMapper.toTimestamp(endDate.plusDays(1));
            } else {
                this.start = jtMapper.toTimestamp(endDate);
                this.end = jtMapper.toTimestamp(startDate.plusDays(1));
            }
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
