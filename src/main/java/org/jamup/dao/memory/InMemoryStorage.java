package org.jamup.dao.memory;

import org.jamup.model.*;
import org.jamup.model.enums.Instrument;
import org.jamup.model.enums.MusicGenre;
import org.jamup.util.Encryptor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class InMemoryStorage {

    private static final List<Venue> venues = new ArrayList<>();
    private static final List<Reservation> reservations = new ArrayList<>();
    private static final List<Artist> artists = new ArrayList<>();
    private static final List<VenueManager> managers = new ArrayList<>();

    public static List<Venue> getVenues() {
        return venues;
    }

    public static List<Reservation> getReservations() {
        return reservations;
    }

    public static List<Artist> getArtists() {
        return artists;
    }

    public static List<VenueManager> getManagers() {
        return managers;
    }

    //population
    static {
        //venues
        Venue v1 = new Venue("1", "Jazz Club Milano",
                "Historic jazz club in the heart of Milan, featuring live performances every weekend.",
                List.of(MusicGenre.JAZZ, MusicGenre.BLUES), "Via Garibaldi 23, Milano", "1");
        v1.getCalendar().addSlot(LocalDate.of(2026, 3, 15), LocalTime.of(20, 0));
        v1.getCalendar().addSlot(LocalDate.of(2026, 3, 15), LocalTime.of(22, 0));
        venues.add(v1);

        Venue v2 = new Venue("2", "Rock Arena",
                "The loudest stage in Milan, home of rock and metal nights.",
                List.of(MusicGenre.ROCK, MusicGenre.METAL), "Corso Buenos Aires 45, Milano", "2");
        v2.getCalendar().addSlot(LocalDate.of(2026, 3, 16), LocalTime.of(21, 0));
        venues.add(v2);

        Venue v3 = new Venue("3", "Groove Bar",
                "Underground electronic venue with state-of-the-art sound system.",
                List.of(MusicGenre.ELECTRONIC, MusicGenre.HOUSE), "Via Torino 12, Milano", "3");
        v3.getCalendar().addSlot(LocalDate.of(2026, 3, 17), LocalTime.of(23, 0));
        venues.add(v3);

        Venue v4 = new Venue("4", "Live Stage Milano",
                "Versatile venue in the city center, open to all genres and emerging artists.",
                List.of(MusicGenre.POP, MusicGenre.INDIE), "Piazza Duomo 8, Milano", "4");
        v4.getCalendar().addSlot(LocalDate.of(2026, 3, 18), LocalTime.of(20, 30));
        v4.getCalendar().addSlot(LocalDate.of(2026, 3, 18), LocalTime.of(22, 30));
        venues.add(v4);

        //artist
        Artist a1 = new Artist("1", "mario.rossi@email.com", Encryptor.hash("artist123"),
                "Mario Rossi", List.of(Instrument.GUITAR), List.of(MusicGenre.JAZZ, MusicGenre.BLUES));
        artists.add(a1);

        //managers
        VenueManager m1 = new VenueManager("1", "Giuseppe Verdi", "manager1@email.com", Encryptor.hash("manager123"), List.of("1", "2"));
        managers.add(m1);

        VenueManager m2 = new VenueManager("2", "Antonio Vivaldi", "manager2@email.com", Encryptor.hash("manager456"), List.of("3", "4"));
        managers.add(m2);
    }

}