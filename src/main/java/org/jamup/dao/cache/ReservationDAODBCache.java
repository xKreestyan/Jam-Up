package org.jamup.dao.cache;

import org.jamup.dao.db.ReservationDAODB;
import org.jamup.dao.interfaces.ReservationDAO;
import org.jamup.model.Reservation;
import org.jamup.model.enums.ReservationStatus;

import java.util.List;

public class ReservationDAODBCache extends AbstractDAOCache<Reservation> implements ReservationDAO {

    private final ReservationDAODB reservationDAOComponent;

    public ReservationDAODBCache() {
        reservationDAOComponent = new ReservationDAODB();
    }

    @Override
    public void save(Reservation reservation) {
        reservationDAOComponent.save(reservation);
        putInCache(reservation.getId(), reservation);
    }

    @Override
    public Reservation findById(String id) {
        if (isInCache(id)) {
            System.out.println("Dato preso dalla CACHE!");
            return fetchFromCache(id);
        }

        System.out.println("Dato NON in cache, interrogo il DB...");
        Reservation reservationFromDB = reservationDAOComponent.findById(id);
        if (reservationFromDB != null) {
            putInCache(reservationFromDB.getId(), reservationFromDB);
        }

        return reservationFromDB;
    }

    @Override
    public List<Reservation> findByVenues(List<String> venueIds, ReservationStatus status) {
        List<Reservation> results = reservationDAOComponent.findByVenues(venueIds, status);
        for (Reservation reservation : results) {
            putInCache(reservation.getId(), reservation);
        }
        return results;
    }

    @Override
    public void update(Reservation reservation) {
        reservationDAOComponent.update(reservation);
        putInCache(reservation.getId(), reservation);
    }
}