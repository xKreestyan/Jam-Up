package org.jamup.bean;

import org.jamup.exception.InvalidFieldException;
import org.jamup.model.TimeSlot;
import org.jamup.model.enums.Instrument;
import org.jamup.model.enums.MusicGenre;
import org.jamup.model.enums.ReservationStatus;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;

public class ReservationBean {

    private String reservationId;
    private String venueId;
    private String venueName;
    private String artistId;
    private String artistName;
    private List<Instrument> artistInstruments;
    private List<MusicGenre> artistGenres;
    private String notes;
    private ReservationStatus status;
    private TimeSlot reservedSlot;

    //validation


    //getters and setters

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getVenueId() {
        return venueId;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public List<Instrument> getArtistInstruments() {
        return artistInstruments;
    }

    public void setArtistInstruments(List<Instrument> artistInstruments) {
        this.artistInstruments = artistInstruments;
    }

    public List<MusicGenre> getArtistGenres() {
        return artistGenres;
    }

    public void setArtistGenres(List<MusicGenre> artistGenres) {
        this.artistGenres = artistGenres;
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

    public void setReservedSlot(TimeSlot reservedSlot) {
        this.reservedSlot = reservedSlot;
    }

    //reservation creation bean constructor (View -> Controller)
    public ReservationBean(String venueId, TimeSlot reservedSlot, String notes) throws InvalidFieldException {
        this.venueId = venueId;
        this.reservedSlot = reservedSlot;
        checkAndSetNotes(notes);
    }

    //existing reservation bean constructor (Controller -> View)
    public ReservationBean(String reservationId, String venueId, String venueName,
                           String artistId, String artistName,
                           List<Instrument> artistInstruments, List<MusicGenre> artistGenres,
                           String notes, ReservationStatus status, TimeSlot reservedSlot) {

        this.reservationId = reservationId;
        this.venueId = venueId;
        this.venueName = venueName;
        this.artistId = artistId;
        this.artistName = artistName;
        this.artistInstruments = artistInstruments;
        this.artistGenres = artistGenres;
        this.notes = notes;
        this.status = status;
        this.reservedSlot = reservedSlot;

    }

}
