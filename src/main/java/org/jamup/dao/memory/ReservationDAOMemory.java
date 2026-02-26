package org.jamup.dao.memory;

import org.jamup.dao.interfaces.ReservationDAO;
import org.jamup.model.Reservation;
import org.jamup.model.enums.ReservationStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReservationDAOMemory implements ReservationDAO {

    @Override
    public String save(Reservation newReservation) {
        String id = UUID.randomUUID().toString();
        newReservation.setId(id);
        InMemoryStorage.getReservations().add(newReservation);
        return id;
    }

    @Override
    public Reservation findById(String id) {
        for (Reservation reservation : InMemoryStorage.getReservations()) {
            if (reservation.getId().equals(id)) {
                return reservation;
            }
        }
        return null;
    }

    @Override
    public List<Reservation> findPendingByVenue(String venueId) {
        List<Reservation> pendingReservations = new ArrayList<>();
        for (Reservation reservation : InMemoryStorage.getReservations()) {
            if (reservation.getVenue().getId().equals(venueId) &&
                    reservation.getStatus() == ReservationStatus.PENDING) {
                pendingReservations.add(reservation);
            }
        }
        return pendingReservations;
    }

    //update of the i-th reservation following a status change
    @Override
    public void update(Reservation updatedReservation) {
        List<Reservation> reservations = InMemoryStorage.getReservations();
        for (int i = 0; i < reservations.size(); i++) {
            if (reservations.get(i).getId().equals(updatedReservation.getId())) {
                reservations.set(i, updatedReservation);
                return;
            }
        }
    }

    public ReservationDAOMemory() {
        System.out.println("Creato ReservationDAO versione DEMO");
    }

}
