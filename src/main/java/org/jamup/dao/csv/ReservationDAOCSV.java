package org.jamup.dao.csv;

import org.jamup.dao.interfaces.ReservationDAO;
import org.jamup.model.Reservation;

import java.util.List;

public class ReservationDAOCSV implements ReservationDAO {

    public ReservationDAOCSV() {
        System.out.println("Creato ReservationDAO versione CSV");
    }

    @Override
    public void save(Reservation reservation) {
        /* TODO */
    }

    @Override
    public Reservation findById(String id) {
        /* TODO */
        return null;
    }

    @Override
    public List<Reservation> findPendingByVenue(String venueId) {
        /* TODO */
        return List.of();
    }

    @Override
    public void update(Reservation reservation) {
        /* TODO */
    }

}
