package ru.vldf.sportsportal.mapper.sectional.lease;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.vldf.sportsportal.domain.sectional.lease.ReservationEntity;
import ru.vldf.sportsportal.dto.sectional.lease.ReservationDTO;
import ru.vldf.sportsportal.mapper.generic.ObjectMapper;

@Mapper
public interface ReservationMapper extends ObjectMapper<ReservationEntity, ReservationDTO> {

    @Mapping(target = "datetime", source = "pk.datetime")
    ReservationDTO toDTO(ReservationEntity entity);

    @Mapping(target = "pk.datetime", source = "datetime")
    ReservationEntity toEntity(ReservationDTO dto);
}
