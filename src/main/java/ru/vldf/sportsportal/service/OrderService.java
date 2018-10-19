package ru.vldf.sportsportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vldf.sportsportal.config.messages.MessageContainer;
import ru.vldf.sportsportal.domain.sectional.lease.OrderEntity;
import ru.vldf.sportsportal.dto.sectional.lease.OrderDTO;
import ru.vldf.sportsportal.mapper.sectional.lease.OrderMapper;
import ru.vldf.sportsportal.repository.common.RoleRepository;
import ru.vldf.sportsportal.repository.common.UserRepository;
import ru.vldf.sportsportal.repository.lease.OrderRepository;
import ru.vldf.sportsportal.service.generic.*;

import javax.persistence.EntityNotFoundException;

@Service
public class OrderService extends AbstractSecurityService implements AbstractCRUDService<OrderEntity, OrderDTO> {

    @Value("${code.role.admin}")
    private String adminRoleCode;

    private OrderRepository orderRepository;
    private OrderMapper orderMapper;

    @Autowired
    public OrderService(MessageContainer messages, UserRepository userRepository, RoleRepository roleRepository) {
        super(messages, userRepository, roleRepository);
    }

    @Autowired
    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Autowired
    public void setOrderMapper(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }


    /**
     * Returns requested order.
     *
     * @param id {@link Integer} order identifier
     * @return {@link OrderDTO} order
     * @throws UnauthorizedAccessException if authorization is missing
     * @throws ForbiddenAccessException    if user don't have permission to get this order
     * @throws ResourceNotFoundException   if order not found
     */
    @Transactional(
            readOnly = true,
            rollbackFor = {UnauthorizedAccessException.class, ForbiddenAccessException.class, ResourceNotFoundException.class},
            noRollbackFor = {EntityNotFoundException.class}
    )
    public OrderDTO get(Integer id) throws UnauthorizedAccessException, ForbiddenAccessException, ResourceNotFoundException {
        try {
            OrderEntity orderEntity = orderRepository.getOne(id);
            if (!currentUserHasRole(adminRoleCode) && (!getCurrentUserId().equals(orderEntity.getCustomer().getId()))) {
                throw new ForbiddenAccessException(mGet("sportsportal.lease.Order.forbidden.message"));
            }
            return orderMapper.toDTO(orderEntity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(mGetAndFormat("sportsportal.lease.Order.notExistById.message", id), e);
        }
    }

    /**
     * Create new order and returns its identifier.
     *
     * @param orderDTO {@link OrderDTO} order data
     * @return new order {@link Integer} identifier
     */
    @Transactional
    public Integer create(OrderDTO orderDTO) {
        throw new UnsupportedOperationException(mGetAndFormat("sportsportal.handle.UnsupportedOperationException.message", "create"));
    }

    /**
     * Update order data.
     *
     * @param id       {@link Integer} order identifier
     * @param orderDTO {@link OrderDTO} new order data
     */
    @Transactional
    public void update(Integer id, OrderDTO orderDTO) {
        throw new UnsupportedOperationException(mGetAndFormat("sportsportal.handle.UnsupportedOperationException.message", "update"));
    }

    /**
     * Delete order.
     *
     * @param id {@link Integer} order identifier
     */
    @Transactional
    public void delete(Integer id) {
        throw new UnsupportedOperationException(mGetAndFormat("sportsportal.handle.UnsupportedOperationException.message", "delete"));
    }
}
