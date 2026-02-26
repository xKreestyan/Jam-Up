package org.jamup.factory;

import org.jamup.dao.interfaces.*;
import org.jamup.dao.csv.*;

public class CSVDAOFactory extends DAOFactory {

    @Override
    public VenueDAO createVenueDAO() {
        return new VenueDAOCSV();
    }

    @Override
    public ArtistDAO createArtistDAO() {
        return new ArtistDAOCSV();
    }

    @Override
    public ReservationDAO createReservationDAO() {
        return new ReservationDAOCSV();
    }

    @Override
    public UserDAO createUserDAO() {
        return new UserDAOCSV();
    }

}
