package org.jamup.util;

import org.jamup.model.User;
import org.jamup.model.Artist;
import org.jamup.model.VenueManager;

/**
 * Rappresenta lo stato della sessione dell'utente corrente.
 */
public record Session(User currentUser) {

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