package ru.vldf.sportsportal.service.security.keykeeper;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
public class Payload {

    private Integer userId;
    private UUID keyUuid;
}
