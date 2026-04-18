package org.jamup.dao.factory;

import org.jamup.dao.interfaces.*;
import org.jamup.dao.cache.VenueDAOCache;
import org.jamup.dao.cache.ReservationDAOCache;
import org.jamup.dao.cache.UserDAOCache;
import org.jamup.dao.cache.NotificationDAOCache;
import org.jamup.dao.db.*;

public class DBDAOFactory extends DAOFactory {

    private VenueDAO venueDAOInstance;
    private ReservationDAO reservationDAOInstance;
    private UserDAO userDAOInstance;
    private NotificationDAO notificationDAOInstance;

    //each create method retrieves the singleton instance of the DAO with cache
    //(this is to maintain the cache for the entire duration of the user session)

    @Override
    public synchronized VenueDAO createVenueDAO() {
        if (venueDAOInstance == null) {
            venueDAOInstance = new VenueDAOCache(new VenueDAODB());
        }
        return venueDAOInstance;
    }

    @Override
    public synchronized ReservationDAO createReservationDAO() {
        if (reservationDAOInstance == null) {
            reservationDAOInstance = new ReservationDAOCache(new ReservationDAODB());
        }
        return reservationDAOInstance;
    }

    @Override
    public synchronized UserDAO createUserDAO() {
        if (userDAOInstance == null) {
            userDAOInstance = new UserDAOCache(new UserDAODB());
        }
        return userDAOInstance;
    }

    @Override
    public synchronized NotificationDAO createNotificationDAO() {
        if (notificationDAOInstance == null) {
            notificationDAOInstance = new NotificationDAOCache(new NotificationDAODB());
        }
        return notificationDAOInstance;
    }

    @Override
    public synchronized void clearCache() {
        venueDAOInstance = null;
        reservationDAOInstance = null;
        userDAOInstance = null;
        notificationDAOInstance = null;
    }

}