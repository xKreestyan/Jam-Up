package org.jamup.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AvailabilityCalendar {

    private final List<TimeSlot> slots;

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

    public List<TimeSlot> getSlots() {
        return slots;
    }

    public void removeSlot(TimeSlot slotToRemove) {
        slots.removeIf(slot -> slot.getDate().equals(slotToRemove.getDate())
                && slot.getTime().equals(slotToRemove.getTime()));
    }

    public AvailabilityCalendar() {
        slots = new ArrayList<>();
    }

}
