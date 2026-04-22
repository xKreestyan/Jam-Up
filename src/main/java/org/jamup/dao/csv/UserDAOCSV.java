package org.jamup.dao.csv;

import org.jamup.dao.interfaces.UserDAO;
import org.jamup.dao.interfaces.VenueDAO;
import org.jamup.dao.factory.DAOFactory;
import org.jamup.model.Artist;
import org.jamup.model.Venue;
import org.jamup.model.VenueManager;
import org.jamup.model.enums.Instrument;
import org.jamup.model.enums.MusicGenre;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDAOCSV implements UserDAO {

    private static final String ARTISTS_FILE = "artists.csv";
    private static final String MANAGERS_FILE = "managers.csv";

    // Map to prevent infinite recursion during manager and venue construction.
    // When building a VenueManager, VenueDAO is invoked to build its Venues.
    // VenueDAO in turn asks UserDAO for the manager, triggering a loop.
    // This map holds the partial manager being built to break the cycle.
    private final Map<String, VenueManager> managersInProgress = new HashMap<>();

    //artists.csv: id, email, password, name, instruments, genres
    /**
     * Converts a CSV row array into an Artist object.
     *
     * @param row An array of strings representing a row from the artists CSV file.
     * @return An Artist object populated with the data from the row.
     */
    private Artist rowToArtist(String[] row) {
        String id           = row[0];
        String email        = row[1];
        String password     = row[2];
        String name         = row[3];

        List<Instrument> instruments = new ArrayList<>();
        for (String i : row[4].split("\\|")) {
            instruments.add(Instrument.valueOf(i));
        }

        List<MusicGenre> genres = new ArrayList<>();
        for (String g : row[5].split("\\|")) {
            genres.add(MusicGenre.valueOf(g));
        }

        return new Artist(id, email, password, name, instruments, genres);
    }

    //managers.csv: id, name, email, password, venueIds
    /**
     * Converts a CSV row array into a VenueManager object.
     *
     * @param row An array of strings representing a row from the managers CSV file.
     * @return A VenueManager object populated with the data from the row.
     */
    private VenueManager rowToManager(String[] row) {
        String id       = row[0];
        String email    = row[2];
        String password = row[3];

        // Check if we are already building this manager
        if (managersInProgress.containsKey(id)) {
            return managersInProgress.get(id);
        }

        VenueManager manager = new VenueManager(id, email, password, new ArrayList<>());
        managersInProgress.put(id, manager);

        List<Venue> venues = new ArrayList<>();
        if (!row[4].isEmpty()) {
            String[] venueIds = row[4].split("\\|");
            VenueDAO venueDAO = DAOFactory.getInstance().createVenueDAO();
            for (String venueId : venueIds) {
                Venue venue = venueDAO.findById(venueId);
                if (venue != null) {
                    venues.add(venue);
                }
            }
        }

        manager.setVenues(venues);
        managersInProgress.remove(id);

        return manager;
    }

    @Override
    public Artist findArtistByEmail(String email) {
        for (String[] row : CSVStorage.read(ARTISTS_FILE)) {
            if (row[1].equals(email)) {
                return rowToArtist(row);
            }
        }
        return null;
    }

    @Override
    public VenueManager findManagerByEmail(String email) {
        for (String[] row : CSVStorage.read(MANAGERS_FILE)) {
            if (row[2].equals(email)) {
                return rowToManager(row);
            }
        }
        return null;
    }

    @Override
    public Artist findArtistById(String id) {
        for (String[] row : CSVStorage.read(ARTISTS_FILE)) {
            if (row[0].equals(id)) {
                return rowToArtist(row);
            }
        }
        return null;
    }

    @Override
    public VenueManager findManagerById(String id) {
        for (String[] row : CSVStorage.read(MANAGERS_FILE)) {
            if (row[0].equals(id)) {
                return rowToManager(row);
            }
        }
        return null;
    }

}