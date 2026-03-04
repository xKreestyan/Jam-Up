package org.jamup.dao.db;

import org.jamup.dao.interfaces.ReservationDAO;
import org.jamup.model.Reservation;
import org.jamup.model.enums.ReservationStatus;

import java.util.List;

public class ReservationDAODB implements ReservationDAO {

    public ReservationDAODB() {
        System.out.println("Creato ReservationDAO versione DB");
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
    public List<Reservation> findByVenues(List<String> venueIds, ReservationStatus status) {
        /* TODO */
        return List.of();
    }

    @Override
    public void update(Reservation reservation) {
        /* TODO */
    }

}
