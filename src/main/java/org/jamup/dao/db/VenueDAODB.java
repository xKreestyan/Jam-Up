package org.jamup.dao.db;

import org.jamup.dao.VenueFilter;
import org.jamup.dao.factory.DBConnectionFactory;
import org.jamup.dao.interfaces.VenueDAO;
import org.jamup.exception.DAOException;
import org.jamup.model.Venue;
import org.jamup.model.enums.MusicGenre;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class VenueDAODB implements VenueDAO {

    /**
     * Converts a row from the ResultSet into a Venue object.
     *
     * @param rs the ResultSet containing venue data, including concatenated genres and slots.
     * @return a Venue object populated with the data from the current row.
     * @throws SQLException if a database access error occurs or this method is called on a closed result set.
     */
    private Venue resultSetToVenue(ResultSet rs) throws SQLException {
        String id          = rs.getString("id");
        String name        = rs.getString("name");
        String description = rs.getString("description");
        String location    = rs.getString("location");
        String managerId   = rs.getString("manager_id");

        List<MusicGenre> genres = new ArrayList<>();
        String genreStr = rs.getString("genres");
        if (genreStr != null) {
            for (String g : genreStr.split("\\|")) {
                genres.add(MusicGenre.valueOf(g));
            }
        }

        Venue venue = new Venue(id, name, description, genres, location, managerId);

        String slotsStr = rs.getString("slots");
        if (slotsStr != null) {
            for (String slot : slotsStr.split("\\|")) {
                String[] parts = slot.split("T");
                LocalDate date = LocalDate.parse(parts[0]);
                LocalTime time = LocalTime.parse(parts[1]);
                venue.getCalendar().addSlot(date, time);
            }
        }

        return venue;
    }

    /**
     * Compares the slots of two venue instances and executes a stored procedure for each slot
     * present in the second venue but missing from the first.
     *
     * @param updatedVenue the venue containing the target set of slots.
     * @param currentVenue the venue containing the source set of slots to iterate over.
     * @param stmt         the CallableStatement (Insert or Delete) to execute.
     * @param id           the unique identifier of the venue.
     * @throws SQLException if a database access error occurs.
     */
    private void updateSlots(Venue updatedVenue, Venue currentVenue, CallableStatement stmt, String id) throws SQLException {
        //set the ID once, as it is loop-invariant
        stmt.setString(1, id);

        //iterate through each slot in the source venue
        for (var slot : currentVenue.getCalendar().getSlots()) {
            boolean stillPresent = false;

            //check if the current slot exists in the target venue's calendar
            for (var updatedSlot : updatedVenue.getCalendar().getSlots()) {
                if (updatedSlot.getDate().equals(slot.getDate()) && updatedSlot.getTime().equals(slot.getTime())) {
                    stillPresent = true;
                    break;
                }
            }

            //if the slot is missing from the target, execute the provided database statement
            if (!stillPresent) {
                stmt.setDate(2, java.sql.Date.valueOf(slot.getDate()));
                stmt.setTime(3, java.sql.Time.valueOf(slot.getTime()));
                stmt.executeUpdate();
            }
        }
    }

    @Override
    public List<Venue> findByCriteria(String searchName, List<MusicGenre> searchGenres, LocalDate searchDate) {
        List<Venue> results = new ArrayList<>();
        try {
            Connection conn = DBConnectionFactory.getConnection();
            try (CallableStatement stmt = conn.prepareCall("{CALL FindAllVenues()}")) {
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Venue venue = resultSetToVenue(rs);
                        if (VenueFilter.matches(venue, searchName, searchGenres, searchDate)) {
                            results.add(venue);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new DAOException("DB error in findByCriteria", e);
        }
        return results;
    }

    @Override
    public Venue findById(String id) {
        try {
            Connection conn = DBConnectionFactory.getConnection();
            try (CallableStatement stmt = conn.prepareCall("{CALL FindVenueById(?)}")) {
                stmt.setString(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return resultSetToVenue(rs);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DAOException("DB error in findVenueById", e);
        }
        return null;
    }

    @Override
    public void update(Venue updatedVenue) {
        try {
            Connection conn = DBConnectionFactory.getConnection();

            //retrieve current venue from the DB
            Venue currentVenue = findById(updatedVenue.getId());

            //find slots to delete (present in DB but not in the updated venue)
            try (CallableStatement deleteStmt = conn.prepareCall("{CALL DeleteTimeSlot(?, ?, ?)}")) {
                updateSlots(updatedVenue, currentVenue, deleteStmt, updatedVenue.getId());
            }

            //find slots to insert (present in the updated venue but not in the DB)
            try (CallableStatement insertStmt = conn.prepareCall("{CALL InsertTimeSlot(?, ?, ?)}")) {
                updateSlots(currentVenue, updatedVenue, insertStmt, updatedVenue.getId());
            }

        } catch (SQLException e) {
            throw new DAOException("DB error in updateVenue", e);
        }
    }

}