package ru.vldf.sportsportal.service.security.encoder;

import org.springframework.data.util.Pair;

import java.util.Date;

public interface ExpiringClockProvider {

    Pair<Date, Date> gen(ExpirationType type);
}
