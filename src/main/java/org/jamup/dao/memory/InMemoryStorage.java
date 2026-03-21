package org.jamup.dao.memory;

import org.jamup.model.*;
import org.jamup.model.enums.Instrument;
import org.jamup.model.enums.MusicGenre;
import org.jamup.util.Encryptor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InMemoryStorage {

    private static final List<Venue> venues = new ArrayList<>();
    private static final List<Reservation> reservations = new ArrayList<>();
    private static final List<Artist> artists = new ArrayList<>();
    private static final List<VenueManager> managers = new ArrayList<>();
    private static final List<Notification> notifications = new ArrayList<>();

    //utility method for adding multiple time slots at once
    private static void addSlots(Venue venue, LocalDate date, LocalTime... times) {
        for (LocalTime time : times) {
            venue.getCalendar().addSlot(date, time);
        }
    }

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

    public static List<Notification> getNotifications() {
        return notifications;
    }

    //population
    static {
        //managers ids
        String m1Id = UUID.randomUUID().toString();
        String m2Id = UUID.randomUUID().toString();

        //venues
        Venue v1 = new Venue(UUID.randomUUID().toString(), "Jazz Club Milano",
                "Historic jazz club in the heart of Milan, featuring live performances every weekend.",
                List.of(MusicGenre.JAZZ, MusicGenre.BLUES), "Via Garibaldi 23, Milano", m1Id);
        addSlots(v1, LocalDate.of(2026, 3, 15), LocalTime.of(20, 0), LocalTime.of(22, 0));
        addSlots(v1, LocalDate.of(2026, 3, 20), LocalTime.of(21, 0));
        addSlots(v1, LocalDate.of(2026, 3, 22), LocalTime.of(20, 0), LocalTime.of(22, 0));
        addSlots(v1, LocalDate.of(2026, 3, 27), LocalTime.of(21, 0), LocalTime.of(23, 0));
        addSlots(v1, LocalDate.of(2026, 4, 3),  LocalTime.of(20, 0), LocalTime.of(22, 0));
        addSlots(v1, LocalDate.of(2026, 4, 10), LocalTime.of(20, 0), LocalTime.of(22, 0));
        addSlots(v1, LocalDate.of(2026, 4, 17), LocalTime.of(21, 0));
        venues.add(v1);

        Venue v2 = new Venue(UUID.randomUUID().toString(), "Rock Arena",
                "The loudest stage in Milan, home of rock and metal nights.",
                List.of(MusicGenre.ROCK, MusicGenre.METAL), "Corso Buenos Aires 45, Milano", m1Id);
        addSlots(v2, LocalDate.of(2026, 3, 16), LocalTime.of(21, 0), LocalTime.of(23, 0));
        addSlots(v2, LocalDate.of(2026, 3, 21), LocalTime.of(22, 0));
        addSlots(v2, LocalDate.of(2026, 3, 28), LocalTime.of(21, 0), LocalTime.of(23, 0));
        addSlots(v2, LocalDate.of(2026, 4, 4),  LocalTime.of(21, 0), LocalTime.of(23, 0));
        addSlots(v2, LocalDate.of(2026, 4, 11), LocalTime.of(22, 0));
        addSlots(v2, LocalDate.of(2026, 4, 18), LocalTime.of(21, 0), LocalTime.of(23, 0));
        addSlots(v2, LocalDate.of(2026, 4, 25), LocalTime.of(22, 0));
        venues.add(v2);

        Venue v3 = new Venue(UUID.randomUUID().toString(), "Groove Bar",
                "Underground electronic venue with state-of-the-art sound system.",
                List.of(MusicGenre.ELECTRONIC, MusicGenre.HOUSE), "Via Torino 12, Milano", m2Id);
        addSlots(v3, LocalDate.of(2026, 3, 17), LocalTime.of(22, 0), LocalTime.of(23, 30));
        addSlots(v3, LocalDate.of(2026, 3, 19), LocalTime.of(23, 0));
        addSlots(v3, LocalDate.of(2026, 3, 24), LocalTime.of(22, 0), LocalTime.of(23, 30));
        addSlots(v3, LocalDate.of(2026, 3, 26), LocalTime.of(23, 0));
        addSlots(v3, LocalDate.of(2026, 4, 2),  LocalTime.of(22, 0), LocalTime.of(23, 30));
        addSlots(v3, LocalDate.of(2026, 4, 9),  LocalTime.of(23, 0));
        addSlots(v3, LocalDate.of(2026, 4, 16), LocalTime.of(22, 0), LocalTime.of(23, 30));
        venues.add(v3);

        Venue v4 = new Venue(UUID.randomUUID().toString(), "Live Stage Milano",
                "Versatile venue in the city center, open to all genres and emerging artists.",
                List.of(MusicGenre.POP, MusicGenre.INDIE), "Piazza Duomo 8, Milano", m2Id);
        addSlots(v4, LocalDate.of(2026, 3, 18), LocalTime.of(19, 0), LocalTime.of(20, 30), LocalTime.of(22, 30));
        addSlots(v4, LocalDate.of(2026, 3, 25), LocalTime.of(19, 0), LocalTime.of(21, 0));
        addSlots(v4, LocalDate.of(2026, 4, 1),  LocalTime.of(19, 0), LocalTime.of(20, 30), LocalTime.of(22, 30));
        addSlots(v4, LocalDate.of(2026, 4, 8),  LocalTime.of(19, 0), LocalTime.of(21, 0));
        addSlots(v4, LocalDate.of(2026, 4, 15), LocalTime.of(19, 0), LocalTime.of(20, 30), LocalTime.of(22, 30));
        addSlots(v4, LocalDate.of(2026, 4, 22), LocalTime.of(20, 0), LocalTime.of(22, 0));
        venues.add(v4);

        //artist
        Artist a1 = new Artist(UUID.randomUUID().toString(), "mario.rossi@email.com", Encryptor.hash("artist123"),
                "Mario Rossi", List.of(Instrument.GUITAR), List.of(MusicGenre.JAZZ, MusicGenre.BLUES));
        artists.add(a1);

        //managers
        VenueManager m1 = new VenueManager(m1Id, "Giuseppe Verdi", "manager1@email.com", Encryptor.hash("manager123"), List.of(v1.getId(), v2.getId()));
        managers.add(m1);

        VenueManager m2 = new VenueManager(m2Id, "Antonio Vivaldi", "manager2@email.com", Encryptor.hash("manager456"), List.of(v3.getId(), v4.getId()));
        managers.add(m2);
    }

}