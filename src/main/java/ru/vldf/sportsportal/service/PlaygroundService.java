package ru.vldf.sportsportal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.vldf.sportsportal.domain.sectional.booking.*;
import ru.vldf.sportsportal.domain.sectional.common.PictureEntity;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;
import ru.vldf.sportsportal.dto.pagination.PageDTO;
import ru.vldf.sportsportal.dto.pagination.filters.PlaygroundFilterDTO;
import ru.vldf.sportsportal.dto.sectional.booking.PlaygroundDTO;
import ru.vldf.sportsportal.dto.sectional.booking.shortcut.PlaygroundShortDTO;
import ru.vldf.sportsportal.dto.sectional.booking.specialized.PlaygroundBoardDTO;
import ru.vldf.sportsportal.dto.sectional.booking.specialized.ReservationListDTO;
import ru.vldf.sportsportal.integration.mail.MailService;
import ru.vldf.sportsportal.mapper.general.throwable.DataCorruptedException;
import ru.vldf.sportsportal.mapper.manual.JavaTimeMapper;
import ru.vldf.sportsportal.mapper.sectional.booking.PlaygroundMapper;
import ru.vldf.sportsportal.repository.booking.OrderRepository;
import ru.vldf.sportsportal.repository.booking.PlaygroundRepository;
import ru.vldf.sportsportal.repository.booking.ReservationRepository;
import ru.vldf.sportsportal.service.general.AbstractSecurityService;
import ru.vldf.sportsportal.service.general.CRUDService;
import ru.vldf.sportsportal.service.general.throwable.*;
import ru.vldf.sportsportal.util.CollectionSorter;
import ru.vldf.sportsportal.util.LocalDateTimeGridChecker;

import javax.mail.MessagingException;
import javax.persistence.OptimisticLockException;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Namednev Artem
 */
@Service
@RequiredArgsConstructor
public class PlaygroundService extends AbstractSecurityService implements CRUDService<PlaygroundEntity, PlaygroundDTO> {

    private final PictureService pictureService;

    private final OrderRepository orderRepository;
    private final ReservationRepository reservationRepository;
    private final PlaygroundRepository playgroundRepository;

    private final PlaygroundMapper playgroundMapper;
    private final JavaTimeMapper javaTimeMapper;

    private final MailService mailService;


    @Value("${order.expiration.amount}")
    private Integer orderExpirationAmount;

    @Value("${order.expiration.unit}")
    private String orderExpirationUnit;


    /**
     * Returns requested filtered page with list of playgrounds.
     *
     * @param filterDTO the filter parameters.
     * @return filtered requested page with playgrounds.
     */
    @Transactional(readOnly = true)
    public PageDTO<PlaygroundShortDTO> getList(PlaygroundFilterDTO filterDTO) {
        PlaygroundFilter filter = new PlaygroundFilter(filterDTO);
        return PageDTO.from(playgroundRepository.findAll(filter, filter.getPageRequest()).map(playgroundMapper::toShortDTO));
    }

    /**
     * Returns requested playground by playground identifier.
     *
     * @param id the playground identifier.
     * @return requested playground data.
     * @throws ResourceNotFoundException if playground not found.
     */
    @Override
    @Transactional(readOnly = true, rollbackFor = {ResourceNotFoundException.class})
    public PlaygroundDTO get(Integer id) throws ResourceNotFoundException {
        return playgroundMapper.toDTO(findById(id));
    }

    /**
     * Returns requested playground by playground identifier.
     *
     * @param id the playground identifier.
     * @return requested playground short data.
     * @throws ResourceNotFoundException if playground not found.
     */
    @Transactional(readOnly = true, rollbackFor = {ResourceNotFoundException.class})
    public PlaygroundShortDTO getShort(Integer id) throws ResourceNotFoundException {
        return playgroundMapper.toShortDTO(findById(id));
    }

