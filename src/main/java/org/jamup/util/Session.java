package org.jamup.util;

import org.jamup.model.User;
import org.jamup.model.Artist;
import org.jamup.model.VenueManager;

/**
 * Rappresenta lo stato della sessione dell'utente corrente.
 */
public class Session {

    private final User currentUser;

    public Session(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public String getCurrentUserId() {
        return currentUser != null ? currentUser.getId() : null;
    }

    public boolean isArtist() {
        return currentUser instanceof Artist;
    }

    public boolean isManager() {
        return currentUser instanceof VenueManager;
    }

}