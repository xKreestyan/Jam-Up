package org.jamup.dao.cache;

import org.jamup.dao.interfaces.VenueDAO;
import org.jamup.model.Venue;
import org.jamup.model.enums.MusicGenre;

import java.time.LocalDate;
import java.util.List;

public class VenueDAOCache extends AbstractDAOCache<Venue> implements VenueDAO {

    private final VenueDAO venueDAOComponent;

    public VenueDAOCache(VenueDAO venueDAOComponent) {
        this.venueDAOComponent = venueDAOComponent;
    }

    /**
     * Retrieves a Venue by its unique identifier, checking the cache first.
     *
     * @param id the unique identifier of the venue
     * @return the Venue if found in cache or storage, null otherwise
     */
    @Override
    public Venue findById(String id) {
        //cache hit case
        if (isInCache(id)) {
            System.out.println("Venue fetched from CACHE!");
            return fetchFromCache(id);
        }

        //cache miss case
        System.out.println("Venue NOT in cache, querying storage...");
        Venue venueFromStorage = venueDAOComponent.findById(id);
        if (venueFromStorage != null) {
            putInCache(venueFromStorage.getId(), venueFromStorage);
        }

        return venueFromStorage;
    }

    /**
     * Updates the venue in the underlying storage and refreshes the cache.
     *
     * @param updatedVenue the venue object containing updated information
     */
    @Override
    public void update(Venue updatedVenue) {
        //updates storage immediately
        venueDAOComponent.update(updatedVenue);
        
        //cache invalidation
        putInCache(updatedVenue.getId(), updatedVenue);
    }

    /**
     * Searches for venues based on criteria. This operation queries the underlying storage
     * directly and updates the cache with the retrieved individual venue objects.
     *
     * @param searchName   the name or partial name to search for
     * @param searchGenres the list of music genres to filter by
     * @param searchDate   the date to check for availability or events
     * @return a list of venues matching the criteria
     */
    @Override
    public List<Venue> findByCriteria(String searchName, List<MusicGenre> searchGenres, LocalDate searchDate) {
        //bypass the initial cache hit as the query is too complex
        List<Venue> results = venueDAOComponent.findByCriteria(searchName, searchGenres, searchDate);

        //put results in cache
        for (Venue venue : results) {
            putInCache(venue.getId(), venue);
        }

        return results;
    }

}