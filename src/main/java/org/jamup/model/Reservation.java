package org.jamup.model;

import org.jamup.model.enums.ReservationStatus;

public class Reservation {

    private String id;
    private String notes;
    private ReservationStatus status;
    private Artist artist;
    private Venue venue;
    private TimeSlot reservedSlot;

    private void updateStatus(ReservationStatus status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public TimeSlot getReservedSlot() {
        return reservedSlot;
    }

    public void setReservedSlot(TimeSlot reservedSlot) {
        this.reservedSlot = reservedSlot;
    }

    public Reservation(String id, String notes, Venue venue, TimeSlot reservedSlot) {
        this.id = id;
        this.notes = notes;
        this.status = ReservationStatus.PENDING;
        this.venue = venue;
        this.reservedSlot = reservedSlot;
    }

}
