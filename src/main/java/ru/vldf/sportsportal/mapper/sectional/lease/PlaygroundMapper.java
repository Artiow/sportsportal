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
    PlaygroundGridDTO toGridDTO(PlaygroundEntity entity);

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

    @Mapping(target = "id", ignore = true)
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
        acceptor.setCost(donor.getCost());
        acceptor.setSpecializations(donor.getSpecializations());
        acceptor.setCapabilities(donor.getCapabilities());
        acceptor.setOwners(donor.getOwners());
        acceptor.setPhotos(donor.getPhotos());

        return acceptor;
    }

    default PlaygroundGridDTO.ReservationGridDTO getRawReservationGridDTO(PlaygroundEntity entity) {
        if (entity == null) {
            return null;
        }

        // integer time value init
        boolean halfHourAvailable = entity.getHalfHourAvailable();
        int openTimeHour = entity.getOpening().toLocalDateTime().toLocalTime().getHour();
        int openTimeMinute = entity.getOpening().toLocalDateTime().toLocalTime().getMinute();
        int closeTimeHour = entity.getClosing().toLocalDateTime().toLocalTime().getHour();
        int closeTimeMinute = entity.getClosing().toLocalDateTime().toLocalTime().getMinute();

        // total times hour pre-calculate
        int totalTimes = (closeTimeHour - openTimeHour);
        totalTimes = (totalTimes < 0) ? (totalTimes + 24) : totalTimes;
        totalTimes = (halfHourAvailable) ? (totalTimes * 2) : totalTimes;

        // total times minute final calculate
        int minuteDiff = (closeTimeMinute - openTimeMinute);
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

    default PlaygroundGridDTO makeSchedule(PlaygroundGridDTO playgroundGridDTO, LocalDateTime now, LocalDate startDate, LocalDate endDate) {
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
        // WARING: startTime must be less than endTime!
        // todo: add night schedules support!
        LocalDate currentDate = now.toLocalDate();
        LocalTime currentTime = now.toLocalTime();
        LocalTime startTime = playgroundGridDTO.getGrid().getStartTime();
        LocalTime endTime = playgroundGridDTO.getGrid().getEndTime();

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

    default PlaygroundGridDTO makeSchedule(PlaygroundGridDTO playgroundGridDTO, LocalDateTime now, LocalDate startDate, LocalDate endDate, Collection<ReservationEntity> reservations) {
        if ((playgroundGridDTO == null) || (reservations == null)) {
            return null;
        }

        // schedule making
        Map<LocalDate, Map<LocalTime, Boolean>> schedule =
                makeSchedule(playgroundGridDTO, now, startDate, endDate).getGrid().getSchedule();

        // schedule updating
        for (ReservationEntity item : reservations) {
            LocalDateTime datetime = item.getDatetime().toLocalDateTime();
            Optional
                    .ofNullable(schedule.get(datetime.toLocalDate()))
                    .map(line -> line.put(datetime.toLocalTime(), false));
        }

        return playgroundGridDTO;
    }
}
