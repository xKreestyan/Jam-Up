package org.jamup.dao.db;

import org.jamup.dao.interfaces.ArtistDAO;
import org.jamup.model.Artist;

public class ArtistDAODB implements ArtistDAO {

    public ArtistDAODB() {
        System.out.println("Creato ArtistDAO versione DB");
    }

    @Override
    public Artist findById(String id) {
        /* TODO */
        return null;
    }

}
