package ru.vldf.sportsportal.mapper.manual;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class JavaTimeMapper {

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
