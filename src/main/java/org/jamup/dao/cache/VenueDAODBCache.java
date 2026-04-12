package org.jamup.dao.cache;

import org.jamup.dao.db.VenueDAODB;
import org.jamup.dao.interfaces.VenueDAO;
import org.jamup.model.Venue;
import org.jamup.model.enums.MusicGenre;

import java.time.LocalDate;
import java.util.List;

public class VenueDAODBCache extends AbstractDAOCache<Venue> implements VenueDAO {

    private final VenueDAODB venueDAOComponent;

    public VenueDAODBCache() {
        venueDAOComponent = new VenueDAODB();
    }

    @Override
    public Venue findById(String id) {
        //cache hit case
        if (isInCache(id)) {
            System.out.println("Dato preso dalla CACHE!");
            return fetchFromCache(id);
        }

        //cache miss case
        System.out.println("Dato NON in cache, interrogo il DB...");
        Venue venueFromDB = venueDAOComponent.findById(id);
        if (venueFromDB != null) {
            putInCache(venueFromDB.getId(), venueFromDB);
        }

        return venueFromDB;
    }

    @Override
    public void update(Venue updatedVenue) {
        //updates db immediately
        venueDAOComponent.update(updatedVenue);
        
        //cache invalidation
        putInCache(updatedVenue.getId(), updatedVenue);
    }

    @Override
    public List<Venue> findByCriteria(String searchName, List<MusicGenre> searchGenres, LocalDate searchDate) {
        //complex query
        List<Venue> results = venueDAOComponent.findByCriteria(searchName, searchGenres, searchDate);

        //put results in cache
        for (Venue venue : results) {
            putInCache(venue.getId(), venue);
        }

        return results;
    }

}