package org.jamup.dao.db;

import org.jamup.dao.factory.DBConnectionFactory;
import org.jamup.dao.factory.DAOFactory;
import org.jamup.dao.interfaces.ReservationDAO;
import org.jamup.model.Artist;
import org.jamup.model.Reservation;
import org.jamup.model.TimeSlot;
import org.jamup.model.Venue;
import org.jamup.model.enums.ReservationStatus;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReservationDAODB implements ReservationDAO {

    /**
     * Converts a row from a ResultSet into a Reservation object.
     *
     * @param rs the ResultSet containing reservation data
     * @return a fully populated Reservation object
     * @throws SQLException if a database access error occurs
     */
    private Reservation resultSetToReservation(ResultSet rs) throws SQLException {
        String id       = rs.getString("id");
        String notes    = rs.getString("notes");
        ReservationStatus status = ReservationStatus.valueOf(rs.getString("status"));
        String artistId = rs.getString("artist_id");
        String venueId  = rs.getString("venue_id");
        LocalDate date  = rs.getDate("slot_date").toLocalDate();
        LocalTime time  = rs.getTime("slot_time").toLocalTime();

        Artist artist = DAOFactory.getInstance().createUserDAO().findArtistById(artistId);
        Venue venue   = DAOFactory.getInstance().createVenueDAO().findById(venueId);
        TimeSlot slot = new TimeSlot(date, time);

        Reservation reservation = new Reservation(notes, artist, venue, slot);
        reservation.setId(id);
        reservation.setStatus(status);
        return reservation;
    }

    @Override
    public void save(Reservation reservation) {
        try {
            reservation.setId(UUID.randomUUID().toString());
            Connection conn = DBConnectionFactory.getConnection();
            CallableStatement stmt = conn.prepareCall("{CALL SaveReservation(?, ?, ?, ?, ?, ?)}");
            stmt.setString(1, reservation.getId());
            stmt.setString(2, reservation.getNotes());
            stmt.setString(3, reservation.getArtist().getId());
            stmt.setString(4, reservation.getVenue().getId());
            stmt.setDate(5, java.sql.Date.valueOf(reservation.getReservedSlot().getDate()));
            stmt.setTime(6, java.sql.Time.valueOf(reservation.getReservedSlot().getTime()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("DB error in saveReservation", e);
        }
    }

    @Override
    public Reservation findById(String id) {
        try {
            Connection conn = DBConnectionFactory.getConnection();
            CallableStatement stmt = conn.prepareCall("{CALL FindReservationById(?)}");
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return resultSetToReservation(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("DB error in findReservationById", e);
        }
        return null;
    }

    @Override
    public List<Reservation> findByVenues(List<String> venueIds, ReservationStatus status) {
        List<Reservation> results = new ArrayList<>();
        try {
            Connection conn = DBConnectionFactory.getConnection();
            CallableStatement stmt = conn.prepareCall("{CALL FindReservationsByVenueId(?, ?)}");
            for (String venueId : venueIds) {
                stmt.setString(1, venueId);
                stmt.setString(2, status != null ? status.name() : null);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    results.add(resultSetToReservation(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("DB error in findByVenues", e);
        }
        return results;
    }

    @Override
    public void update(Reservation reservation) {
        try {
            Connection conn = DBConnectionFactory.getConnection();
            CallableStatement stmt = conn.prepareCall("{CALL UpdateReservationStatus(?, ?)}");
            stmt.setString(1, reservation.getId());
            stmt.setString(2, reservation.getStatus().name());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("DB error in updateReservation", e);
        }
    }

}