    /**
     * Returns requested playground with time board information.
     *
     * @param id   the playground identifier.
     * @param from the first date of time board.
     * @param to   the last date of time board.
     * @return requested playground with time board information.
     * @throws ResourceNotFoundException  if playground not found.
     * @throws ResourceCorruptedException if playground data corrupted.
     */
    @Transactional(readOnly = true, rollbackFor = {ResourceNotFoundException.class, ResourceCorruptedException.class}, noRollbackFor = {DataCorruptedException.class})
    public PlaygroundBoardDTO getBoard(Integer id, LocalDate from, LocalDate to) throws ResourceNotFoundException, ResourceCorruptedException {
        try {
            return playgroundMapper.makeSchedule(playgroundMapper.toGridDTO(findById(id)), LocalDateTime.now(), from, to, reservationRepository.findAll(new ReservationFilter(id, from, to)));
        } catch (DataCorruptedException e) {
            throw new ResourceCorruptedException(msg("sportsportal.booking.Playground.dataCorrupted.message", id), e);
        }
    }

    /**
     * Returns available reservation times for playground.
     *
     * @param id      the playground identifier.
     * @param version the playground data version.
     * @param times   the collection of checked reservation times.
     * @return list of available reservation times.
     * @throws ResourceNotFoundException if playground not found.
     */
    @Transactional(readOnly = true, rollbackFor = {ResourceNotFoundException.class})
    public ReservationListDTO check(Integer id, Long version, Collection<String> times) throws ResourceNotFoundException {
        PlaygroundEntity playgroundEntity = findById(id);
        if (!version.equals(playgroundEntity.getVersion())) {
            ReservationListDTO reservationListDTO = new ReservationListDTO();
            reservationListDTO.setReservations(Collections.emptyList());
            return reservationListDTO;
        } else {
            List<LocalDateTime> reservations;
            try {
                reservations = times != null ? times.stream().map(LocalDateTime::parse).sorted().collect(Collectors.toList()) : Collections.emptyList();
            } catch (DateTimeParseException e) {
                reservations = Collections.emptyList();
            }

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

            reservations = LocalDateTimeGridChecker.filter(reservations, playgroundEntity.getHalfHourAvailable(), playgroundEntity.getFullHourRequired());
            ReservationListDTO reservationListDTO = new ReservationListDTO();
            reservationListDTO.setReservations(reservations);
            return reservationListDTO;
        }
    }

