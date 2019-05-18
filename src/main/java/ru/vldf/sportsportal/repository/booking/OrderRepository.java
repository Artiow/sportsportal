package ru.vldf.sportsportal.repository.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;
import ru.vldf.sportsportal.domain.sectional.booking.OrderEntity;
import ru.vldf.sportsportal.repository.AbstractIdentifiedRepository;

import java.util.List;

/**
 * @author Namednev Artem
 */
public interface OrderRepository extends AbstractIdentifiedRepository<OrderEntity> {

    Page<OrderEntity> findAllByCustomer(UserEntity customer, Pageable pageable);

    List<OrderEntity> findAllByIsPaidIsFalse();

    void deleteByIdAndIsPaidIsFalse(Integer id);
}
