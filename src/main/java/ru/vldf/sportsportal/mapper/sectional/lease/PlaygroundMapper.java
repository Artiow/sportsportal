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
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

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
            @Mapping(target = "grid", expression = "java(calcGrid(entity.getOpening(), entity.getClosing(), entity.getHalfHourAvailable()))")
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

    default PlaygroundGridDTO.ReservationGridDTO calcGrid(Timestamp opening, Timestamp closing, Boolean halfHourAvailable) {
        if ((opening == null) || (closing == null) || (halfHourAvailable == null)) {
            return null;
        }

        int openTime = opening.toLocalDateTime().toLocalTime().getHour();
        int closeTime = closing.toLocalDateTime().toLocalTime().getHour();
        int totalTimes = (closeTime - openTime);
        totalTimes = (totalTimes < 0) ? (totalTimes + 24) : totalTimes;
        totalTimes = (halfHourAvailable) ? (totalTimes * 2) : totalTimes;

        List<PlaygroundGridDTO.TimegridCellDTO> schedule = new ArrayList<>(totalTimes);
        if (!halfHourAvailable) {
            for (int i = openTime; i < closeTime; ) {
                schedule.add(new PlaygroundGridDTO.TimegridCellDTO()
                        .setStartTime(LocalTime.of(i, 0))
                        .setEndTime(LocalTime.of(++i, 0))
                );
            }
        } else {
            for (int i = openTime; i < closeTime; ) {
                schedule.add(new PlaygroundGridDTO.TimegridCellDTO()
                        .setStartTime(LocalTime.of(i, 0))
                        .setEndTime(LocalTime.of(i, 30))
                );
                schedule.add(new PlaygroundGridDTO.TimegridCellDTO()
                        .setStartTime(LocalTime.of(i, 30))
                        .setEndTime(LocalTime.of(++i, 0))
                );
            }
        }

        return new PlaygroundGridDTO.ReservationGridDTO()
                .setTotalTimes(totalTimes)
                .setSchedule(schedule);
    }

    default PlaygroundGridDTO setGrid(PlaygroundGridDTO grid, Collection<ReservationEntity> reservation) {
        List<PlaygroundGridDTO.ReservationLineDTO> days = new ArrayList<>();

        // todo: insert map logic here!

        days.sort(Comparator.comparing(PlaygroundGridDTO.ReservationLineDTO::getDate));
        int totalDays = days.size();
        grid.getGrid()
                .setTotalDays(totalDays)
                .setStartDate(days.get(0).getDate())
                .setEndDate(days.get(totalDays - 1).getDate())
                .setDays(days);

        return grid;
    }
}
