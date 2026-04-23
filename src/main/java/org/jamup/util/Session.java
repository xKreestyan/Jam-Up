package org.jamup.util;

import org.jamup.model.User;
import org.jamup.model.Artist;
import org.jamup.model.VenueManager;

/**
 * Represents the state of the current user's session.
 */
public record Session(User currentUser) {

    /**
     * Gets the unique identifier of the currently logged-in user.
     *
     * @return the user ID, or {@code null} if no user is logged in.
     */
    public String getCurrentUserId() {
        return currentUser != null ? currentUser.getId() : null;
    }

    /**
     * Checks if the current user is an artist.
     *
     * @return {@code true} if the user is an instance of {@link Artist}, {@code false} otherwise.
     */
    public boolean isArtist() {
        return currentUser instanceof Artist;
    }

    /**
     * Checks if the current user is a venue manager.
     *
     * @return {@code true} if the user is an instance of {@link VenueManager}, {@code false} otherwise.
     */
    public boolean isManager() {
        return currentUser instanceof VenueManager;
    }

}