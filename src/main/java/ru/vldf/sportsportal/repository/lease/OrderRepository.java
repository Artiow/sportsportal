package ru.vldf.sportsportal.repository.lease;

import ru.vldf.sportsportal.domain.sectional.lease.OrderEntity;
import ru.vldf.sportsportal.repository.AbstractIdentifiedRepository;

import java.sql.Timestamp;
import java.util.List;

public interface OrderRepository extends AbstractIdentifiedRepository<OrderEntity> {

    List<OrderEntity> findAllByPaidIsFalseAndExpirationBefore(Timestamp limit);
}
