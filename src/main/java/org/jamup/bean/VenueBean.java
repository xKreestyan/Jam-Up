package org.jamup.bean;

import org.jamup.exception.InvalidFieldException;
import org.jamup.model.TimeSlot;
import org.jamup.model.enums.MusicGenre;

import java.time.LocalDate;
import java.util.List;

public class VenueBean {

    private String id;
    private String name;
    private List<MusicGenre> genres;
    private String location;
    private LocalDate searchDate;
    private List<TimeSlot> availableSlots;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    //smart setter
    public void checkAndSetName(String name) throws InvalidFieldException {
        if (name == null || name.trim().length() < 2) {
            throw new InvalidFieldException("name", "must be at least 2 characters long");
        }
        this.name = name.trim();
    }

    public List<MusicGenre> getGenres() {
        return genres;
    }

    public void setGenres(List<MusicGenre> genres) {
        this.genres = genres;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getSearchDate() {
        return searchDate;
    }

    public void setSearchDate(LocalDate searchDate) {
        this.searchDate = searchDate;
    }

    public List<TimeSlot> getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(List<TimeSlot> availableSlots) {
        this.availableSlots = availableSlots;
    }

    //search query bean constructor (View -> Controller)
    public VenueBean(String name, String location, LocalDate searchDate, List<MusicGenre> genres) throws InvalidFieldException {
        checkAndSetName(name);
        this.location = location;
        this.searchDate = searchDate;
        this.genres = genres;
    }

    //search results bean constructor (Controller -> View)
    public VenueBean(String id, String name, String location, List<MusicGenre> genres, List<TimeSlot> availableSlots) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.genres = genres;
        this.availableSlots = availableSlots;
    }
}
