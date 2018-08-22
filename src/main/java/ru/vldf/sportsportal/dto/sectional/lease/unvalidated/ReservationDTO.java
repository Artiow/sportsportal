package ru.vldf.sportsportal.dto.sectional.lease.unvalidated;

import ru.vldf.sportsportal.dto.generic.DataTransferObject;
import ru.vldf.sportsportal.dto.util.DayInfoDTO;

import java.net.URI;
import java.time.LocalTime;
import java.util.List;

public class ReservationDTO implements DataTransferObject {

    private URI playgroundURL;
    private Boolean halfHourAvailable;
    private Boolean fullHourRequired;
    private List<TimegridCell> schedule;
    private List<ReservationLine> lines;


    public URI getPlaygroundURL() {
        return playgroundURL;
    }

    public ReservationDTO setPlaygroundURL(URI playgroundURL) {
        this.playgroundURL = playgroundURL;
        return this;
    }

    public Boolean getHalfHourAvailable() {
        return halfHourAvailable;
    }

    public ReservationDTO setHalfHourAvailable(Boolean halfHourAvailable) {
        this.halfHourAvailable = halfHourAvailable;
        return this;
    }

    public Boolean getFullHourRequired() {
        return fullHourRequired;
    }

    public ReservationDTO setFullHourRequired(Boolean fullHourRequired) {
        this.fullHourRequired = fullHourRequired;
        return this;
    }

    public List<TimegridCell> getSchedule() {
        return schedule;
    }

    public ReservationDTO setSchedule(List<TimegridCell> schedule) {
        this.schedule = schedule;
        return this;
    }

    public List<ReservationLine> getLines() {
        return lines;
    }

    public ReservationDTO setLines(List<ReservationLine> lines) {
        this.lines = lines;
        return this;
    }


    public static class ReservationLine implements DataTransferObject {

        private DayInfoDTO day;
        private List<ReservationCell> cells;

        public DayInfoDTO getDay() {
            return day;
        }

        public ReservationLine setDay(DayInfoDTO day) {
            this.day = day;
            return this;
        }

        public List<ReservationCell> getCells() {
            return cells;
        }

        public ReservationLine setCells(List<ReservationCell> cells) {
            this.cells = cells;
            return this;
        }
    }

    public static class ReservationCell implements DataTransferObject {

        private Integer cost;
        private Boolean available;

        public Integer getCost() {
            return cost;
        }

        public ReservationCell setCost(Integer cost) {
            this.cost = cost;
            return this;
        }

        public Boolean getAvailable() {
            return available;
        }

        public ReservationCell setAvailable(Boolean available) {
            this.available = available;
            return this;
        }
    }

    public static class TimegridCell implements DataTransferObject {

        private LocalTime startTime;
        private LocalTime endTime;

        public LocalTime getStartTime() {
            return startTime;
        }

        public TimegridCell setStartTime(LocalTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public LocalTime getEndTime() {
            return endTime;
        }

        public TimegridCell setEndTime(LocalTime endTime) {
            this.endTime = endTime;
            return this;
        }
    }
}
