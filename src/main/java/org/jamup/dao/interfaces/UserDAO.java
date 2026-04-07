package org.jamup.dao.interfaces;

import org.jamup.model.Artist;
import org.jamup.model.VenueManager;

public interface UserDAO {

    // ============================================================
    // LOGIN
    // ============================================================
    /**
     * Finds an Artist by their email address.
     *
     * @param email the email address of the artist
     * @return the Artist object if found, null otherwise
     */
    Artist findArtistByEmail(String email);

    /**
     * Finds a VenueManager by their email address.
     *
     * @param email the email address of the venue manager
     * @return the VenueManager object if found, null otherwise
     */
    VenueManager findManagerByEmail(String email);

    // ============================================================
    // SEARCH BY ID
    // ============================================================
    /**
     * Finds an Artist by their unique identifier.
     *
     * @param id the unique ID of the artist
     * @return the Artist object if found, null otherwise
     */
    Artist findArtistById(String id);

    /**
     * Finds a VenueManager by their unique identifier.
     *
     * @param id the unique ID of the venue manager
     * @return the VenueManager object if found, null otherwise
     */
    VenueManager findManagerById(String id);
}