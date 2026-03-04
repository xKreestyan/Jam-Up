package org.jamup.factory;

import org.jamup.dao.interfaces.*;
import org.jamup.dao.db.*;

public class DBDAOFactory extends DAOFactory {

    @Override
    public VenueDAO createVenueDAO() {
        return new VenueDAODB();
    }

    @Override
    public ArtistDAO createArtistDAO() {
        return new ArtistDAODB();
    }

    @Override
    public ReservationDAO createReservationDAO() {
        return new ReservationDAODB();
    }

    @Override
    public UserDAO createUserDAO() {
        return new UserDAODB();
    }

    @Override
    public NotificationDAO createNotificationDAO() {
        return new NotificationDAODB();
    }

}