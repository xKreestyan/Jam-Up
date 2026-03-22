package org.jamup.dao.csv;

import org.jamup.dao.VenueFilter;
import org.jamup.dao.interfaces.VenueDAO;
import org.jamup.model.Venue;
import org.jamup.model.enums.MusicGenre;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class VenueDAOCSV implements VenueDAO {

    private static final String VENUES_FILE = "venues.csv";
    private static final String[] HEADER = {"id", "name", "description", "genres", "location", "managerId", "slots"};

    // venues.csv: id, name, description, genres, location, managerId, slots
    /**
     * Converts a CSV row represented as a String array into a Venue object.
     *
     * @param row the array of strings representing a row from the CSV file
     * @return the Venue object populated with data from the row
     */
    private Venue rowToVenue(String[] row) {
        String id          = row[0];
        String name        = row[1];
        String description = row[2];
        String location    = row[4];
        String managerId   = row[5];

        List<MusicGenre> genres = new ArrayList<>();
        for (String g : row[3].split("\\|")) {
            genres.add(MusicGenre.valueOf(g));
        }

        Venue venue = new Venue(id, name, description, genres, location, managerId);

        for (String slot : row[6].split("\\|")) {
            String[] parts = slot.split("T");
            LocalDate date = LocalDate.parse(parts[0]);
            LocalTime time = LocalTime.parse(parts[1]);
            venue.getCalendar().addSlot(date, time);
        }

        return venue;
    }

    /**
     * Converts a Venue object into a CSV row represented as a String array.
     *
     * @param venue the Venue object to be converted
     * @return an array of strings representing the Venue data for CSV storage
     */
    private String[] venueToRow(Venue venue) {
        String genres = venue.getGenres().stream()
                .map(Enum::name)
                .reduce((a, b) -> a + "|" + b)
                .orElse("");

        String slots = venue.getCalendar().getSlots().stream()
                .map(s -> s.getDate().toString() + "T" + s.getTime().toString())
                .reduce((a, b) -> a + "|" + b)
                .orElse("");

        return new String[]{
                venue.getId(), venue.getName(), venue.getDescription(),
                genres, venue.getLocation(), venue.getManagerId(), slots
        };
    }

    @Override
    public List<Venue> findByCriteria(String searchName, List<MusicGenre> searchGenres, LocalDate searchDate) {
        List<Venue> searchResults = new ArrayList<>();

        for (String[] row : CSVStorage.read(VENUES_FILE)) {
            Venue venue = rowToVenue(row);

            if (!VenueFilter.matches(venue, searchName, searchGenres, searchDate)){
                continue;
            }

            searchResults.add(venue);
        }

        return searchResults;
    }

    @Override
    public Venue findById(String id) {
        for (String[] row : CSVStorage.read(VENUES_FILE)) {
            if (row[0].equals(id)) {
                return rowToVenue(row);
            }
        }
        return null;
    }

    //update of the i-th venue following a change to its availability calendar
    @Override
    public void update(Venue updatedVenue) {
        List<String[]> rows = CSVStorage.read(VENUES_FILE);
        for (int i = 0; i < rows.size(); i++) {
            if (rows.get(i)[0].equals(updatedVenue.getId())) {
                rows.set(i, venueToRow(updatedVenue));
                break;
            }
        }
        CSVStorage.rewrite(VENUES_FILE, HEADER, rows);
    }

    public VenueDAOCSV() {
        System.out.println("Creato VenueDAO versione CSV");
    }

}