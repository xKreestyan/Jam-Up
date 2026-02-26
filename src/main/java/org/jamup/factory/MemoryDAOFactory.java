package org.jamup.factory;

import org.jamup.dao.interfaces.*;
import org.jamup.dao.memory.*;

public class MemoryDAOFactory extends DAOFactory {

    @Override
    public VenueDAO createVenueDAO() {
        return new VenueDAOMemory();
    }

    @Override
    public ArtistDAO createArtistDAO() {
        return new ArtistDAOMemory();
    }

    @Override
    public ReservationDAO createReservationDAO() {
        return new ReservationDAOMemory();
    }

    @Override
    public UserDAO createUserDAO() {
        return new UserDAOMemory();
    }

}
