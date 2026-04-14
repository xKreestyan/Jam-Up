package org.jamup.dao.cache;

import org.jamup.dao.db.ReservationDAODB;
import org.jamup.dao.interfaces.ReservationDAO;
import org.jamup.model.Reservation;
import org.jamup.model.enums.ReservationStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReservationDAODBCache extends AbstractDAOCache<Reservation> implements ReservationDAO {

    private final ReservationDAODB reservationDAOComponent;

    // Strutture dati per ottimizzare la ricerca per Venue
    private final Map<String, List<Reservation>> reservationsForVenue = new HashMap<>();
    private final List<String> fullyCachedVenues = new ArrayList<>();

    public ReservationDAODBCache() {
        reservationDAOComponent = new ReservationDAODB();
    }

    @Override
    public void putInCache(String reservationId, Reservation reservation) {
        super.putInCache(reservationId, reservation);

        // Aggiorniamo anche l'indice secondario
        String venueId = reservation.getVenue().getId();
        // Se la chiave non esiste già, creiamo una nuova lista vuota e l'associamo a quella chiave
        // Se già esiste, non facciamo nulla
        reservationsForVenue.putIfAbsent(venueId, new ArrayList<>());

        // Prendo il riferimento alla lista di notifiche associata alla chiave venueId
        List<Reservation> venueReservations = reservationsForVenue.get(venueId);
        // Evitiamo duplicati nell'indice (capitano quando aggiorniamo lo stato della prenotazione)
        boolean exists = false;
        for (int i = 0; i < venueReservations.size(); i++) {
            if (venueReservations.get(i).getId().equals(reservation.getId())) {
                venueReservations.set(i, reservation);
                exists = true;
                break;
            }
        }
        // Entriamo se non è stato rilevato un duplicato
        if (!exists) {
            venueReservations.add(reservation);
        }
    }

    @Override
    public void save(Reservation reservation) {
        //updates db immediately
        reservationDAOComponent.save(reservation);
        //cache invalidation
        putInCache(reservation.getId(), reservation);
    }

    @Override
    public Reservation findById(String id) {
        //cache hit case
        if (isInCache(id)) {
            System.out.println("Prenotazione presa dalla CACHE!");
            return fetchFromCache(id);
        }

        //cache miss case
        System.out.println("Prenotazione NON in cache, interrogo il DB...");
        Reservation reservationFromDB = reservationDAOComponent.findById(id);
        if (reservationFromDB != null) {
            putInCache(reservationFromDB.getId(), reservationFromDB);
        }

        return reservationFromDB;
    }

    @Override
    public List<Reservation> findByVenues(List<String> venueIds, ReservationStatus status) {
        if (venueIds == null || venueIds.isEmpty()) {
            return new ArrayList<>();
        }

        List<Reservation> finalResults = new ArrayList<>();
        List<String> missingVenueIds = new ArrayList<>();

        // 1. Processa i dati già in cache (cache hit)
        processVenueIdsForCache(venueIds, status, finalResults, missingVenueIds);

        // 2. Scarica dal DB solo i dati mancanti (cache miss)
        fetchMissingVenuesFromDB(missingVenueIds, status, finalResults);

        return finalResults;
    }

    private void processVenueIdsForCache(List<String> venueIds, ReservationStatus status, 
                                         List<Reservation> finalResults, List<String> missingVenueIds) {
        // Controlliamo quali venue sono in cache e quali mancano
        for (String venueId : venueIds) {
            if (fullyCachedVenues.contains(venueId)) {
                System.out.println("Prenotazioni per il locale " + venueId + " prese dalla CACHE!");
                List<Reservation> cachedReservations = reservationsForVenue.getOrDefault(venueId, new ArrayList<>());
                // Filtriamo per status (se specificato) e aggiungiamo ai risultati finali
                finalResults.addAll(filterByStatus(cachedReservations, status));
            } else {
                missingVenueIds.add(venueId);
            }
        }
    }

    private void fetchMissingVenuesFromDB(List<String> missingVenueIds, ReservationStatus status, List<Reservation> finalResults) {
        // Se ci sono venue non in cache, interroghiamo il DB SOLO per quelle mancanti
        if (missingVenueIds.isEmpty()) {
            return;
        }

        System.out.println("Prenotazioni per " + missingVenueIds.size() + " locali NON in cache, interrogo il DB...");
        
        // Attenzione: al DB chiediamo TUTTE le prenotazioni (status null) per poterle salvare interamente in cache
        List<Reservation> dbResults = reservationDAOComponent.findByVenues(missingVenueIds, null);
        
        // Le salviamo in cache
        for (Reservation reservation : dbResults) {
            putInCache(reservation.getId(), reservation);
        }
        
        // Segniamo i venue come completamente cachati
        fullyCachedVenues.addAll(missingVenueIds);
        
        // Aggiungiamo ai risultati finali solo quelle che matchano lo status richiesto
        finalResults.addAll(filterByStatus(dbResults, status));
    }

    private List<Reservation> filterByStatus(List<Reservation> reservations, ReservationStatus status) {
        if (status == null) {
            return reservations;
        }
        return reservations.stream()
                .filter(r -> r.getStatus() == status)
                .toList();
    }

    @Override
    public void update(Reservation reservation) {
        reservationDAOComponent.update(reservation);
        putInCache(reservation.getId(), reservation);
    }
}