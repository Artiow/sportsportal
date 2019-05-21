package ru.vldf.sportsportal.mapper.sectional.booking;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.vldf.sportsportal.domain.sectional.booking.PlaygroundEntity;
import ru.vldf.sportsportal.domain.sectional.booking.ReservationEntity;
import ru.vldf.sportsportal.dto.sectional.booking.PlaygroundDTO;
import ru.vldf.sportsportal.dto.sectional.booking.links.PlaygroundLinkDTO;
import ru.vldf.sportsportal.dto.sectional.booking.shortcut.PlaygroundShortDTO;
import ru.vldf.sportsportal.dto.sectional.booking.specialized.PlaygroundBoardDTO;
import ru.vldf.sportsportal.dto.sectional.booking.specialized.ReservationGridDTO;
import ru.vldf.sportsportal.mapper.general.AbstractOverallVersionedMapper;
import ru.vldf.sportsportal.mapper.general.throwable.DataCorruptedException;
import ru.vldf.sportsportal.mapper.manual.JavaTimeMapper;
import ru.vldf.sportsportal.mapper.manual.url.booking.PlaygroundURLMapper;
import ru.vldf.sportsportal.mapper.manual.url.common.PictureURLMapper;
import ru.vldf.sportsportal.mapper.manual.url.common.UserURLMapper;
import ru.vldf.sportsportal.mapper.sectional.common.PictureLinkMapper;
import ru.vldf.sportsportal.mapper.sectional.common.UserMapper;

import javax.persistence.OptimisticLockException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author Namednev Artem
 */
@SuppressWarnings("UnmappedTargetProperties")
@Mapper(uses = {UserMapper.class, SportMapper.class, FeatureMapper.class, PictureLinkMapper.class, PlaygroundURLMapper.class, UserURLMapper.class, PictureURLMapper.class, JavaTimeMapper.class})
public abstract class PlaygroundMapper extends AbstractOverallVersionedMapper<PlaygroundEntity, PlaygroundDTO, PlaygroundShortDTO, PlaygroundLinkDTO> {

    @Mappings({
            @Mapping(target = "locationLatitude", source = "location.latitude"),
            @Mapping(target = "locationLongitude", source = "location.longitude")
    })
    public abstract PlaygroundDTO toDTO(PlaygroundEntity entity);

    @Mappings({
            @Mapping(target = "photos", ignore = true),
            @Mapping(target = "location.latitude", source = "locationLatitude"),
            @Mapping(target = "location.longitude", source = "locationLongitude")
    })
    public abstract PlaygroundEntity toEntity(PlaygroundDTO dto);


    @Mappings({
            @Mapping(target = "playgroundURL", source = "id", qualifiedByName = {"toPlaygroundURL", "fromId"}),
            @Mapping(target = "avatarURL", source = "avatar", qualifiedByName = {"toPictureURL", "fromEntity"})
    })
    public abstract PlaygroundLinkDTO toLinkDTO(PlaygroundEntity entity);


    @Mappings({
            @Mapping(target = "playgroundURL", source = "id", qualifiedByName = {"toPlaygroundURL", "fromId"}),
            @Mapping(target = "avatarURL", source = "avatar", qualifiedByName = {"toPictureURL", "fromEntity"}),
            @Mapping(target = "ownersURLs", source = "owners", qualifiedByName = {"toUserURL", "fromCollection"}),
            @Mapping(target = "photoURLs", source = "photos", qualifiedByName = {"toPictureURL", "fromCollection"})
    })
    public abstract PlaygroundShortDTO toShortDTO(PlaygroundEntity entity);


    @Mappings({
            @Mapping(target = "playground", expression = "java(toLinkDTO(entity))"),
            @Mapping(target = "grid", expression = "java(getRawReservationGridDTO(entity))")
    })
    public abstract PlaygroundBoardDTO toGridDTO(PlaygroundEntity entity) throws DataCorruptedException;


