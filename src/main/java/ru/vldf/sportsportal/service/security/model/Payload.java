package ru.vldf.sportsportal.service.security.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
public class Payload {

    private ExpirationType type;
    private Integer userId;
    private UUID keyUuid;
}
