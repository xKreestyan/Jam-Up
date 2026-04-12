package org.jamup.dao.cache;

import org.jamup.dao.db.UserDAODB;
import org.jamup.dao.interfaces.UserDAO;
import org.jamup.model.Artist;
import org.jamup.model.User;
import org.jamup.model.VenueManager;

public class UserDAODBCache extends AbstractDAOCache<User> implements UserDAO {

    private final UserDAODB userDAOComponent;

    public UserDAODBCache() {
        userDAOComponent = new UserDAODB();
    }

    @Override
    public Artist findArtistByEmail(String email) {
        // Le query per email bypassano la cache hit iniziale perché la chiave è l'id
        Artist artistFromDB = userDAOComponent.findArtistByEmail(email);
        if (artistFromDB != null) {
            putInCache(artistFromDB.getId(), artistFromDB);
        }
        return artistFromDB;
    }

    @Override
    public VenueManager findManagerByEmail(String email) {
        // Le query per email bypassano la cache hit iniziale perché la chiave è l'id
        VenueManager managerFromDB = userDAOComponent.findManagerByEmail(email);
        if (managerFromDB != null) {
            putInCache(managerFromDB.getId(), managerFromDB);
        }
        return managerFromDB;
    }

    @Override
    public Artist findArtistById(String id) {
        if (isInCache(id)) {
            System.out.println("Dato preso dalla CACHE!");
            return (Artist) fetchFromCache(id);
        }

        System.out.println("Dato NON in cache, interrogo il DB...");
        Artist artistFromDB = userDAOComponent.findArtistById(id);
        if (artistFromDB != null) {
            putInCache(artistFromDB.getId(), artistFromDB);
        }
        return artistFromDB;
    }

    @Override
    public VenueManager findManagerById(String id) {
        if (isInCache(id)) {
            System.out.println("Dato preso dalla CACHE!");
            return (VenueManager) fetchFromCache(id);
        }

        System.out.println("Dato NON in cache, interrogo il DB...");
        VenueManager managerFromDB = userDAOComponent.findManagerById(id);
        if (managerFromDB != null) {
            putInCache(managerFromDB.getId(), managerFromDB);
        }
        return managerFromDB;
    }
}