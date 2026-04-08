package org.jamup.model;

import org.jamup.model.enums.Instrument;
import org.jamup.model.enums.MusicGenre;

import java.util.List;

public class Artist extends User {

    private String name;
    private final List<Instrument> instruments;
    private List<MusicGenre> genres;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Instrument> getInstruments() {
        return instruments;
    }

    public List<MusicGenre> getGenres() {
        return genres;
    }

    public void setGenres(List<MusicGenre> genres) {
        this.genres = genres;
    }

    public Artist(String id, String email, String password, String name,
                  List<Instrument> instruments, List<MusicGenre> genres) {
        super(id, email, password);
        this.name = name;
        this.instruments = instruments;
        this.genres = genres;
    }

}
