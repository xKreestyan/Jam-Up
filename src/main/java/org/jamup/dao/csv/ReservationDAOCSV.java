package org.jamup.dao.csv;

import org.jamup.dao.interfaces.ReservationDAO;
import org.jamup.dao.interfaces.UserDAO;
import org.jamup.dao.interfaces.VenueDAO;
import org.jamup.factory.DAOFactory;
import org.jamup.model.Artist;
import org.jamup.model.Reservation;
import org.jamup.model.TimeSlot;
import org.jamup.model.Venue;
import org.jamup.model.enums.ReservationStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReservationDAOCSV implements ReservationDAO {

    private static final String RESERVATIONS_FILE = "reservations.csv";
    private static final String[] HEADER = {"id", "notes", "status", "artistId", "venueId", "slotDate", "slotTime"};

    private final UserDAO userDAO = DAOFactory.getInstance().createUserDAO();
    private final VenueDAO venueDAO = DAOFactory.getInstance().createVenueDAO();

    /**
     * Converts a CSV row array into a Reservation object.
     *
     * @param row An array of strings representing the columns in a CSV row.
     * @return A {@link Reservation} object populated with the data from the row.
     */
    private Reservation rowToReservation(String[] row) {
        String id       = row[0];
        String notes    = row[1];
        ReservationStatus status = ReservationStatus.valueOf(row[2]);
        String artistId = row[3];
        String venueId  = row[4];
        LocalDate date  = LocalDate.parse(row[5]);
        LocalTime time  = LocalTime.parse(row[6]);

        Artist artist = userDAO.findArtistById(artistId);
        Venue venue   = venueDAO.findById(venueId);
        TimeSlot slot = new TimeSlot(date, time);

        Reservation reservation = new Reservation(notes, artist, venue, slot);
        reservation.setId(id);
        reservation.setStatus(status);
        return reservation;
    }

    /**
     * Converts a Reservation object into a CSV row array.
     *
     * @param reservation The {@link Reservation} object to be converted.
     * @return An array of strings representing the columns for the CSV row.
     */
    private String[] reservationToRow(Reservation reservation) {
        return new String[]{
                reservation.getId(),
                reservation.getNotes(),
                reservation.getStatus().name(),
                reservation.getArtist().getId(),
                reservation.getVenue().getId(),
                reservation.getReservedSlot().getDate().toString(),
                reservation.getReservedSlot().getTime().toString()
        };
    }

    @Override
    public void save(Reservation newReservation) {
        String id = UUID.randomUUID().toString();
        newReservation.setId(id);
        CSVStorage.append(RESERVATIONS_FILE, reservationToRow(newReservation));
    }

    @Override
    public Reservation findById(String id) {
        for (String[] row : CSVStorage.read(RESERVATIONS_FILE)) {
            if (row[0].equals(id)) {
                return rowToReservation(row);
            }
        }
        return null;
    }

    @Override
    public List<Reservation> findByVenues(List<String> venueIds, ReservationStatus status) {
        List<Reservation> results = new ArrayList<>();
        for (String[] row : CSVStorage.read(RESERVATIONS_FILE)) {
            if (venueIds.contains(row[4])) {
                //if status is not specified, it takes all reservations
                //otherwise it only takes those with the specific status
                if (status == null || ReservationStatus.valueOf(row[2]).equals(status)) {
                    results.add(rowToReservation(row));
                }
            }
        }
        return results;
    }

    //update of the i-th reservation following a status change
    @Override
    public void update(Reservation reservation) {
        List<String[]> rows = CSVStorage.read(RESERVATIONS_FILE);
        for (int i = 0; i < rows.size(); i++) {
            if (rows.get(i)[0].equals(reservation.getId())) {
                rows.set(i, reservationToRow(reservation));
                break;
            }
        }
        CSVStorage.rewrite(RESERVATIONS_FILE, HEADER, rows);
    }

    public ReservationDAOCSV() {
        System.out.println("Creato ReservationDAO versione CSV");
    }

}