package org.jamup.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class TimeSlot {

    private LocalDate date;
    private LocalTime time;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public TimeSlot(LocalDate date, LocalTime time) {
        this.date = date;
        this.time = time;
    }

}
