package org.jamup.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AvailabilityCalendar {

    private List<TimeSlot> slots;

    public List<LocalTime> availableTimesForDate(LocalDate date) {
        List<LocalTime> result = new ArrayList<>();
        for (TimeSlot slot : slots) {
            if (slot.getDate().equals(date)) {
                result.add(slot.getTime());
            }
        }
        return result;
    }

    public void addSlot(LocalDate date, LocalTime time) {
        slots.add(new TimeSlot(date, time));
    }

    public void removeSlot(LocalDate date, LocalTime time) {
        slots.removeIf(slot -> slot.getDate().equals(date) && slot.getTime().equals(time));
    }

    public AvailabilityCalendar() {
        slots = new ArrayList<>();
    }

}
