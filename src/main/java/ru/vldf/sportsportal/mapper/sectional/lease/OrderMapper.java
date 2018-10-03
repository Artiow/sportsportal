package ru.vldf.sportsportal.mapper.sectional.lease;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.vldf.sportsportal.domain.sectional.lease.OrderEntity;
import ru.vldf.sportsportal.domain.sectional.lease.ReservationEntity;
import ru.vldf.sportsportal.dto.sectional.lease.OrderDTO;
import ru.vldf.sportsportal.dto.sectional.lease.shortcut.OrderShortDTO;
import ru.vldf.sportsportal.dto.sectional.lease.specialized.OrderLinkDTO;
import ru.vldf.sportsportal.mapper.generic.AbstractVersionedMapper;
import ru.vldf.sportsportal.mapper.manual.JavaTimeMapper;
import ru.vldf.sportsportal.mapper.manual.url.lease.OrderURLMapper;
import ru.vldf.sportsportal.mapper.sectional.common.PictureMapper;
import ru.vldf.sportsportal.mapper.sectional.common.UserMapper;

import javax.persistence.OptimisticLockException;
import java.util.Collection;

@Mapper(
        componentModel = "spring",
        uses = {JavaTimeMapper.class, OrderURLMapper.class, PlaygroundMapper.class, UserMapper.class, PictureMapper.class, ReservationMapper.class}
)
public interface OrderMapper extends AbstractVersionedMapper<OrderEntity, OrderDTO> {

    @Mappings({
            @Mapping(target = "orderURL", source = "id", qualifiedByName = {"toOrderURL", "fromId"}),
            @Mapping(target = "customerURL", source = "customer", qualifiedByName = {"toUserURL", "fromEntity"})
    })
    OrderShortDTO toShortDTO(OrderEntity entity);

    @Mappings({
            @Mapping(target = "orderURL", source = "id", qualifiedByName = {"toOrderURL", "fromId"}),
            @Mapping(target = "customerURL", source = "customer", qualifiedByName = {"toUserURL", "fromEntity"})
    })
    OrderLinkDTO toLinkDTO(OrderEntity entity);

    @Mapping(target = "price", ignore = true)
    OrderEntity toEntity(OrderLinkDTO dto);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "customer", ignore = true),
            @Mapping(target = "reservations", ignore = true)
    })
    OrderEntity toEntity(OrderDTO dto);

    @Override
    default OrderEntity merge(OrderEntity acceptor, OrderEntity donor) throws OptimisticLockException {
        AbstractVersionedMapper.super.merge(acceptor, donor);

        acceptor.setPrice(donor.getPrice());
        acceptor.setPaid(donor.getPaid());
        acceptor.setDatetime(donor.getDatetime());
        acceptor.setExpiration(donor.getExpiration());
        acceptor.setCustomer(donor.getCustomer());

        Collection<ReservationEntity> reservations = acceptor.getReservations();
        if (!reservations.isEmpty()) reservations.clear();
        for (ReservationEntity reservation : donor.getReservations()) {
            reservation.setOrder(acceptor);
            reservations.add(reservation);
        }

        return acceptor;
    }
}
