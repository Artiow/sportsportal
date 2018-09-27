package ru.vldf.sportsportal.mapper.sectional.lease;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.vldf.sportsportal.domain.sectional.lease.PlaygroundEntity;
import ru.vldf.sportsportal.domain.sectional.lease.ReservationEntity;
import ru.vldf.sportsportal.dto.sectional.lease.PlaygroundDTO;
import ru.vldf.sportsportal.dto.sectional.lease.shortcut.PlaygroundShortDTO;
import ru.vldf.sportsportal.dto.sectional.lease.specialized.PlaygroundGridDTO;
import ru.vldf.sportsportal.dto.sectional.lease.specialized.PlaygroundLinkDTO;
import ru.vldf.sportsportal.mapper.generic.AbstractVersionedMapper;
import ru.vldf.sportsportal.mapper.generic.DataCorruptedException;
import ru.vldf.sportsportal.mapper.manual.JavaTimeMapper;
import ru.vldf.sportsportal.mapper.manual.url.common.PictureURLMapper;
import ru.vldf.sportsportal.mapper.manual.url.common.UserURLMapper;
import ru.vldf.sportsportal.mapper.manual.url.lease.PlaygroundURLMapper;
import ru.vldf.sportsportal.mapper.sectional.common.PictureMapper;
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

@Mapper(
        componentModel = "spring",
        uses = {JavaTimeMapper.class, PlaygroundURLMapper.class, UserURLMapper.class, PictureURLMapper.class, UserMapper.class, PictureMapper.class, SportMapper.class, FeatureMapper.class}
)
public interface PlaygroundMapper extends AbstractVersionedMapper<PlaygroundEntity, PlaygroundDTO> {

    @Mappings({
            @Mapping(target = "playgroundURL", source = "id", qualifiedByName = {"toPlaygroundURL", "fromId"}),
            @Mapping(target = "ownersURLs", source = "owners", qualifiedByName = {"toUserURL", "fromCollection"}),
            @Mapping(target = "photoURLs", source = "photos", qualifiedByName = {"toPictureURL", "fromCollection"})
    })
    PlaygroundShortDTO toShortDTO(PlaygroundEntity entity);

    @Mappings({
            @Mapping(target = "playgroundURL", source = "id", qualifiedByName = {"toPlaygroundURL", "fromId"}),
            @Mapping(target = "grid", expression = "java(getRawReservationGridDTO(entity))")
    })
    PlaygroundGridDTO toGridDTO(PlaygroundEntity entity) throws DataCorruptedException;

    @Mappings({
            @Mapping(target = "playgroundURL", source = "id", qualifiedByName = {"toPlaygroundURL", "fromId"}),
            @Mapping(target = "ownersURLs", source = "owners", qualifiedByName = {"toUserURL", "fromCollection"}),
            @Mapping(target = "photoURLs", source = "photos", qualifiedByName = {"toPictureURL", "fromCollection"})
    })
    PlaygroundLinkDTO toLinkDTO(PlaygroundEntity entity);

    @Mappings({
            @Mapping(target = "name", ignore = true),
            @Mapping(target = "address", ignore = true),
            @Mapping(target = "phone", ignore = true),
            @Mapping(target = "rate", ignore = true)
    })
    PlaygroundEntity toEntity(PlaygroundLinkDTO dto);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "reservations", ignore = true),
    })
    PlaygroundEntity toEntity(PlaygroundDTO dto);

    @Override
    default PlaygroundEntity merge(PlaygroundEntity acceptor, PlaygroundEntity donor) throws OptimisticLockException {
        AbstractVersionedMapper.super.merge(acceptor, donor);

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

    default PlaygroundGridDTO.ReservationGridDTO getRawReservationGridDTO(PlaygroundEntity entity) throws DataCorruptedException {
        if (entity == null) {
            return null;
        }

        // data integrity check
        LocalTime openTime = entity.getOpening().toLocalDateTime().toLocalTime();
        LocalTime closeTime = entity.getClosing().toLocalDateTime().toLocalTime();
        boolean toMidnight = closeTime.equals(LocalTime.MIN);
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
        return new PlaygroundGridDTO.ReservationGridDTO()
                .setTotalTimes(totalTimes)
                .setStartTime(LocalTime.of(openTimeHour, openTimeMinute))
                .setEndTime(LocalTime.of(closeTimeHour, closeTimeMinute));
    }

    default PlaygroundGridDTO makeSchedule(PlaygroundGridDTO playgroundGridDTO, LocalDateTime now, LocalDate startDate, LocalDate endDate)
            throws DataCorruptedException {
        if (playgroundGridDTO == null) {
            return null;
        }

        // start and end dates normalize
        if (startDate.isAfter(endDate)) {
            LocalDate tmp = startDate;
            startDate = endDate;
            endDate = tmp;
        }

        // times and dates info init
        LocalTime startTime = playgroundGridDTO.getGrid().getStartTime();
        LocalTime endTime = playgroundGridDTO.getGrid().getEndTime();
        if (!startTime.isBefore(endTime)) {
            throw new DataCorruptedException("PlaygroundGridDTO data corrupted: startTime must be less than endTime!");
        }

        // day count info definition
        int totalDays = ((int) ChronoUnit.DAYS.between(startDate, endDate) + 1);

        // step increment definition
        int amountToAdd;
        ChronoUnit unitToAdd;
        if (playgroundGridDTO.getHalfHourAvailable()) {
            unitToAdd = ChronoUnit.MINUTES;
            amountToAdd = 30;
        } else {
            unitToAdd = ChronoUnit.HOURS;
            amountToAdd = 1;
        }

        // bool-solid time line
        Function<Boolean, Map<LocalTime, Boolean>> solidMap = param -> {
            Map<LocalTime, Boolean> result = new HashMap<>();
            LocalTime timeIter = startTime;
            while (!timeIter.isAfter(endTime)) {
                result.put(timeIter, param);
                timeIter = timeIter.plus(amountToAdd, unitToAdd);
            }
            return result;
        };

        // bool-modular time line
        Function<LocalTime, Map<LocalTime, Boolean>> modularMap = param -> {
            Map<LocalTime, Boolean> result = new HashMap<>();
            LocalTime timeIter = startTime;
            while ((!timeIter.isAfter(param)) && (!timeIter.isAfter(endTime))) {
                result.put(timeIter, false);
                timeIter = timeIter.plus(amountToAdd, unitToAdd);
            }
            while (!timeIter.isAfter(endTime)) {
                result.put(timeIter, true);
                timeIter = timeIter.plus(amountToAdd, unitToAdd);
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
        playgroundGridDTO.getGrid()
                .setSchedule(schedule)
                .setTotalDays(totalDays)
                .setStartDate(startDate)
                .setEndDate(endDate);

        return playgroundGridDTO;
    }

    default PlaygroundGridDTO makeSchedule(PlaygroundGridDTO playgroundGridDTO, LocalDateTime now, LocalDate startDate, LocalDate endDate, Collection<ReservationEntity> reservations)
            throws DataCorruptedException {
        if ((playgroundGridDTO == null) || (reservations == null)) {
            return null;
        }

        // schedule making
        Map<LocalDate, Map<LocalTime, Boolean>> schedule =
                makeSchedule(playgroundGridDTO, now, startDate, endDate).getGrid().getSchedule();

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

        return playgroundGridDTO;
    }
}
