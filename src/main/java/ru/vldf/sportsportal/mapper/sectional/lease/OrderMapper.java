package ru.vldf.sportsportal.mapper.sectional.lease;

import org.mapstruct.Mapper;
import ru.vldf.sportsportal.domain.sectional.lease.OrderEntity;
import ru.vldf.sportsportal.dto.sectional.lease.OrderDTO;
import ru.vldf.sportsportal.mapper.generic.AbstractIdentifiedMapper;
import ru.vldf.sportsportal.mapper.manual.JavaTimeMapper;
import ru.vldf.sportsportal.mapper.sectional.common.PictureMapper;
import ru.vldf.sportsportal.mapper.sectional.common.UserMapper;

@Mapper(
        componentModel = "spring",
        uses = {JavaTimeMapper.class, UserMapper.class, PictureMapper.class, ReservationMapper.class}
)
public interface OrderMapper extends AbstractIdentifiedMapper<OrderEntity, OrderDTO> {

}
