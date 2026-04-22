package org.jamup.dao.db;

import org.jamup.dao.factory.DAOFactory;
import org.jamup.dao.factory.DBConnectionFactory;
import org.jamup.dao.interfaces.UserDAO;
import org.jamup.dao.interfaces.VenueDAO;
import org.jamup.exception.DAOException;
import org.jamup.model.Artist;
import org.jamup.model.Venue;
import org.jamup.model.VenueManager;
import org.jamup.model.enums.Instrument;
import org.jamup.model.enums.MusicGenre;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDAODB implements UserDAO {

    //map to prevent infinite recursion during manager and venue construction.
    //when building a VenueManager, VenueDAO is invoked to build its Venues.
    //VenueDAO in turn asks UserDAO for the manager, triggering a loop.
    //this map holds the partial manager being built to break the cycle.
    private final Map<String, VenueManager> managersInProgress = new HashMap<>();

    /**
     * Maps a row from a ResultSet to an Artist object.
     *
     * @param rs the ResultSet containing artist data
     * @return an Artist instance populated with data from the current row
     * @throws SQLException if a database access error occurs or column labels are missing
     */
    private Artist resultSetToArtist(ResultSet rs) throws SQLException {
        String id       = rs.getString("id");
        String email    = rs.getString("email");
        String password = rs.getString("password");
        String name     = rs.getString("name");

        List<Instrument> instruments = new ArrayList<>();
        String instrStr = rs.getString("instruments");
        if (instrStr != null && !instrStr.isEmpty()) {
            for (String i : instrStr.split("\\|")) {
                instruments.add(Instrument.valueOf(i));
            }
        }

        List<MusicGenre> genres = new ArrayList<>();
        String genreStr = rs.getString("genres");
        if (genreStr != null && !genreStr.isEmpty()) {
            for (String g : genreStr.split("\\|")) {
                genres.add(MusicGenre.valueOf(g));
            }
        }

        return new Artist(id, email, password, name, instruments, genres);
    }

    /**
     * Maps a row from a ResultSet to a VenueManager object.
     *
     * @param rs the ResultSet containing venue manager data
     * @return a VenueManager instance populated with data from the current row
     * @throws SQLException if a database access error occurs or column labels are missing
     */
    private VenueManager resultSetToManager(ResultSet rs) throws SQLException {
        String id       = rs.getString("id");
        String email    = rs.getString("email");
        String password = rs.getString("password");

        //check if we are already building this manager
        if (managersInProgress.containsKey(id)) {
            return managersInProgress.get(id);
        }

        VenueManager manager = new VenueManager(id, email, password, new ArrayList<>());
        managersInProgress.put(id, manager);

        List<Venue> venues = new ArrayList<>();
        String venueStr = rs.getString("venue_ids");
        if (venueStr != null && !venueStr.isEmpty()) {
            String[] venueIds = venueStr.split("\\|");
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
        try {
            Connection conn = DBConnectionFactory.getConnection();
            try (CallableStatement stmt = conn.prepareCall("{CALL FindArtistByEmail(?)}")) {
                stmt.setString(1, email);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return resultSetToArtist(rs);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DAOException("DB error in findArtistByEmail", e);
        }
        return null;
    }

    @Override
    public Artist findArtistById(String id) {
        try {
            Connection conn = DBConnectionFactory.getConnection();
            try (CallableStatement stmt = conn.prepareCall("{CALL FindArtistById(?)}")) {
                stmt.setString(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return resultSetToArtist(rs);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DAOException("DB error in findArtistById", e);
        }
        return null;
    }

    @Override
    public VenueManager findManagerByEmail(String email) {
        try {
            Connection conn = DBConnectionFactory.getConnection();
            try (CallableStatement stmt = conn.prepareCall("{CALL FindManagerByEmail(?)}")) {
                stmt.setString(1, email);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return resultSetToManager(rs);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DAOException("DB error in findManagerByEmail", e);
        }
        return null;
    }

    @Override
    public VenueManager findManagerById(String id) {
        try {
            Connection conn = DBConnectionFactory.getConnection();
            try (CallableStatement stmt = conn.prepareCall("{CALL FindManagerById(?)}")) {
                stmt.setString(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return resultSetToManager(rs);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DAOException("DB error in findManagerById", e);
        }
        return null;
    }

}