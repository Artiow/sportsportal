package ru.vldf.sportsportal.mapper.sectional.lease;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.vldf.sportsportal.domain.sectional.lease.ReservationEntity;
import ru.vldf.sportsportal.dto.sectional.lease.specialized.ReservationResumeDTO;
import ru.vldf.sportsportal.mapper.generic.ObjectMapper;
import ru.vldf.sportsportal.mapper.manual.JavaTimeMapper;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring", uses = JavaTimeMapper.class)
public interface ReservationMapper extends ObjectMapper<ReservationEntity, ReservationResumeDTO.ReservationItemDTO> {

    @Mapping(target = "datetime", source = "pk.datetime")
    ReservationResumeDTO.ReservationItemDTO toDTO(ReservationEntity entity);

    @Mapping(target = "pk.datetime", source = "datetime")
    ReservationEntity toEntity(ReservationResumeDTO.ReservationItemDTO dto);

    default List<ReservationResumeDTO> toResumeDTO(Collection<ReservationEntity> entityCollection) {
        if (entityCollection == null) {
            return null;
        }

        // todo: do mapping!
        return null;
    }
}
