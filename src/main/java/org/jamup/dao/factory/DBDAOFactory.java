package org.jamup.dao.factory;

import org.jamup.dao.interfaces.*;
import org.jamup.dao.cache.VenueDAODBCache;
import org.jamup.dao.cache.ReservationDAODBCache;
import org.jamup.dao.cache.UserDAODBCache;
import org.jamup.dao.cache.NotificationDAODBCache;

public class DBDAOFactory extends DAOFactory {

    private VenueDAO venueDAOInstance;
    private ReservationDAO reservationDAOInstance;
    private UserDAO userDAOInstance;
    private NotificationDAO notificationDAOInstance;

    @Override
    public VenueDAO createVenueDAO() {
        if (venueDAOInstance == null) {
            venueDAOInstance = new VenueDAODBCache();
        }
        return venueDAOInstance;
    }

    @Override
    public ReservationDAO createReservationDAO() {
        if (reservationDAOInstance == null) {
            reservationDAOInstance = new ReservationDAODBCache();
        }
        return reservationDAOInstance;
    }

    @Override
    public UserDAO createUserDAO() {
        if (userDAOInstance == null) {
            userDAOInstance = new UserDAODBCache();
        }
        return userDAOInstance;
    }

    @Override
    public NotificationDAO createNotificationDAO() {
        if (notificationDAOInstance == null) {
            notificationDAOInstance = new NotificationDAODBCache();
        }
        return notificationDAOInstance;
    }

    @Override
    public void clearCache() {
        venueDAOInstance = null;
        reservationDAOInstance = null;
        userDAOInstance = null;
        notificationDAOInstance = null;
    }

}