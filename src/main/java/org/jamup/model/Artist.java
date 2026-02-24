package org.jamup.model;

import org.jamup.model.enums.Instrument;
import org.jamup.model.enums.MusicGenre;

import java.util.List;

public class Artist {

    private String id;
    private String name;
    private List<Instrument> instruments;
    private List<MusicGenre> genres;

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

    public List<Instrument> getInstruments() {
        return instruments;
    }

    public void setInstruments(List<Instrument> instruments) {
        this.instruments = instruments;
    }

    public List<MusicGenre> getGenres() {
        return genres;
    }

    public void setGenres(List<MusicGenre> genres) {
        this.genres = genres;
    }

    public Artist(String id, String name, List<Instrument> instruments, List<MusicGenre> genres) {
        this.id = id;
        this.name = name;
        this.instruments = instruments;
        this.genres = genres;
    }

}
