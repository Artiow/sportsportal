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
import java.util.*;

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
            @Mapping(target = "grid", expression = "java(prepareGrid(entity))")
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

    default PlaygroundGridDTO.ReservationGridDTO prepareGrid(PlaygroundEntity entity) {
        if (entity == null) {
            return null;
        }

        // integer time value init
        boolean halfHourAvailable = entity.getHalfHourAvailable();
        int openTimeHour = entity.getOpening().toLocalDateTime().toLocalTime().getHour();
        int openTimeMinute = entity.getOpening().toLocalDateTime().toLocalTime().getMinute();
        int closeTimeHour = entity.getClosing().toLocalDateTime().toLocalTime().getHour();
        int closeTimeMinute = entity.getClosing().toLocalDateTime().toLocalTime().getMinute();

        // total time minute normalize
        int step = halfHourAvailable ? 30 : 60;
        int minuteDiff = (closeTimeMinute - openTimeMinute);
        if ((minuteDiff % 30) != 0) {
            closeTimeMinute -= ((minuteDiff < 0) ? (minuteDiff + step) : minuteDiff);
            if (closeTimeMinute < 0) {
                closeTimeMinute += 60;
                closeTimeHour -= 1;
            }
            minuteDiff = (closeTimeMinute - openTimeMinute);
        }

        // total times hour pre-calculate
        int totalTimes = (closeTimeHour - openTimeHour);
        totalTimes = (totalTimes < 0) ? (totalTimes + 24) : totalTimes;
        totalTimes = (halfHourAvailable) ? (totalTimes * 2) : totalTimes;

        // total times minute final calculate
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

    default PlaygroundGridDTO setGrid(PlaygroundGridDTO grid, Collection<ReservationEntity> reservations) {
        if ((grid == null) || (reservations == null)) {
            return null;
        }

        // sorting
        List<ReservationEntity> list = new ArrayList<>(reservations);
        list.sort(Comparator.comparing(ReservationEntity::getDatetime));

        // date info definition
        LocalDate startDate = list.get(0).getDatetime().toLocalDateTime().toLocalDate();
        LocalDate endDate = list.get(list.size() - 1).getDatetime().toLocalDateTime().toLocalDate();
        int totalDays = (int) ChronoUnit.DAYS.between(startDate, endDate);

        // line template init
        Map<LocalTime, Boolean> lineTemplate = new HashMap<>();
        LocalTime iter = grid.getGrid().getStartTime();
        LocalTime end = grid.getGrid().getEndTime();
        while (end.isAfter(iter)) {
            lineTemplate.put(iter, true);
            iter = iter.plusMinutes(30);
        }

        // schedule build
        LocalDate current = null;
        Map<LocalTime, Boolean> line = null;
        Map<LocalDate, Map<LocalTime, Boolean>> schedule = new HashMap<>();
        for (ReservationEntity item : list) {
            LocalDateTime datetime = item.getDatetime().toLocalDateTime();
            LocalDate date = datetime.toLocalDate();
            LocalTime time = datetime.toLocalTime();
            if (!date.equals(current)) {
                if (current != null) {
                    schedule.put(current, line);
                }
                schedule.put(current, line);
                line = new HashMap<>(lineTemplate);
                current = date;
            }
            line.put(time, false);
        }

        // grid setting
        grid.getGrid()
                .setSchedule(schedule)
                .setTotalDays(totalDays)
                .setStartDate(startDate)
                .setEndDate(endDate);

        return grid;
    }
}