    /**
     * Reserve sent datetimes and returns new created order identifier.
     *
     * @param id                 the playground identifier.
     * @param reservationListDTO the reservation details.
     * @return new created order identifier.
     * @throws UnauthorizedAccessException   if authorization is missing.
     * @throws ResourceNotFoundException     if playground not found.
     * @throws ResourceCannotCreateException if reservation cannot create.
     */
    @Transactional(rollbackFor = {UnauthorizedAccessException.class, ResourceNotFoundException.class, ResourceCannotCreateException.class})
    public Integer reserve(Integer id, ReservationListDTO reservationListDTO) throws UnauthorizedAccessException, ResourceNotFoundException, ResourceCannotCreateException {
        PlaygroundEntity playground = findById(id);

        boolean isOwned = currentUserIn(playground.getOwners());
        boolean isFreed = playground.getIsFreed();
        if (!isOwned && playground.getIsTested()) {
            throw new ResourceCannotCreateException(msg("sportsportal.booking.Playground.isTested.message"));
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiration = now.plus(orderExpirationAmount, ChronoUnit.valueOf(orderExpirationUnit));

        OrderEntity order = new OrderEntity();
        order.setCustomer(getCurrentUserEntity());
        order.setDatetime(Timestamp.valueOf(now));
        order.setExpiration(!isOwned && !isFreed ? Timestamp.valueOf(expiration) : null);

        List<LocalDateTime> datetimes = CollectionSorter.getSorted(reservationListDTO.getReservations());
        if (!LocalDateTimeGridChecker.check(datetimes, playground.getHalfHourAvailable(), playground.getFullHourRequired())) {
            throw new ResourceCannotCreateException(msg("sportsportal.booking.Playground.notSupportedTime.message"));
        }

        BigDecimal sumPrice = BigDecimal.valueOf(0, 2);
        BigDecimal playgroundPrice = playground.getPrice();
        BigDecimal price = (playground.getHalfHourAvailable())
                ? playgroundPrice.divide(BigDecimal.valueOf(200, 2), RoundingMode.HALF_UP)
                : playgroundPrice;

        Collection<ReservationEntity> reservations = new ArrayList<>(datetimes.size());
        for (LocalDateTime datetime : datetimes) {
            Timestamp midnight = javaTimeMapper.toTimestamp(LocalTime.MIDNIGHT);
            Timestamp reservedTime = javaTimeMapper.toTimestamp(datetime.toLocalTime());
            if (!(!reservedTime.before(playground.getOpening()) && (reservedTime.before(playground.getClosing()) || midnight.equals(playground.getClosing())))) {
                throw new ResourceCannotCreateException(msg("sportsportal.booking.Playground.notWorkingTime.message"));
            }
            Timestamp reservedDatetime = Timestamp.valueOf(datetime);
            if (reservationRepository.existsByPkPlaygroundAndPkDatetime(playground, reservedDatetime)) {
                throw new ResourceCannotCreateException(msg("sportsportal.booking.Playground.alreadyReservedTime.message"));
            }

            ReservationEntity reservation = new ReservationEntity();
            reservation.setDatetime(reservedDatetime);
            reservation.setPlayground(playground);
            reservation.setOrder(order);
            if (!isOwned) {
                reservation.setPrice(price);
                sumPrice = sumPrice.add(price);
            }

            reservations.add(reservation);
        }

        order.setSum(sumPrice);
        order.setIsPaid(isOwned || isFreed);
        order.setIsOwned(isOwned);
        order.setIsFreed(isFreed);
        order.setReservations(reservations);

        Integer newOrderId = orderRepository.saveAndFlush(order).getId();
        order.setId(newOrderId);

        // if expiration date set, do scheduled job
        if (Optional.ofNullable(order.getExpiration()).isPresent()) {
            ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
            executorService.schedule(() ->
                    orderRepository.deleteByIdAndIsPaidIsFalse(newOrderId), ChronoUnit.MILLIS.between(LocalDateTime.now(), expiration), TimeUnit.MILLISECONDS
            );
        }

        // email notification
        if (order.getIsPaid()) try {
            sendNotificationEmail(order);
        } catch (MessagingException e) {
            throw new ResourceCannotCreateException(msg("sportsportal.mail.cannotSendEmail.message"), e);
        }

        return newOrderId;
    }

    /**
     * Saves new playground.
     *
     * @param playgroundDTO the playground data.
     * @return new created playground identifier.
     * @throws UnauthorizedAccessException   if authorization is missing.
     * @throws ResourceCannotCreateException if playground cannot create.
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
            throw new ResourceCannotCreateException(msg("sportsportal.booking.Playground.cannotCreate.message"), e);
        }
    }

    /**
     * Update playground data.
     *
     * @param id            the playground identifier.
     * @param playgroundDTO the new playground data.
     * @throws UnauthorizedAccessException     if authorization is missing.
     * @throws ForbiddenAccessException        if user don't have permission to update this playground.
     * @throws ResourceNotFoundException       if playground not found.
     * @throws ResourceOptimisticLockException if playground was already updated.
     */
    @Override
    @Transactional(
            rollbackFor = {UnauthorizedAccessException.class, ForbiddenAccessException.class, ResourceNotFoundException.class, ResourceOptimisticLockException.class},
            noRollbackFor = {OptimisticLockException.class, OptimisticLockingFailureException.class}
    )
    public void update(Integer id, PlaygroundDTO playgroundDTO) throws UnauthorizedAccessException, ForbiddenAccessException, ResourceNotFoundException, ResourceOptimisticLockException {
        PlaygroundEntity playgroundEntity = findById(id);
        if ((!currentUserIsAdmin()) && (!currentUserIn(playgroundEntity.getOwners()))) {
            throw new ForbiddenAccessException(msg("sportsportal.booking.Playground.forbidden.message"));
        } else try {
            playgroundRepository.save(playgroundMapper.inject(playgroundEntity, playgroundDTO));
        } catch (OptimisticLockException | OptimisticLockingFailureException e) {
            throw new ResourceOptimisticLockException(msg("sportsportal.booking.Playground.optimisticLock.message"), e);
        }
    }

    /**
     * Delete playground.
     *
     * @param id the playground identifier.
     * @throws UnauthorizedAccessException if authorization is missing.
     * @throws ForbiddenAccessException    if user don't have permission to delete this playground.
     * @throws ResourceNotFoundException   if playground not found.
     */
    @Override
    @Transactional(rollbackFor = {UnauthorizedAccessException.class, ForbiddenAccessException.class, ResourceNotFoundException.class})
    public void delete(Integer id) throws UnauthorizedAccessException, ForbiddenAccessException, ResourceNotFoundException {
        PlaygroundEntity playgroundEntity = findById(id);
        if ((!currentUserIsAdmin()) && (!currentUserIn(playgroundEntity.getOwners()))) {
            throw new ForbiddenAccessException(msg("sportsportal.booking.Playground.forbidden.message"));
        } else {
            playgroundRepository.delete(playgroundEntity);
        }
    }


    /**
     * Upload playground avatar and returns its picture identifier.
     *
     * @param playgroundId the playground identifier.
     * @param picture      the picture file.
     * @return uploaded avatar picture identifier.
     * @throws UnauthorizedAccessException   if authorization is missing.
     * @throws ForbiddenAccessException      if user don't have permission to upload this playground avatar.
     * @throws ResourceNotFoundException     if playground not found.
     * @throws ResourceCannotCreateException if picture resource cannot be create.
     */
    @Transactional(rollbackFor = {UnauthorizedAccessException.class, ForbiddenAccessException.class, ResourceNotFoundException.class, ResourceCannotCreateException.class})
    public Integer uploadAvatar(Integer playgroundId, MultipartFile picture) throws UnauthorizedAccessException, ForbiddenAccessException, ResourceNotFoundException, ResourceCannotCreateException {
        PlaygroundEntity playgroundEntity = findById(playgroundId);
        if ((!currentUserIsAdmin()) && (!currentUserIn(playgroundEntity.getOwners()))) {
            throw new ForbiddenAccessException(msg("sportsportal.booking.Playground.forbidden.message"));
        } else {
            PictureEntity oldPictureEntity = playgroundEntity.getAvatar();
            PictureEntity newPictureEntity = pictureService.upload(picture);
            playgroundEntity.setAvatar(newPictureEntity);
            playgroundRepository.saveAndFlush(playgroundEntity);

            // old avatar deleting
            if (oldPictureEntity != null) {
                pictureService.delete(oldPictureEntity.getId());
            }

            return newPictureEntity.getId();
        }
    }

    /**
     * Delete playground avatar by playground identifier.
     *
     * @param playgroundId the playground identifier.
     * @throws UnauthorizedAccessException if authorization is missing.
     * @throws ForbiddenAccessException    if user don't have permission to delete this playground avatar.
     * @throws ResourceNotFoundException   if picture not found in database.
     */
    @Transactional(rollbackFor = {UnauthorizedAccessException.class, ForbiddenAccessException.class, ResourceNotFoundException.class})
    public void deleteAvatar(Integer playgroundId) throws UnauthorizedAccessException, ForbiddenAccessException, ResourceNotFoundException {
        PlaygroundEntity playgroundEntity = findById(playgroundId);
        if ((!currentUserIsAdmin()) && (!currentUserIn(playgroundEntity.getOwners()))) {
            throw new ForbiddenAccessException(msg("sportsportal.booking.Playground.forbidden.message"));
        } else if (playgroundEntity.getAvatar() == null) {
            throw new ResourceNotFoundException(msg("sportsportal.booking.Playground.avatarNotExist.message", playgroundId));
        } else {
            PictureEntity pictureEntity = playgroundEntity.getAvatar();
            playgroundEntity.setAvatar(null);
            playgroundRepository.saveAndFlush(playgroundEntity);
            pictureService.delete(pictureEntity.getId());
        }
    }

    /**
     * Upload playground photo and returns its picture identifier.
     *
     * @param playgroundId the playground identifier.
     * @param picture      the picture file.
     * @return uploaded photo picture identifier.
     * @throws UnauthorizedAccessException   if authorization is missing.
     * @throws ForbiddenAccessException      if user don't have permission to upload this playground photo.
     * @throws ResourceNotFoundException     if playground not found.
     * @throws ResourceCannotCreateException if picture resource cannot be create.
     */
    @Transactional(rollbackFor = {UnauthorizedAccessException.class, ForbiddenAccessException.class, ResourceNotFoundException.class, ResourceCannotCreateException.class})
    public Integer uploadPhoto(Integer playgroundId, MultipartFile picture) throws UnauthorizedAccessException, ForbiddenAccessException, ResourceNotFoundException, ResourceCannotCreateException {
        PlaygroundEntity playgroundEntity = findById(playgroundId);
        if ((!currentUserIsAdmin()) && (!currentUserIn(playgroundEntity.getOwners()))) {
            throw new ForbiddenAccessException(msg("sportsportal.booking.Playground.forbidden.message"));
        } else if (playgroundEntity.getPhotos().size() == 5) {
            throw new ResourceCannotCreateException(msg("sportsportal.booking.Playground.photoLimit.message"));
        } else {
            PictureEntity pictureEntity = pictureService.upload(picture);
            playgroundEntity.getPhotos().add(pictureEntity);
            playgroundRepository.saveAndFlush(playgroundEntity);
            return pictureEntity.getId();
        }
    }

    /**
     * Delete playground photo by playground identifier and photo picture identifier.
     *
     * @param playgroundId the playground identifier.
     * @param photoId      the photo picture identifier.
     * @throws UnauthorizedAccessException if authorization is missing.
     * @throws ForbiddenAccessException    if user don't have permission to delete this playground photo.
     * @throws ResourceNotFoundException   if playground not found.
     */
    @Transactional(rollbackFor = {UnauthorizedAccessException.class, ForbiddenAccessException.class, ResourceNotFoundException.class})
    public void deletePhoto(Integer playgroundId, Integer photoId) throws UnauthorizedAccessException, ForbiddenAccessException, ResourceNotFoundException {
        PlaygroundEntity playgroundEntity = findById(playgroundId);
        if ((!currentUserIsAdmin()) && (!currentUserIn(playgroundEntity.getOwners()))) {
            throw new ForbiddenAccessException(msg("sportsportal.booking.Playground.forbidden.message"));
        } else if (!playgroundEntity.getPhotos().removeIf(pictureEntity -> pictureEntity.getId().equals(photoId))) {
            throw new ResourceNotFoundException(msg("sportsportal.booking.Playground.photoNotExistById.message", photoId, playgroundId));
        } else {
            playgroundRepository.saveAndFlush(playgroundEntity);
            pictureService.delete(photoId);
        }
    }


    private PlaygroundEntity findById(Integer id) throws ResourceNotFoundException {
        return playgroundRepository.findById(id).orElseThrow(ResourceNotFoundException.supplier(msg("sportsportal.booking.Playground.notExistById.message", id)));
    }


    /**
     * Notification email sending.
     *
     * @param order the published order.
     * @throws MessagingException if could not sent email.
     */
    private void sendNotificationEmail(OrderEntity order) throws MessagingException {
        UserEntity customer = order.getCustomer();
        Collection<UserEntity> admins = adminRole().getUsers();
        mailService.sender()
                .setTo(customer.getEmail())
                .setBcc(admins.stream().filter(admin -> !admin.equals(customer)).map(UserEntity::getEmail).toArray(String[]::new))
                .setFrom(msg("sportsportal.email.notification.from"), msg("sportsportal.email.notification.personal"))
                .setSubject(msg("sportsportal.email.notification.subject"))
                .setHtml(String.format("<p>%s</p>", msg("sportsportal.email.notification.text", order.getId())))
                .send();
    }


    public static class PlaygroundFilter extends StringSearcher<PlaygroundEntity> {

        // crutch! todo: separate the filter object and specification logic
        private final JavaTimeMapper mapper = new JavaTimeMapper();

        private Collection<String> featureCodes;
        private Collection<String> sportCodes;
        private boolean includeFreed;
        private boolean includeBooked;
        private BigDecimal minPrice;
        private BigDecimal maxPrice;
        private Timestamp opening;
        private Timestamp closing;
        private Integer minRate;
        private Integer maxRate;


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
            this.opening = mapper.toTimestamp(dto.getOpening());
            this.closing = (!Objects.equals(LocalTime.MIN, dto.getClosing())) ? mapper.toTimestamp(dto.getClosing()) : null;
        }

        private void configureSearchByPrice(PlaygroundFilterDTO dto) {
            // fields 'includeFreed' and 'includeBooked' cannot be null, default values used
            this.includeFreed = Optional.ofNullable(dto.getIncludeFreed()).orElse(true);
            this.includeBooked = Optional.ofNullable(dto.getIncludeBooked()).orElse(true);
            this.minPrice = dto.getMinPrice();
            this.maxPrice = dto.getMaxPrice();
        }

        private void configureSearchByRate(PlaygroundFilterDTO dto) {
            this.minRate = dto.getMinRate();
            this.maxRate = dto.getMaxRate();
        }

        @Override
        public Predicate toPredicate(Root<PlaygroundEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            List<Predicate> predicates = super.toPredicateList(root, cb);
            if (featureCodes != null) {
                predicates.add(searchByFeaturesPredicate(root, cb));
            }
            if (sportCodes != null) {
                predicates.add(searchBySportsPredicate(root, cb));
            }
            if (opening != null) {
                predicates.add(searchByWorkTimePredicate(root, cb));
            }
            if (!(includeFreed && includeBooked)) {
                if (includeFreed ^ includeBooked) {
                    predicates.add(cb.equal(root.get(PlaygroundEntity_.isFreed), (includeFreed)));
                } else {
                    predicates.add(cb.disjunction());
                }
            }
            if (includeBooked && minPrice != null) {
                predicates.add(searchByMinPricePredicate(root, cb));
            }
            if (includeBooked && maxPrice != null) {
                predicates.add(searchByMaxPricePredicate(root, cb));
            }
            if (minRate != null) {
                predicates.add(searchByMinRatePredicate(root, cb));
            }
            if (maxRate != null) {
                predicates.add(searchByMaxRatePredicate(root, cb));
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
            final Predicate openingMatch = closing != null ? cb.lessThan(playgroundOpening, closing) : cb.conjunction();
            final Predicate closingMatch = opening != null ? cb.greaterThan(playgroundClosing, opening) : cb.conjunction();
            return cb.and(cb.or(closingMatch, cb.equal(playgroundClosing, mapper.toTimestamp(LocalTime.MIN))), openingMatch);
        }

        private Predicate searchByMinPricePredicate(Root<PlaygroundEntity> root, CriteriaBuilder cb) {
            return cb.or(
                    cb.equal(root.get(PlaygroundEntity_.isFreed), true),
                    cb.and(
                            cb.equal(root.get(PlaygroundEntity_.isFreed), false),
                            cb.greaterThanOrEqualTo(root.get(PlaygroundEntity_.price), minPrice)
                    )
            );
        }

        private Predicate searchByMaxPricePredicate(Root<PlaygroundEntity> root, CriteriaBuilder cb) {
            return cb.or(
                    cb.equal(root.get(PlaygroundEntity_.isFreed), true),
                    cb.and(
                            cb.equal(root.get(PlaygroundEntity_.isFreed), false),
                            cb.lessThanOrEqualTo(root.get(PlaygroundEntity_.price), maxPrice)
                    )
            );
        }

        private Predicate searchByMinRatePredicate(Root<PlaygroundEntity> root, CriteriaBuilder cb) {
            return cb.greaterThanOrEqualTo(root.get(PlaygroundEntity_.rate), minRate);
        }

        private Predicate searchByMaxRatePredicate(Root<PlaygroundEntity> root, CriteriaBuilder cb) {
            return cb.lessThanOrEqualTo(root.get(PlaygroundEntity_.rate), maxRate);
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
