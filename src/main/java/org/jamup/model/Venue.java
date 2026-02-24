package org.jamup.model;

import org.jamup.model.enums.MusicGenre;
import java.util.List;

public class Venue {

    private String id;
    private String name;
    private List<MusicGenre> genres;
    private String location;
    private AvailabilityCalendar calendar;

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

    public void setCalendar(AvailabilityCalendar calendar) {
        this.calendar = calendar;
    }

    public Venue(String id, String name, List<MusicGenre> genres, String location) {
        this.id = id;
        this.name = name;
        this.genres = genres;
        this.location = location;
        calendar = new AvailabilityCalendar();
    }

}
