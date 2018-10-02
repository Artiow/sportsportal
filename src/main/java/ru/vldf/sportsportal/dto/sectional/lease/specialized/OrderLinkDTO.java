package ru.vldf.sportsportal.dto.sectional.lease.specialized;

import ru.vldf.sportsportal.dto.generic.AbstractIdentifiedDTO;

import javax.validation.constraints.NotNull;

public class OrderLinkDTO extends AbstractIdentifiedDTO {

    @NotNull
    private Integer id;


    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public AbstractIdentifiedDTO setId(Integer id) {
        this.id = id;
        return this;
    }
}
