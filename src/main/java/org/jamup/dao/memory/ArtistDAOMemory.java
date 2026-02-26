package org.jamup.dao.memory;

import org.jamup.dao.interfaces.ArtistDAO;
import org.jamup.model.Artist;

public class ArtistDAOMemory implements ArtistDAO {

    @Override
    public Artist findById(String id) {
        for (Artist artist : InMemoryStorage.getArtists()) {
            if (artist.getId().equals(id)) {
                return artist;
            }
        }
        return null;
    }

    public ArtistDAOMemory() {
        System.out.println("Creato ArtistDAO versione DEMO");
    }

}
