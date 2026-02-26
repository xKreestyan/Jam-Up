package org.jamup.dao.csv;

import org.jamup.dao.interfaces.ArtistDAO;
import org.jamup.model.Artist;

public class ArtistDAOCSV implements ArtistDAO {

    public ArtistDAOCSV() {
        System.out.println("Creato ArtistDAO versione CSV");
    }

    @Override
    public Artist findById(String id) {
        /* TODO */
        return null;
    }

}
