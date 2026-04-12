package org.jamup.dao.memory;

import org.jamup.dao.interfaces.ReservationDAO;
import org.jamup.model.Reservation;
import org.jamup.model.enums.ReservationStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReservationDAOMemory implements ReservationDAO {

    @Override
    public void save(Reservation newReservation) {
        String id = UUID.randomUUID().toString();
        newReservation.setId(id);
        InMemoryStorage.getReservations().put(id, newReservation);
    }

    @Override
    public Reservation findById(String id) {
        return InMemoryStorage.getReservations().get(id);
    }

    @Override
    public List<Reservation> findByVenues(List<String> venueIds, ReservationStatus status) {
        List<Reservation> results = new ArrayList<>();
        for (Reservation reservation : InMemoryStorage.getReservations().values()) {
            //if status is not specified, it takes all reservations
            //otherwise it only takes those with the specific status
            if (venueIds.contains(reservation.getVenue().getId()) && (status == null || reservation.getStatus() == status)) {
                    results.add(reservation);
                }

        }
        return results;
    }

    //update of the i-th reservation following a status change
    @Override
    public void update(Reservation updatedReservation) {
        InMemoryStorage.getReservations().put(updatedReservation.getId(), updatedReservation);
    }

    public ReservationDAOMemory() {
        System.out.println("Creato ReservationDAO versione DEMO");
    }

}