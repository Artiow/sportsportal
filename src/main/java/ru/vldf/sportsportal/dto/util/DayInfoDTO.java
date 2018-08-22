package ru.vldf.sportsportal.dto.util;

import ru.vldf.sportsportal.dto.generic.DataTransferObject;

import java.time.LocalDate;

public class DayInfoDTO implements DataTransferObject {

    private LocalDate date;
    private String dayOfWeek;
    private String dayOfWeekShort;

    public LocalDate getDate() {
        return date;
    }

    public DayInfoDTO setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public DayInfoDTO setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
        return this;
    }

    public String getDayOfWeekShort() {
        return dayOfWeekShort;
    }

    public DayInfoDTO setDayOfWeekShort(String dayOfWeekShort) {
        this.dayOfWeekShort = dayOfWeekShort;
        return this;
    }
}
