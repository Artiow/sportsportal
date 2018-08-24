package ru.vldf.sportsportal.dto.sectional.lease.specialized;

import ru.vldf.sportsportal.dto.generic.DataTransferObject;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

public class PlaygroundGridDTO implements DataTransferObject {

    private URI playgroundURL;
    private Integer cost;
    private Boolean halfHourAvailable;
    private Boolean fullHourRequired;
    private ReservationGridDTO grid;


    public URI getPlaygroundURL() {
        return playgroundURL;
    }

    public PlaygroundGridDTO setPlaygroundURL(URI playgroundURL) {
        this.playgroundURL = playgroundURL;
        return this;
    }

    public Integer getCost() {
        return cost;
    }

    public PlaygroundGridDTO setCost(Integer cost) {
        this.cost = cost;
        return this;
    }

    public Boolean getHalfHourAvailable() {
        return halfHourAvailable;
    }

    public PlaygroundGridDTO setHalfHourAvailable(Boolean halfHourAvailable) {
        this.halfHourAvailable = halfHourAvailable;
        return this;
    }

    public Boolean getFullHourRequired() {
        return fullHourRequired;
    }

    public PlaygroundGridDTO setFullHourRequired(Boolean fullHourRequired) {
        this.fullHourRequired = fullHourRequired;
        return this;
    }

    public ReservationGridDTO getGrid() {
        return grid;
    }

    public PlaygroundGridDTO setGrid(ReservationGridDTO grid) {
        this.grid = grid;
        return this;
    }


    public static class ReservationGridDTO implements DataTransferObject {

        private LocalDate startDate;
        private LocalDate endDate;
        private Integer totalDays;
        private LocalTime startTime;
        private LocalTime endTime;
        private Integer totalTimes;
        private Map<LocalDate, Map<LocalTime, Boolean>> schedule;


        public LocalDate getStartDate() {
            return startDate;
        }

        public ReservationGridDTO setStartDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        public ReservationGridDTO setEndDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public Integer getTotalDays() {
            return totalDays;
        }

        public ReservationGridDTO setTotalDays(Integer totalDays) {
            this.totalDays = totalDays;
            return this;
        }

        public LocalTime getStartTime() {
            return startTime;
        }

        public ReservationGridDTO setStartTime(LocalTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public LocalTime getEndTime() {
            return endTime;
        }

        public ReservationGridDTO setEndTime(LocalTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public Integer getTotalTimes() {
            return totalTimes;
        }

        public ReservationGridDTO setTotalTimes(Integer totalTimes) {
            this.totalTimes = totalTimes;
            return this;
        }

        public Map<LocalDate, Map<LocalTime, Boolean>> getSchedule() {
            return schedule;
        }

        public ReservationGridDTO setSchedule(Map<LocalDate, Map<LocalTime, Boolean>> schedule) {
            this.schedule = schedule;
            return this;
        }
    }
}
