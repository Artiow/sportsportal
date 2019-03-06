package ru.vldf.sportsportal.service.security.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
public class Payload {

    private Integer userId;
    private ExpirationType type;
}
