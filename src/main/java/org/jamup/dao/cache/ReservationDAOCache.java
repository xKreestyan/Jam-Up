package org.jamup.dao.cache;

import org.jamup.dao.interfaces.ReservationDAO;
import org.jamup.model.Reservation;
import org.jamup.model.enums.ReservationStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReservationDAOCache extends AbstractDAOCache<Reservation> implements ReservationDAO {

    private final ReservationDAO reservationDAOComponent;

    //data structures to optimize the search by Venue
    private final Map<String, List<Reservation>> reservationsForVenue = new HashMap<>();
    private final List<String> fullyCachedVenues = new ArrayList<>();

    public ReservationDAOCache(ReservationDAO reservationDAOComponent) {
        this.reservationDAOComponent = reservationDAOComponent;
    }

    /**
     * Adds or updates a reservation in the cache and maintains the secondary index by venue.
     *
     * @param reservationId the unique identifier of the reservation
     * @param reservation   the reservation object to cache
     */
    @Override
    public void putInCache(String reservationId, Reservation reservation) {
        super.putInCache(reservationId, reservation);

        //we also update the secondary index
        String venueId = reservation.getVenue().getId();
        //if the key does not exist, we create a new empty list and associate it with that key
        //if it already exists, we do nothing
        reservationsForVenue.putIfAbsent(venueId, new ArrayList<>());

        //get the reference to the list of reservations associated with the venueId key
        List<Reservation> venueReservations = reservationsForVenue.get(venueId);
        //we avoid duplicates in the index (they happen when we update the reservation status)
        boolean exists = false;
        for (int i = 0; i < venueReservations.size(); i++) {
            if (venueReservations.get(i).getId().equals(reservation.getId())) {
                venueReservations.set(i, reservation);
                exists = true;
                break;
            }
        }
        //we enter if a duplicate was not detected
        if (!exists) {
            venueReservations.add(reservation);
        }
    }

    /**
     * Saves a reservation to the persistent storage and updates the cache.
     *
     * @param reservation the reservation to save
     */
    @Override
    public void save(Reservation reservation) {
        //updates storage immediately
        reservationDAOComponent.save(reservation);
        //cache invalidation
        putInCache(reservation.getId(), reservation);
    }

    /**
     * Retrieves a reservation by its ID, checking the cache first.
     *
     * @param id the unique identifier of the reservation
     * @return the reservation if found, null otherwise
     */
    @Override
    public Reservation findById(String id) {
        //cache hit case
        if (isInCache(id)) {
            System.out.println("Reservation fetched from CACHE!");
            return fetchFromCache(id);
        }

        //cache miss case
        System.out.println("Reservation NOT in cache, querying storage...");
        Reservation reservationFromStorage = reservationDAOComponent.findById(id);
        if (reservationFromStorage != null) {
            putInCache(reservationFromStorage.getId(), reservationFromStorage);
        }

        return reservationFromStorage;
    }

    /**
     * Retrieves reservations for a list of venues, filtered by status.
     *
     * @param venueIds the list of venue IDs to search for
     * @param status   the status to filter by (optional)
     * @return a list of matching reservations
     */
    @Override
    public List<Reservation> findByVenues(List<String> venueIds, ReservationStatus status) {
        if (venueIds == null || venueIds.isEmpty()) {
            return new ArrayList<>();
        }

        List<Reservation> finalResults = new ArrayList<>();
        List<String> missingVenueIds = new ArrayList<>();

        //1. process the data already in cache (cache hit)
        processVenueIdsForCache(venueIds, status, finalResults, missingVenueIds);

        //2. fetch from storage only the missing data (cache miss)
        fetchMissingVenuesFromStorage(missingVenueIds, status, finalResults);

        return finalResults;
    }

    /**
     * Checks which venue IDs are already fully cached and populates results from cache.
     *
     * @param venueIds        the list of venue IDs to check
     * @param status          the status filter
     * @param finalResults    the list to add cached results to
     * @param missingVenueIds the list to add IDs not found in cache to
     */
    private void processVenueIdsForCache(List<String> venueIds, ReservationStatus status,
                                         List<Reservation> finalResults, List<String> missingVenueIds) {
        //we check which venues are in cache and which are missing
        for (String venueId : venueIds) {
            if (fullyCachedVenues.contains(venueId)) {
                System.out.println("Reservations for venue " + venueId + " fetched from CACHE!");
                List<Reservation> cachedReservations = reservationsForVenue.getOrDefault(venueId, new ArrayList<>());
                //we filter by status (if specified) and add to the final results
                finalResults.addAll(filterByStatus(cachedReservations, status));
            } else {
                missingVenueIds.add(venueId);
            }
        }
    }

    /**
     * Fetches reservations for missing venues from storage and populates the cache.
     *
     * @param missingVenueIds the list of venue IDs not in cache
     * @param status          the status filter for the final results
     * @param finalResults    the list to add fetched results to
     */
    private void fetchMissingVenuesFromStorage(List<String> missingVenueIds, ReservationStatus status, List<Reservation> finalResults) {
        //if there are venues not in cache, we query the storage ONLY for the missing ones
        if (missingVenueIds.isEmpty()) {
            return;
        }

        System.out.println("Reservations for " + missingVenueIds.size() + " venues NOT in cache, querying storage...");
        
        //attention: we ask the storage for ALL reservations (status null) to be able to save them entirely in cache
        List<Reservation> storageResults = reservationDAOComponent.findByVenues(missingVenueIds, null);
        
        //we save them in cache
        for (Reservation reservation : storageResults) {
            putInCache(reservation.getId(), reservation);
        }
        
        //we mark the venues as fully cached
        fullyCachedVenues.addAll(missingVenueIds);
        
        //we add to the final results only those that match the requested status
        finalResults.addAll(filterByStatus(storageResults, status));
    }

    /**
     * Filters a list of reservations by the specified status.
     *
     * @param reservations the list to filter
     * @param status       the status to match
     * @return a filtered list of reservations
     */
    private List<Reservation> filterByStatus(List<Reservation> reservations, ReservationStatus status) {
        if (status == null) {
            return reservations;
        }
        return reservations.stream()
                .filter(r -> r.getStatus() == status)
                .toList();
    }

    /**
     * Updates an existing reservation in storage and refreshes the cache.
     *
     * @param reservation the reservation to update
     */
    @Override
    public void update(Reservation reservation) {
        reservationDAOComponent.update(reservation);
        putInCache(reservation.getId(), reservation);
    }

}