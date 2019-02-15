package ru.vldf.sportsportal.dto.sectional.lease.specialized;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.dto.generic.DataTransferObject;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
@JsonPropertyOrder({"startDate", "endDate", "totalDays", "startTime", "endTime", "totalTimes", "schedule"})
public class ReservationGridDTO implements DataTransferObject {

    @JsonPropertyOrder(alphabetic = true)
    private Map<LocalDate, Map<LocalTime, Boolean>> schedule;

    private LocalDate startDate;
    private LocalDate endDate;
    private Integer totalDays;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer totalTimes;
}
