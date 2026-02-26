package org.jamup.dao.interfaces;

import org.jamup.model.Artist;

public interface ArtistDAO {

    Artist findById(String id);

}