    public ReservationGridDTO getRawReservationGridDTO(PlaygroundEntity entity) throws DataCorruptedException {
        if (entity == null) {
            return null;
        }

        // data integrity check
        LocalTime openTime = entity.getOpening().toLocalDateTime().toLocalTime();
        LocalTime closeTime = entity.getClosing().toLocalDateTime().toLocalTime();
        boolean toMidnight = closeTime.equals(LocalTime.MIDNIGHT);
        if ((!toMidnight) && (!closeTime.isAfter(openTime))) {
            throw new DataCorruptedException("PlaygroundEntity data corrupted: open time must be less than close time!");
        }

        // integer time value init
        int openTimeHour = openTime.getHour();
        int openTimeMinute = openTime.getMinute();
        int closeTimeHour = (!toMidnight) ? closeTime.getHour() : 24;
        int closeTimeMinute = (!toMidnight) ? closeTime.getMinute() : 0;
        boolean halfHourAvailable = entity.getHalfHourAvailable();

        // total times calculate
        int hourDiff = (closeTimeHour - openTimeHour);
        int totalTimes = (halfHourAvailable) ? (2 * hourDiff) : hourDiff;
        int minuteDiff = (closeTimeMinute - openTimeMinute);
        if ((Math.abs(minuteDiff) % 30) != 0) {
            throw new DataCorruptedException("PlaygroundEntity data corrupted: minuteDiff not a multiple of 30!");
        }
        totalTimes = (minuteDiff != 0) ? ((minuteDiff > 0) ? (totalTimes + 1) : (totalTimes - 1)) : totalTimes;

        // close time hour and minute calculate
        if (!halfHourAvailable) {
            closeTimeHour -= 1;
        } else {
            closeTimeMinute -= 30;
            if (closeTimeMinute < 0) {
                closeTimeMinute += 60;
                closeTimeHour -= 1;
            }
        }

        // grid build
        ReservationGridDTO grid = new ReservationGridDTO();
        grid.setTotalTimes(totalTimes);
        grid.setStartTime(LocalTime.of(openTimeHour, openTimeMinute));
        grid.setEndTime(LocalTime.of(closeTimeHour, closeTimeMinute));
        return grid;
    }

    public PlaygroundBoardDTO makeSchedule(PlaygroundBoardDTO playgroundBoardDTO, LocalDateTime now, LocalDate startDate, LocalDate endDate, Collection<ReservationEntity> reservations) throws DataCorruptedException {
        if ((playgroundBoardDTO == null) || (reservations == null)) {
            return null;
        }

        // schedule making
        Map<LocalDate, Map<LocalTime, Boolean>> schedule = makeSchedule(playgroundBoardDTO, now, startDate, endDate).getGrid().getSchedule();

        // schedule updating
        for (ReservationEntity item : reservations) {
            LocalDateTime datetime = item.getDatetime().toLocalDateTime();
            LocalDate date = datetime.toLocalDate();
            LocalTime time = datetime.toLocalTime();

            Map<LocalTime, Boolean> line = Optional
                    .ofNullable(schedule.get(date))
                    .orElseThrow(() -> new DataCorruptedException("Reservation date not supported!"));

            if (Optional.ofNullable(line.get(time))
                    .orElseThrow(() -> new DataCorruptedException("Reservation time not supported!"))) {
                line.put(time, false);
            }
        }

        return playgroundBoardDTO;
    }

