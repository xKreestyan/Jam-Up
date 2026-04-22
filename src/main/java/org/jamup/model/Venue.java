package org.jamup.model;

import org.jamup.model.enums.MusicGenre;
import java.util.List;

public class Venue {

    private String id;
    private String name;
    private final String description;
    private List<MusicGenre> genres;
    private String location;
    private final AvailabilityCalendar calendar;
    private final VenueManager manager;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
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

    public AvailabilityCalendar getCalendar() {
        return calendar;
    }

    public VenueManager getManager() {
        return manager;
    }

    public Venue(String id, String name, String description, List<MusicGenre> genres, String location, VenueManager manager) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.genres = genres;
        this.location = location;
        this.calendar = new AvailabilityCalendar();
        this.manager = manager;
    }

}