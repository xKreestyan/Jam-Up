package org.jamup.bean;

import org.jamup.exception.InvalidFieldException;
import org.jamup.model.Artist;
import org.jamup.model.TimeSlot;
import org.jamup.model.enums.Instrument;
import org.jamup.model.enums.ReservationStatus;

import java.util.List;

public class ReservationBean {

    private String reservationId;
    private final String venueId;
    private String venueName;
    private String artistName;
    private List<Instrument> artistInstruments;
    private String notes;
    private ReservationStatus status;
    private final TimeSlot reservedSlot;

    public String getReservationId() {
        return reservationId;
    }

    public String getVenueId() {
        return venueId;
    }

    public String getVenueName() {
        return venueName;
    }

    public String getArtistName() {
        return artistName;
    }

    public List<Instrument> getArtistInstruments() {
        return artistInstruments;
    }

    public String getNotes() {
        return notes;
    }

    //smart setter
    public void checkAndSetNotes(String notes) throws InvalidFieldException {
        if (notes == null) {
            throw new InvalidFieldException("notes", "cannot be null, use empty string instead");
        }
        if (notes.length() > 500) {
            throw new InvalidFieldException("notes", "cannot exceed 500 characters");
        }
        this.notes = notes.trim();
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public TimeSlot getReservedSlot() {
        return reservedSlot;
    }

    //reservation creation bean constructor (View -> Controller)
    public ReservationBean(String venueId, TimeSlot reservedSlot, String notes) throws InvalidFieldException {
        this.venueId = venueId;
        this.reservedSlot = reservedSlot;
        checkAndSetNotes(notes);
    }

    //existing reservation bean constructor (Controller -> View)
    public ReservationBean(String reservationId, String venueId, String venueName,
                           Artist artist, String notes, ReservationStatus status, TimeSlot reservedSlot) {

        this.reservationId = reservationId;
        this.venueId = venueId;
        this.venueName = venueName;
        this.artistName = artist.getName();
        this.artistInstruments = artist.getInstruments();
        this.notes = notes;
        this.status = status;
        this.reservedSlot = reservedSlot;

    }

}