    public PlaygroundBoardDTO makeSchedule(PlaygroundBoardDTO playgroundBoardDTO, LocalDateTime now, LocalDate startDate, LocalDate endDate) throws DataCorruptedException {
        if (playgroundBoardDTO == null) {
            return null;
        }

        // start and end dates normalize
        if (startDate.isAfter(endDate)) {
            LocalDate tmp = startDate;
            startDate = endDate;
            endDate = tmp;
        }

        // times and dates info init
        LocalTime startTime = playgroundBoardDTO.getGrid().getStartTime();
        LocalTime endTime = playgroundBoardDTO.getGrid().getEndTime();
        if (!startTime.isBefore(endTime)) {
            throw new DataCorruptedException("PlaygroundGridDTO data corrupted: startTime must be less than endTime!");
        }

        // day count info definition
        int totalDays = ((int) ChronoUnit.DAYS.between(startDate, endDate) + 1);

        // step increment definition
        int amountToAdd;
        ChronoUnit unitToAdd;
        if (playgroundBoardDTO.getHalfHourAvailable()) {
            unitToAdd = ChronoUnit.MINUTES;
            amountToAdd = 30;
        } else {
            unitToAdd = ChronoUnit.HOURS;
            amountToAdd = 1;
        }

        // time iterator by increment definition
        Function<LocalTime, LocalTime> timeInc = i -> i.plus(amountToAdd, unitToAdd);

        // midnight detection
        boolean toMidnight = endTime.plus(amountToAdd, unitToAdd).equals(LocalTime.MIDNIGHT);

        // bool-solid time line
        Function<Boolean, Map<LocalTime, Boolean>> solidMap = param -> {
            Map<LocalTime, Boolean> result = new HashMap<>();
            LocalTime timeIter = startTime;
            if (!toMidnight) {
                while (!timeIter.isAfter(endTime)) {
                    result.put(timeIter, param);
                    timeIter = timeInc.apply(timeIter);
                }
            } else {
                result.put(timeIter, param);
                while (!timeIter.equals(endTime)) {
                    timeIter = timeInc.apply(timeIter);
                    result.put(timeIter, param);
                }
            }
            return result;
        };

        // bool-modular time line
        Function<LocalTime, Map<LocalTime, Boolean>> modularMap = param -> {
            Map<LocalTime, Boolean> result = new HashMap<>();
            LocalTime timeIter = startTime;
            if (!toMidnight) {
                while ((!timeIter.isAfter(param)) && (!timeIter.isAfter(endTime))) {
                    result.put(timeIter, false);
                    timeIter = timeInc.apply(timeIter);
                }
                while (!timeIter.isAfter(endTime)) {
                    result.put(timeIter, true);
                    timeIter = timeInc.apply(timeIter);
                }
            } else {
                while ((!timeIter.isAfter(param)) && (!timeIter.equals(endTime))) {
                    result.put(timeIter, false);
                    timeIter = timeInc.apply(timeIter);
                }
                result.put(timeIter, timeIter.isAfter(param));
                while (!timeIter.equals(endTime)) {
                    timeIter = timeInc.apply(timeIter);
                    result.put(timeIter, true);
                }
            }
            return result;
        };

        // schedule init
        LocalDate currentDate = now.toLocalDate();
        LocalTime currentTime = now.toLocalTime();
        Map<LocalDate, Map<LocalTime, Boolean>> schedule = new HashMap<>();
        LocalDate dayIter = startDate;
        while ((dayIter.isBefore(currentDate)) && (!dayIter.isAfter(endDate))) {
            schedule.put(dayIter, solidMap.apply(false));
            dayIter = dayIter.plusDays(1);
        }
        if ((dayIter.equals(currentDate)) && (!dayIter.isAfter(endDate))) {
            schedule.put(dayIter, modularMap.apply(currentTime));
            dayIter = dayIter.plusDays(1);
        }
        while (!dayIter.isAfter(endDate)) {
            schedule.put(dayIter, solidMap.apply(true));
            dayIter = dayIter.plusDays(1);
        }

        // grid setting
        ReservationGridDTO grid = playgroundBoardDTO.getGrid();
        grid.setSchedule(schedule);
        grid.setTotalDays(totalDays);
        grid.setStartDate(startDate);
        grid.setEndDate(endDate);
        return playgroundBoardDTO;
    }


    @Override
    public PlaygroundEntity merge(PlaygroundEntity acceptor, PlaygroundEntity donor) throws OptimisticLockException {
        super.merge(acceptor, donor);

        acceptor.setName(donor.getName());
        acceptor.setAddress(donor.getAddress());
        acceptor.setPhone(donor.getPhone());
        acceptor.setRate(donor.getRate());
        acceptor.setOpening(donor.getOpening());
        acceptor.setClosing(donor.getClosing());
        acceptor.setHalfHourAvailable(donor.getHalfHourAvailable());
        acceptor.setFullHourRequired(donor.getFullHourRequired());
        acceptor.setPrice(donor.getPrice());
        acceptor.setSpecializations(donor.getSpecializations());
        acceptor.setCapabilities(donor.getCapabilities());
        acceptor.setOwners(donor.getOwners());
        acceptor.setPhotos(donor.getPhotos());

        return acceptor;
    }
}
