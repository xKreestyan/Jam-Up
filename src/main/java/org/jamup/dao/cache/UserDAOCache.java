package org.jamup.dao.cache;

import org.jamup.dao.interfaces.UserDAO;
import org.jamup.model.Artist;
import org.jamup.model.User;
import org.jamup.model.VenueManager;

public class UserDAOCache extends AbstractDAOCache<User> implements UserDAO {

    private final UserDAO userDAOComponent;

    public UserDAOCache(UserDAO userDAOComponent) {
        this.userDAOComponent = userDAOComponent;
    }

    /**
     * Finds an Artist by their email. Bypasses initial cache hit as the cache is keyed by ID.
     *
     * @param email the email of the artist to find
     * @return the Artist if found, null otherwise
     */
    @Override
    public Artist findArtistByEmail(String email) {
        //queries by email bypass the initial cache hit because the key is the id
        Artist artistFromStorage = userDAOComponent.findArtistByEmail(email);
        if (artistFromStorage != null) {
            putInCache(artistFromStorage.getId(), artistFromStorage);
        }
        return artistFromStorage;
    }

    /**
     * Finds a VenueManager by their email. Bypasses initial cache hit as the cache is keyed by ID.
     *
     * @param email the email of the manager to find
     * @return the VenueManager if found, null otherwise
     */
    @Override
    public VenueManager findManagerByEmail(String email) {
        //queries by email bypass the initial cache hit because the key is the id
        VenueManager managerFromStorage = userDAOComponent.findManagerByEmail(email);
        if (managerFromStorage != null) {
            putInCache(managerFromStorage.getId(), managerFromStorage);
        }
        return managerFromStorage;
    }

    /**
     * Finds an Artist by their ID. Checks the cache first before querying storage.
     *
     * @param id the unique identifier of the artist
     * @return the Artist if found, null otherwise
     */
    @Override
    public Artist findArtistById(String id) {
        //cache hit case
        if (isInCache(id)) {
            System.out.println("Artist fetched from CACHE!");
            return (Artist) fetchFromCache(id);
        }

        //cache miss case
        System.out.println("Artist NOT in cache, querying storage...");
        Artist artistFromStorage = userDAOComponent.findArtistById(id);
        if (artistFromStorage != null) {
            putInCache(artistFromStorage.getId(), artistFromStorage);
        }
        return artistFromStorage;
    }

    /**
     * Finds a VenueManager by their ID. Checks the cache first before querying storage.
     *
     * @param id the unique identifier of the manager
     * @return the VenueManager if found, null otherwise
     */
    @Override
    public VenueManager findManagerById(String id) {
        //cache hit case
        if (isInCache(id)) {
            System.out.println("Manager fetched from CACHE!");
            return (VenueManager) fetchFromCache(id);
        }

        //cache miss case
        System.out.println("Manager NOT in cache, querying storage...");
        VenueManager managerFromStorage = userDAOComponent.findManagerById(id);
        if (managerFromStorage != null) {
            putInCache(managerFromStorage.getId(), managerFromStorage);
        }
        return managerFromStorage;
    }

}