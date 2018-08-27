package ru.vldf.sportsportal.mapper.manual;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JavaTimeMapper {

    private ZoneId systemDefaultZoneId;

    @PostConstruct
    public void init() {
        systemDefaultZoneId = ZoneId.systemDefault();
    }


    public LocalDate toLocalDate(Date date) {
        if (date == null) {
            return null;
        }

        return date.toInstant().atZone(systemDefaultZoneId).toLocalDate();
    }

    public Date toDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }

        return Date.from(localDate.atStartOfDay(systemDefaultZoneId).toInstant());
    }

    public LocalTime toLocalTime(Date date) {
        if (date == null) {
            return null;
        }

        return date.toInstant().atZone(systemDefaultZoneId).toLocalTime();
    }

    public Date toDate(LocalTime localTime) {
        if (localTime == null) {
            return null;
        }

        return Date.from(localTime.atDate(LocalDate.of(1, 1, 1)).atZone(systemDefaultZoneId).toInstant());
    }

    public LocalDateTime toLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }

        return date.toInstant().atZone(systemDefaultZoneId).toLocalDateTime();
    }

    public Date toDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }

        return Date.from(localDateTime.atZone(systemDefaultZoneId).toInstant());
    }

    public LocalDate toLocalDate(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }

        return timestamp.toLocalDateTime().toLocalDate();
    }

    public Timestamp toTimestamp(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }

        return Timestamp.valueOf(LocalDateTime.of(localDate, LocalTime.MIN));
    }

    public LocalTime toLocalTime(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }

        return timestamp.toLocalDateTime().toLocalTime();
    }

    public Timestamp toTimestamp(LocalTime localTime) {
        if (localTime == null) {
            return null;
        }

        return Timestamp.valueOf(LocalDateTime.of(LocalDate.of(1, 1, 1), localTime));
    }

    public LocalDateTime toLocalDateTime(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }

        return timestamp.toLocalDateTime();
    }

    public Timestamp toTimestamp(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }

        return Timestamp.valueOf(localDateTime);
    }
}
