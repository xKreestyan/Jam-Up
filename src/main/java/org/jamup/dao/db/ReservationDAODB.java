package org.jamup.dao.db;

import org.jamup.dao.interfaces.ReservationDAO;
import org.jamup.model.Reservation;

import java.util.List;

public class ReservationDAODB implements ReservationDAO {

    public ReservationDAODB() {
        System.out.println("Creato ReservationDAO versione DB");
    }

    @Override
    public String save(Reservation reservation) {
        /* TODO */
        return "";
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
