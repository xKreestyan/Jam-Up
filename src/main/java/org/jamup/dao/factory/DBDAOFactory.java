package org.jamup.dao.factory;

import org.jamup.dao.interfaces.*;
import org.jamup.dao.cache.VenueDAODBCache;
import org.jamup.dao.cache.ReservationDAODBCache;
import org.jamup.dao.cache.UserDAODBCache;
import org.jamup.dao.cache.NotificationDAODBCache;

public class DBDAOFactory extends DAOFactory {

    @Override
    public VenueDAO createVenueDAO() {
        return new VenueDAODBCache();
    }

    @Override
    public ReservationDAO createReservationDAO() {
        return new ReservationDAODBCache();
    }

    @Override
    public UserDAO createUserDAO() {
        return new UserDAODBCache();
    }

    @Override
    public NotificationDAO createNotificationDAO() {
        return new NotificationDAODBCache();
    }

}