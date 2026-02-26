package org.jamup.dao.interfaces;

import org.jamup.model.Artist;
import org.jamup.model.VenueManager;

public interface UserDAO {

    Artist findArtistByEmail(String email);
    VenueManager findManagerByEmail(String email);

}