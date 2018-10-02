package ru.vldf.sportsportal.dto.sectional.lease.specialized;

import ru.vldf.sportsportal.dto.generic.AbstractIdentifiedDTO;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.net.URI;

public class OrderLinkDTO extends AbstractIdentifiedDTO {

    @NotNull
    private Integer id;

    private URI orderURL;
    private URI customerURL;
    private BigDecimal price;


    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public OrderLinkDTO setId(Integer id) {
        this.id = id;
        return this;
    }

    public URI getOrderURL() {
        return orderURL;
    }

    public OrderLinkDTO setOrderURL(URI orderURL) {
        this.orderURL = orderURL;
        return this;
    }

    public URI getCustomerURL() {
        return customerURL;
    }

    public OrderLinkDTO setCustomerURL(URI customerURL) {
        this.customerURL = customerURL;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public OrderLinkDTO setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }
}
