package org.jamup;

import org.jamup.bean.*;
import org.jamup.exception.InvalidCredentialsException;
import org.jamup.exception.InvalidFieldException;
import org.jamup.exception.NoReservationsFoundException;
import org.jamup.exception.NoVenuesFoundException;
import org.jamup.model.TimeSlot;
import org.jamup.model.enums.MusicGenre;
import org.jamup.model.enums.ReservationStatus;
import org.jamup.util.JamUpFacade;
import org.jamup.util.MapService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CLIApp {

    private final Scanner scanner;
    private final JamUpFacade facade = JamUpFacade.getInstance();
    private static final String INVALID_INPUT = "Invalid input.";

    public CLIApp(Scanner scanner) {
        this.scanner = scanner;
    }

    public void start() {
        System.out.println("=== Jam Up! CLI ===");
        boolean running = true;
        while (running) {
            running = showLoginMenu();
        }
    }

    // ============================================================
    // LOGIN
    // ============================================================

    private boolean showLoginMenu() {
        System.out.println("1. Login");
        System.out.println("2. Exit");

        if (readChoice(2).equals("2")) {
            return false;
        }
        
        while (true) {
            System.out.println("\n--- Login ---");
            System.out.print("Email: ");
            String email = scanner.nextLine().trim();
            System.out.print("Password: ");
            String password = scanner.nextLine().trim();

            try {
                LoginUserBean bean = new LoginUserBean(email, password);
                facade.login(bean);

                if (facade.isArtistLoggedIn()) {
                    showArtistMenu();
                } else {
                    showManagerMenu();
                }
                return true;

            } catch (InvalidFieldException | InvalidCredentialsException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    // ============================================================
    // ARTIST MENU
    // ============================================================

    private void showArtistMenu() {
        while (true) {
            System.out.println("\n--- Artist Menu ---");
            System.out.println("1. Search venues");
            System.out.println("2. Notifications");
            System.out.println("3. Logout");

            switch (readChoice(3)) {
                case "1":
                    searchVenues();
                    break;
                case "2":
                    showNotifications();
                    break;
                case "3":
                    facade.logout();
                    return;
                default:
                    throw new IllegalStateException("Unexpected choice");
            }
        }
    }

    private void searchVenues() {
        System.out.println("\n--- Search Venues ---");
        System.out.print("Name (leave empty to skip): ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) name = null;

        System.out.print("Date (yyyy-MM-dd, leave empty to skip): ");
        String dateStr = scanner.nextLine().trim();
        LocalDate date = null;
        if (!dateStr.isEmpty()) {
            try { date = LocalDate.parse(dateStr); }
            catch (DateTimeParseException e) { System.out.println("Invalid date format, skipping."); }
        }

        List<MusicGenre> genres = readGenres();

        try {
            VenueBean searchBean = new VenueBean(name, date, genres);
            List<VenueBean> results = facade.search(searchBean);
            
            //dummy map service call
            MapService.renderMap();
            
            System.out.println("--- Results ---");
            for (int j = 0; j < results.size(); j++) {
                VenueBean v = results.get(j);
                System.out.println((j + 1) + ". " + v.getName() + " - " + v.getLocation());
            }
            
            System.out.print("Select venue (0 to go back): ");
            String choice = readChoice(results.size(), true);
            int idx = Integer.parseInt(choice);
            if (idx > 0) {
                showVenueDetail(results.get(idx - 1));
            }
        } catch (NoVenuesFoundException | InvalidFieldException e) {
            System.out.println(e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println(INVALID_INPUT);
        }
    }

    private List<MusicGenre> readGenres() {
        System.out.println("Select genres (comma separated, leave empty to skip):");
        int i = 1;
        for (MusicGenre g : MusicGenre.values()) {
            System.out.print(i++ + ". " + g.name() + "  ");
        }
        System.out.println("\n> ");
        String genreInput = scanner.nextLine().trim();
        if (genreInput.isEmpty()) return new ArrayList<>();

        List<MusicGenre> genres = new ArrayList<>();
        for (String idx : genreInput.split(",")) {
            try {
                int index = Integer.parseInt(idx.trim()) - 1;
                if (index >= 0 && index < MusicGenre.values().length) {
                    genres.add(MusicGenre.values()[index]);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid genre index, skipping.");
            }
        }
        return genres;
    }

    private void showVenueDetail(VenueBean venue) {
        System.out.println("\n--- " + venue.getName() + " ---");
        System.out.println("Location: " + venue.getLocation());
        System.out.println("Genres: " + venue.getGenres());
        System.out.println("Description: " + venue.getDescription());

        List<TimeSlot> slots = venue.getAvailableSlots();
        if (slots.isEmpty()) {
            System.out.println("No available slots.");
            return;
        }

        System.out.println("\nAvailable slots:");
        for (int i = 0; i < slots.size(); i++) {
            TimeSlot s = slots.get(i);
            System.out.println((i + 1) + ". " + s.getDate() + " at " + s.getTime());
        }

        System.out.print("Select slot (0 to go back): ");
        String choice = readChoice(slots.size(), true);
        try {
            int idx = Integer.parseInt(choice);
            if (idx > 0) {
                TimeSlot selected = slots.get(idx - 1);
                confirmReservation(venue, selected);
            }
        } catch (NumberFormatException e) {
            System.out.println(INVALID_INPUT);
        }
    }

    private void confirmReservation(VenueBean venue, TimeSlot selected) {
        System.out.print("Notes (leave empty for none): ");
        String notes = scanner.nextLine().trim();
        
        System.out.println("\n--- Reservation Summary ---");
        System.out.println("Venue: " + venue.getName());
        System.out.println("Date: " + selected.getDate() + " at " + selected.getTime());
        System.out.println("Notes: " + (notes.isEmpty() ? "None" : notes));
        System.out.println("1. Confirm");
        System.out.println("2. Cancel");
        
        String choice = readChoice(2);
        if (choice.equals("2")) {
            System.out.println("Reservation cancelled.");
            return;
        }

        try {
            ReservationBean bean = new ReservationBean(venue.getId(), selected, notes);
            facade.confirmReservation(bean);
            System.out.println("Reservation request sent successfully.");
        } catch (InvalidFieldException e) {
            System.out.println(e.getMessage());
        }
    }

    // ============================================================
    // MANAGER MENU
    // ============================================================

    private void showManagerMenu() {
        while (true) {
            System.out.println("\n--- Manager Menu ---");
            System.out.println("1. Pending reservations");
            System.out.println("2. Accepted reservations");
            System.out.println("3. Rejected reservations");
            System.out.println("4. Notifications");
            System.out.println("5. Logout");

            switch (readChoice(5)) {
                case "1":
                    showReservations(ReservationStatus.PENDING);
                    break;
                case "2":
                    showReservations(ReservationStatus.ACCEPTED);
                    break;
                case "3":
                    showReservations(ReservationStatus.REJECTED);
                    break;
                case "4":
                    showNotifications();
                    break;
                case "5":
                    facade.logout();
                    return;
                default:
                    throw new IllegalStateException("Unexpected choice");
            }
        }
    }

    private void showReservations(ReservationStatus status) {
        try {
            List<ReservationBean> reservations = facade.fetchReservations(status);
            System.out.println("\n--- " + status.name() + " Reservations ---");
            for (int i = 0; i < reservations.size(); i++) {
                ReservationBean r = reservations.get(i);
                System.out.println((i + 1) + ". " + r.getArtistName() + " → " + r.getVenueName()
                        + " | " + r.getReservedSlot().getDate() + " at " + r.getReservedSlot().getTime());
            }

            if (status == ReservationStatus.PENDING) {
                selectAndManageReservation(reservations);
            }
        } catch (NoReservationsFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void selectAndManageReservation(List<ReservationBean> reservations) {
        System.out.print("Select reservation to manage (0 to go back): ");
        String choice = readChoice(reservations.size(), true);
        try {
            int idx = Integer.parseInt(choice);
            if (idx > 0) {
                manageReservation(reservations.get(idx - 1));
            }
        } catch (NumberFormatException e) {
            System.out.println(INVALID_INPUT);
        }
    }

    private void manageReservation(ReservationBean reservation) {
        System.out.println("\n--- Manage Reservation ---");
        System.out.println("Artist: " + reservation.getArtistName());
        System.out.println("Venue: " + reservation.getVenueName());
        System.out.println("Date: " + reservation.getReservedSlot().getDate() + " at " + reservation.getReservedSlot().getTime());
        System.out.println("Notes: " + reservation.getNotes());
        System.out.println("1. Accept");
        System.out.println("2. Reject");
        System.out.println("3. Go back");

        switch (readChoice(3)) {
            case "1":
                facade.accept(reservation.getReservationId());
                System.out.println("Reservation accepted.");
                break;
            case "2":
                facade.reject(reservation.getReservationId());
                System.out.println("Reservation rejected.");
                break;
            case "3":
                return;
            default:
                throw new IllegalStateException("Unexpected choice");
        }
    }

    // ============================================================
    // NOTIFICATIONS
    // ============================================================

    private void showNotifications() {
        List<NotificationBean> notifications = facade.fetchNotifications();
        System.out.println("\n--- Notifications ---");
        if (notifications.isEmpty()) {
            System.out.println("No notifications.");
            return;
        }
        for (int i = 0; i < notifications.size(); i++) {
            NotificationBean n = notifications.get(i);
            String readStatus = n.isRead() ? "[read]" : "[unread]";
            System.out.println((i + 1) + ". " + readStatus + " " + n.message());
        }
        System.out.print("Select notification to mark as read (0 to go back): ");
        String choice = readChoice(notifications.size(), true);
        try {
            int idx = Integer.parseInt(choice);
            if (idx > 0) {
                facade.markAsRead(notifications.get(idx - 1));
                System.out.println("Notification marked as read.");
            }
        } catch (NumberFormatException e) {
            System.out.println(INVALID_INPUT);
        }
    }

    // ============================================================
    // UTILITY
    // ============================================================

    private String readChoice(int max) {
        return readChoice(max, false);
    }
    
    private String readChoice(int max, boolean allowZero) {
        String input;
        String regex = allowZero ? "[0-" + max + "]" : "[1-" + max + "]";
        
        // Se max > 9, la regex semplice [0-max] non funziona correttamente (es. [0-12] matcherebbe solo 0, 1 o 2).
        // Quindi se max è > 9 e usiamo readChoice, è meglio fare un controllo numerico
        if (max > 9) {
            do {
                System.out.print("> ");
                input = scanner.nextLine().trim();
                try {
                    int val = Integer.parseInt(input);
                    if ((allowZero && val >= 0 && val <= max) || (!allowZero && val >= 1 && val <= max)) {
                        return input;
                    }
                } catch (NumberFormatException e) {
                    // fallthrough
                }
            } while (true);
        }

        do {
            System.out.print("> ");
            input = scanner.nextLine().trim();
        } while (!input.matches(regex));
        return input;
    }
}