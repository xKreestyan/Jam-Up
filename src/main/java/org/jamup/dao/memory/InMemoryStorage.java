package org.jamup.dao.memory;

import org.jamup.model.*;
import org.jamup.model.enums.Instrument;
import org.jamup.model.enums.MusicGenre;
import org.jamup.util.Encryptor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class InMemoryStorage {

    private static final Map<String, Venue> venues = new HashMap<>();
    private static final Map<String, Reservation> reservations = new HashMap<>();
    private static final Map<String, Artist> artists = new HashMap<>();
    private static final Map<String, VenueManager> managers = new HashMap<>();
    private static final Map<String, Notification> notifications = new HashMap<>();

    //utility method for adding multiple time slots at once
    private static void addSlots(Venue venue, LocalDate date, LocalTime... times) {
        for (LocalTime time : times) {
            venue.getCalendar().addSlot(date, time);
        }
    }

    public static Map<String, Venue> getVenues() {
        return venues;
    }

    public static Map<String, Reservation> getReservations() {
        return reservations;
    }

    public static Map<String, Artist> getArtists() {
        return artists;
    }

    public static Map<String, VenueManager> getManagers() {
        return managers;
    }

    public static Map<String, Notification> getNotifications() {
        return notifications;
    }

    //population
    static {
        //managers ids
        String m1Id = UUID.randomUUID().toString();
        String m2Id = UUID.randomUUID().toString();
        
        VenueManager m1 = new VenueManager(m1Id, "manager1@email.com", Encryptor.hash("manager123"), new ArrayList<>());
        managers.put(m1.getId(), m1);

        VenueManager m2 = new VenueManager(m2Id, "manager2@email.com", Encryptor.hash("manager456"), new ArrayList<>());
        managers.put(m2.getId(), m2);

        //venues
        Venue v1 = new Venue(UUID.randomUUID().toString(), "Jazz Club Milano",
                "Historic jazz club in the heart of Milan, featuring live performances every weekend.",
                List.of(MusicGenre.JAZZ, MusicGenre.BLUES), "Via Garibaldi 23, Milano", m1);
        addSlots(v1, LocalDate.of(2026, 3, 15), LocalTime.of(20, 0), LocalTime.of(22, 0));
        addSlots(v1, LocalDate.of(2026, 3, 20), LocalTime.of(21, 0));
        addSlots(v1, LocalDate.of(2026, 3, 22), LocalTime.of(20, 0), LocalTime.of(22, 0));
        addSlots(v1, LocalDate.of(2026, 3, 27), LocalTime.of(21, 0), LocalTime.of(23, 0));
        addSlots(v1, LocalDate.of(2026, 4, 3),  LocalTime.of(20, 0), LocalTime.of(22, 0));
        addSlots(v1, LocalDate.of(2026, 4, 10), LocalTime.of(20, 0), LocalTime.of(22, 0));
        addSlots(v1, LocalDate.of(2026, 4, 17), LocalTime.of(21, 0));
        venues.put(v1.getId(), v1);

        Venue v2 = new Venue(UUID.randomUUID().toString(), "Rock Arena",
                "The loudest stage in Milan, home of rock and metal nights.",
                List.of(MusicGenre.ROCK, MusicGenre.METAL), "Corso Buenos Aires 45, Milano", m1);
        addSlots(v2, LocalDate.of(2026, 3, 16), LocalTime.of(21, 0), LocalTime.of(23, 0));
        addSlots(v2, LocalDate.of(2026, 3, 21), LocalTime.of(22, 0));
        addSlots(v2, LocalDate.of(2026, 3, 28), LocalTime.of(21, 0), LocalTime.of(23, 0));
        addSlots(v2, LocalDate.of(2026, 4, 4),  LocalTime.of(21, 0), LocalTime.of(23, 0));
        addSlots(v2, LocalDate.of(2026, 4, 11), LocalTime.of(22, 0));
        addSlots(v2, LocalDate.of(2026, 4, 18), LocalTime.of(21, 0), LocalTime.of(23, 0));
        addSlots(v2, LocalDate.of(2026, 4, 25), LocalTime.of(22, 0));
        venues.put(v2.getId(), v2);
        
        m1.setVenues(List.of(v1, v2));

        Venue v3 = new Venue(UUID.randomUUID().toString(), "Groove Bar",
                "Underground electronic venue with state-of-the-art sound system.",
                List.of(MusicGenre.ELECTRONIC, MusicGenre.HOUSE), "Via Torino 12, Milano", m2);
        addSlots(v3, LocalDate.of(2026, 3, 17), LocalTime.of(22, 0), LocalTime.of(23, 30));
        addSlots(v3, LocalDate.of(2026, 3, 19), LocalTime.of(23, 0));
        addSlots(v3, LocalDate.of(2026, 3, 24), LocalTime.of(22, 0), LocalTime.of(23, 30));
        addSlots(v3, LocalDate.of(2026, 3, 26), LocalTime.of(23, 0));
        addSlots(v3, LocalDate.of(2026, 4, 2),  LocalTime.of(22, 0), LocalTime.of(23, 30));
        addSlots(v3, LocalDate.of(2026, 4, 9),  LocalTime.of(23, 0));
        addSlots(v3, LocalDate.of(2026, 4, 16), LocalTime.of(22, 0), LocalTime.of(23, 30));
        venues.put(v3.getId(), v3);

        Venue v4 = new Venue(UUID.randomUUID().toString(), "Live Stage Milano",
                "Versatile venue in the city center, open to all genres and emerging artists.",
                List.of(MusicGenre.POP, MusicGenre.INDIE), "Piazza Duomo 8, Milano", m2);
        addSlots(v4, LocalDate.of(2026, 3, 18), LocalTime.of(19, 0), LocalTime.of(20, 30), LocalTime.of(22, 30));
        addSlots(v4, LocalDate.of(2026, 3, 25), LocalTime.of(19, 0), LocalTime.of(21, 0));
        addSlots(v4, LocalDate.of(2026, 4, 1),  LocalTime.of(19, 0), LocalTime.of(20, 30), LocalTime.of(22, 30));
        addSlots(v4, LocalDate.of(2026, 4, 8),  LocalTime.of(19, 0), LocalTime.of(21, 0));
        addSlots(v4, LocalDate.of(2026, 4, 15), LocalTime.of(19, 0), LocalTime.of(20, 30), LocalTime.of(22, 30));
        addSlots(v4, LocalDate.of(2026, 4, 22), LocalTime.of(20, 0), LocalTime.of(22, 0));
        venues.put(v4.getId(), v4);
        
        m2.setVenues(List.of(v3, v4));

        //artist
        Artist a1 = new Artist(UUID.randomUUID().toString(), "mario.rossi@email.com", Encryptor.hash("artist123"),
                "Mario Rossi", List.of(Instrument.GUITAR), List.of(MusicGenre.JAZZ, MusicGenre.BLUES));
        artists.put(a1.getId(), a1);
    }

    private InMemoryStorage() {
        /* This utility class should not be instantiated */
    }

}