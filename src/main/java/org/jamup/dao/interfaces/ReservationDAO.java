package org.jamup.dao.interfaces;

import org.jamup.model.Reservation;

import java.util.List;

public interface ReservationDAO {

    String save(Reservation reservation);
    Reservation findById(String id);
    List<Reservation> findPendingByVenue(String venueId);
    void update(Reservation reservation);

}