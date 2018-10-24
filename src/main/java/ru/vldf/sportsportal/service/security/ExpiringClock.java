package ru.vldf.sportsportal.service.security;

import io.jsonwebtoken.Clock;

import java.util.Date;

public interface ExpiringClock extends Clock {

    Date exp(Date now, ExpirationType type);

    enum ExpirationType {ACCESS, REFRESH}
}
