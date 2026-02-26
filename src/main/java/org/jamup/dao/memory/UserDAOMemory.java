package org.jamup.dao.memory;

import org.jamup.dao.interfaces.UserDAO;
import org.jamup.model.Artist;
import org.jamup.model.VenueManager;

public class UserDAOMemory implements UserDAO {

    @Override
    public Artist findArtistByEmail(String email) {
        for (Artist artist : InMemoryStorage.getArtists()) {
            if (artist.getEmail().equals(email)) {
                return artist;
            }
        }
        return null;
    }

    @Override
    public VenueManager findManagerByEmail(String email) {
        for (VenueManager manager : InMemoryStorage.getManagers()) {
            if (manager.getEmail().equals(email)) {
                return manager;
            }
        }
        return null;
    }
}