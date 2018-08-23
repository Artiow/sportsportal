package ru.vldf.sportsportal.mapper.sectional.lease;

import org.mapstruct.Mapper;
import ru.vldf.sportsportal.domain.sectional.lease.OrderEntity;
import ru.vldf.sportsportal.dto.sectional.lease.OrderDTO;
import ru.vldf.sportsportal.mapper.generic.AbstractVersionedMapper;
import ru.vldf.sportsportal.mapper.manual.JavaTimeMapper;
import ru.vldf.sportsportal.mapper.sectional.common.PictureMapper;
import ru.vldf.sportsportal.mapper.sectional.common.UserMapper;

import javax.persistence.OptimisticLockException;

@Mapper(
        componentModel = "spring",
        uses = {JavaTimeMapper.class, UserMapper.class, PictureMapper.class, ReservationMapper.class}
)
public interface OrderMapper extends AbstractVersionedMapper<OrderEntity, OrderDTO> {

    @Override
    default OrderEntity merge(OrderEntity acceptor, OrderEntity donor) throws OptimisticLockException {
        AbstractVersionedMapper.super.merge(acceptor, donor);

        // todo: merge!

        return acceptor;
    }
}
