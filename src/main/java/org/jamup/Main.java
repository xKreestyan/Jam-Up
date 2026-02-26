package org.jamup;

import org.jamup.dao.interfaces.UserDAO;
import org.jamup.dao.interfaces.VenueDAO;
import org.jamup.exception.InvalidCredentialsException;
import org.jamup.factory.DAOFactory;
import org.jamup.model.Venue;
import org.jamup.model.enums.MusicGenre;
import org.jamup.util.SessionManager;

import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        //TEST DELLE CLASSI

        // 1. inizializza la factory in modalità DEMO
        DAOFactory.getInstance("DEMO");
        System.out.println("DAOFactory initialized in DEMO mode");

        // 2. test login artista corretto
        UserDAO userDAO = DAOFactory.getInstance().createUserDAO();
        try {
            SessionManager.getInstance().login("mario.rossi@email.com", "artist123", userDAO);
            System.out.println("Artist login OK — currentArtistId: " + SessionManager.getInstance().getCurrentArtistId());
        } catch (InvalidCredentialsException e) {
            System.out.println("Login FAILED: " + e.getMessage());
        }

        // 3. test login con password sbagliata
        try {
            SessionManager.getInstance().logout();
            SessionManager.getInstance().login("mario.rossi@email.com", "wrongpassword", userDAO);
            System.out.println("Artist login OK");
        } catch (InvalidCredentialsException e) {
            System.out.println("Wrong password test OK — exception caught: " + e.getMessage());
        }

        // 4. test login manager corretto
        try {
            SessionManager.getInstance().login("manager1@email.com", "manager123", userDAO);
            System.out.println("Manager login OK — currentManagerId: " + SessionManager.getInstance().getCurrentManagerId());
        } catch (InvalidCredentialsException e) {
            System.out.println("Login FAILED: " + e.getMessage());
        }

        // 5. test ricerca venue
        VenueDAO venueDAO = DAOFactory.getInstance().createVenueDAO();
        List<Venue> results = venueDAO.findByCriteria(null, List.of(MusicGenre.JAZZ), null, null);
        System.out.println("Venues found with genre JAZZ: " + results.size());
        for (Venue v : results) {
            System.out.println(" - " + v.getName() + " | " + v.getLocation());
        }

        // 6. test ricerca venue per data
        List<Venue> byDate = venueDAO.findByCriteria(null, null, null, LocalDate.of(2026, 3, 15));
        System.out.println("Venues available on 2026-03-15: " + byDate.size());
        for (Venue v : byDate) {
            System.out.println(" - " + v.getName());
        }

        // 7. test venue non trovato
        Venue notFound = venueDAO.findById("999");
        System.out.println("Venue with id 999: " + (notFound == null ? "not found OK" : "ERROR"));
    }
}