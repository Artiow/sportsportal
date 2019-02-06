package ru.vldf.sportsportal.mapper.manual;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author Namednev Artem
 */
@Component
public class JavaTimeMapper {

    public final static LocalTime MIN_TIME = LocalTime.MIN;
    public final static LocalDate MIN_DATE = LocalDate.of(1, 1, 1);
    public final static LocalDateTime MIN = LocalDateTime.of(MIN_DATE, MIN_TIME);


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

        return Timestamp.valueOf(LocalDateTime.of(localDate, MIN_TIME));
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

        return Timestamp.valueOf(LocalDateTime.of(MIN_DATE, localTime));
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
