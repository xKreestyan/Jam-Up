package org.jamup.util;

import org.jamup.dao.interfaces.UserDAO;
import org.jamup.exception.InvalidCredentialsException;
import org.jamup.model.Artist;
import org.jamup.model.VenueManager;

public class SessionManager {

    private static SessionManager instance;

    private String currentArtistId;
    private String currentManagerId;

    //private constructor (singleton pattern)
    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void login(String email, String password, UserDAO userDAO) throws InvalidCredentialsException {
        String hashedPassword = Encryptor.hash(password);

        //search among artists
        Artist artist = userDAO.findArtistByEmail(email);
        if (artist != null && artist.getPassword().equals(hashedPassword)) {
            this.currentArtistId = artist.getId();
            return;
        }

        //search among managers
        VenueManager manager = userDAO.findManagerByEmail(email);
        if (manager != null && manager.getPassword().equals(hashedPassword)) {
            this.currentManagerId = manager.getId();
            return;
        }

        //no match found
        throw new InvalidCredentialsException();
    }

    public void logout() {
        this.currentArtistId = null;
        this.currentManagerId = null;
    }

    public String getCurrentArtistId() {
        return currentArtistId;
    }

    public String getCurrentManagerId() {
        return currentManagerId;
    }

    public boolean isArtistLoggedIn() {
        return currentArtistId != null;
    }

    public boolean isManagerLoggedIn() {
        return currentManagerId != null;
    }

}