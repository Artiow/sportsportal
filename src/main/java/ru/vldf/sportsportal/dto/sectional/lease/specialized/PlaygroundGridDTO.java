package ru.vldf.sportsportal.dto.sectional.lease.specialized;

import ru.vldf.sportsportal.dto.generic.DataTransferObject;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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
        private Integer totalTimes;
        private List<TimegridCellDTO> schedule;
        private List<ReservationLineDTO> days;

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

        public Integer getTotalTimes() {
            return totalTimes;
        }

        public ReservationGridDTO setTotalTimes(Integer totalTimes) {
            this.totalTimes = totalTimes;
            return this;
        }

        public List<TimegridCellDTO> getSchedule() {
            return schedule;
        }

        public ReservationGridDTO setSchedule(List<TimegridCellDTO> schedule) {
            this.schedule = schedule;
            return this;
        }

        public List<ReservationLineDTO> getDays() {
            return days;
        }

        public ReservationGridDTO setDays(List<ReservationLineDTO> days) {
            this.days = days;
            return this;
        }
    }

    public static class ReservationLineDTO implements DataTransferObject {

        private LocalDate date;
        private List<ReservationCellDTO> cells;

        public LocalDate getDate() {
            return date;
        }

        public ReservationLineDTO setDate(LocalDate date) {
            this.date = date;
            return this;
        }

        public List<ReservationCellDTO> getCells() {
            return cells;
        }

        public ReservationLineDTO setCells(List<ReservationCellDTO> cells) {
            this.cells = cells;
            return this;
        }
    }

    public static class ReservationCellDTO implements DataTransferObject {

        private Boolean available;

        public Boolean getAvailable() {
            return available;
        }

        public ReservationCellDTO setAvailable(Boolean available) {
            this.available = available;
            return this;
        }
    }

    public static class TimegridCellDTO implements DataTransferObject {

        private LocalTime startTime;
        private LocalTime endTime;

        public LocalTime getStartTime() {
            return startTime;
        }

        public TimegridCellDTO setStartTime(LocalTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public LocalTime getEndTime() {
            return endTime;
        }

        public TimegridCellDTO setEndTime(LocalTime endTime) {
            this.endTime = endTime;
            return this;
        }
    }
}
