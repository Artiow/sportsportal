package ru.vldf.sportsportal.mapper.manual;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class JavaTimeMapper {

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

    public Integer toDate(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }

        return timestamp.toLocalDateTime().getYear();
    }

    public Timestamp toTimestamp(Integer date) {
        if (date == null) {
            return null;
        }

        try {
            return new Timestamp(new SimpleDateFormat("yyyy").parse(String.format("%04d", date)).getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
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
