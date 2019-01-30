package ru.vldf.sportsportal.integration.robokassa.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
public class Payment {

    // required
    private Integer id;
    private BigDecimal sum;
    private String description;

    // additional
    private String email;
    private Date expiration;
}
