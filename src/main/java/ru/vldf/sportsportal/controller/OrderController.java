package ru.vldf.sportsportal.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vldf.sportsportal.dto.sectional.lease.OrderDTO;
import ru.vldf.sportsportal.service.OrderService;
import ru.vldf.sportsportal.service.generic.ForbiddenAccessException;
import ru.vldf.sportsportal.service.generic.ResourceNotFoundException;
import ru.vldf.sportsportal.service.generic.UnauthorizedAccessException;

/**
 * @author Namednev Artem
 */
@RestController
@Api(tags = {"Order"})
@RequestMapping("${api.path.lease.order}")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    /**
     * Returns order by identifier with full information.
     *
     * @param id order identifier
     * @return {@link OrderDTO} requested order
     * @throws UnauthorizedAccessException if authorization is missing
     * @throws ForbiddenAccessException    if user don't have permission to get this order
     * @throws ResourceNotFoundException   if order not found
     */
    @GetMapping("/{id}")
    @ApiOperation("получить информацию о заказе")
    public OrderDTO get(@PathVariable int id) throws UnauthorizedAccessException, ForbiddenAccessException, ResourceNotFoundException {
        return orderService.get(id);
    }

    /**
     * Returns order link by  order identifier.
     *
     * @param id order identifier
     * @return {@link String} requested link
     * @throws UnauthorizedAccessException if authorization is missing
     * @throws ForbiddenAccessException    if user don't have permission to get this order
     * @throws ResourceNotFoundException   if order not found
     */
    @GetMapping("/{id}/link")
    @ApiOperation("получить ссылку на оплату заказа")
    public String getLink(@PathVariable int id) throws UnauthorizedAccessException, ForbiddenAccessException, ResourceNotFoundException {
        return orderService.getLink(id);
    }

    /**
     * Delete order by id.
     *
     * @param id order identifier
     * @return no content
     * @throws ResourceNotFoundException   if order not found
     * @throws UnauthorizedAccessException if authorization is missing
     * @throws ForbiddenAccessException    if user don't have permission to delete this order
     */
    @DeleteMapping("/{id}")
    @ApiOperation("удалить заказ")
    public ResponseEntity<Void> delete(@PathVariable int id) throws ResourceNotFoundException, UnauthorizedAccessException, ForbiddenAccessException {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
