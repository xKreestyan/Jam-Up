package org.jamup.dao;

import org.jamup.model.Venue;
import org.jamup.model.enums.MusicGenre;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

public class VenueFilter {

    private VenueFilter() {}

    /**
     * Checks if a venue matches the specified search criteria.
     *
     * @param venue the venue to check
     * @param searchName the name or partial name to search for (case-insensitive)
     * @param searchGenres the list of genres the venue must support
     * @param searchDate the date on which the venue must have available times
     * @return true if the venue matches all non-null criteria, false otherwise
     */
    public static boolean matches(Venue venue, String searchName, List<MusicGenre> searchGenres, LocalDate searchDate) {

        //if the entered name is NOT null and does NOT match the venue name, exclude venue
        if (searchName != null && !venue.getName().toLowerCase().contains(searchName.toLowerCase())) {
            return false;
        }
        //if music genres have been specified and the venue does NOT support all of them, exclude venue
        if (searchGenres != null && !searchGenres.isEmpty()) {
            if (!new HashSet<>(venue.getGenres()).containsAll(searchGenres)) {
                return false;
            }
        }
        //if a date has been specified and the venue is NOT available on that date, exclude venue
        if (searchDate != null && venue.getCalendar().availableTimesForDate(searchDate).isEmpty()) {
            return false;
        }

        return true;
    }

}