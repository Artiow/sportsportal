package ru.vldf.sportsportal.service.security.encoder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class ExpiringClockService implements ExpiringClock, ExpiringClockProvider {

    @Value("${jwt.access.lifetime.unit}")
    public TimeUnit accessTokenUnit;

    @Value("${jwt.refresh.lifetime.amount}")
    public Long refreshTokenLifetime;

    @Value("${jwt.refresh.lifetime.unit}")
    public TimeUnit refreshTokenUnit;

    @Value("${jwt.access.lifetime.amount}")
    private Long accessTokenLifetime;


    /**
     * Tokens lifetimes calculating.
     */
    @PostConstruct
    private void init() {
        accessTokenLifetime = accessTokenUnit.toMillis(accessTokenLifetime);
        refreshTokenLifetime = refreshTokenUnit.toMillis(refreshTokenLifetime);
    }


    @Override
    public Date now() {
        return Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
    }

    @Override
    public Date exp(Date now, ExpirationType type) {
        Long lifetime;
        switch (type) {
            case ACCESS:
                lifetime = accessTokenLifetime;
                break;
            case REFRESH:
                lifetime = refreshTokenLifetime;
                break;
            default:
                lifetime = 0L;
        }
        return new Date(now.getTime() + lifetime);
    }

    @Override
    public Pair<Date, Date> gen(ExpirationType type) {
        Date now = now();
        return Pair.of(now, exp(now, type));
    }
}
