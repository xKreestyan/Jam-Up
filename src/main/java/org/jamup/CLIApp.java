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
import org.jamup.util.SessionManager;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class CLIApp {

    private static final Logger logger = Logger.getLogger(CLIApp.class.getName());
    private final Scanner scanner;
    private final JamUpFacade facade = JamUpFacade.getInstance();

    public CLIApp(Scanner scanner) {
        this.scanner = scanner;
    }

    public void start() {
        logger.info("=== Jam Up! CLI ===");
        boolean running = true;
        while (running) {
            running = showLoginMenu();
        }
    }

    // ============================================================
    // LOGIN
    // ============================================================

    private boolean showLoginMenu() {
        logger.info("1. Login");
        logger.info("2. Exit");

        if (readChoice(2).equals("2")) {
            return false;
        }
        while (true) {
            logger.info("\n--- Login ---");
            System.out.print("Email: ");
            String email = scanner.nextLine().trim();
            System.out.print("Password: ");
            String password = scanner.nextLine().trim();

            try {
                LoginUserBean bean = new LoginUserBean(email, password);
                facade.login(bean);

                if (SessionManager.getInstance().isArtistLoggedIn()) {
                    showArtistMenu();
                } else {
                    showManagerMenu();
                }
                return true;

            } catch (InvalidFieldException e) {
                logger.warning("Invalid input: " + e.getMessage());
            } catch (InvalidCredentialsException e) {
                logger.warning("Invalid email or password.");
            }
        }
    }

    // ============================================================
    // ARTIST MENU
    // ============================================================

    private void showArtistMenu() {
        while (true) {
            logger.info("\n--- Artist Menu ---");
            logger.info("1. Search venues");
            logger.info("2. Notifications");
            logger.info("3. Logout");

            switch (readChoice(3)) {
                case "1":
                    searchVenues();
                    break;
                case "2":
                    showNotifications();
                    break;
                case "3":
                    SessionManager.getInstance().logout();
                    return;
                default:
                    throw new IllegalStateException("Unexpected choice");
            }
        }
    }

    private void searchVenues() {
        logger.info("\n--- Search Venues ---");
        System.out.print("Name (leave empty to skip): ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) name = null;

        System.out.print("Date (yyyy-MM-dd, leave empty to skip): ");
        String dateStr = scanner.nextLine().trim();
        LocalDate date = null;
        if (!dateStr.isEmpty()) {
            try { date = LocalDate.parse(dateStr); }
            catch (DateTimeParseException e) { logger.warning("Invalid date format, skipping."); }
        }

        logger.info("Select genres (comma separated, leave empty to skip):");
        int i = 1;
        for (MusicGenre g : MusicGenre.values()) {
            System.out.print(i++ + ". " + g.name() + "  ");
        }
        System.out.println();
        System.out.print("> ");
        String genreInput = scanner.nextLine().trim();
        List<MusicGenre> genres = null;
        if (!genreInput.isEmpty()) {
            genres = new ArrayList<>();
            for (String idx : genreInput.split(",")) {
                try {
                    int index = Integer.parseInt(idx.trim()) - 1;
                    genres.add(MusicGenre.values()[index]);
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    logger.warning("Invalid genre index, skipping.");
                }
            }
            if (genres.isEmpty()) genres = null;
        }

        try {
            VenueBean searchBean = new VenueBean(name, date, genres);
            List<VenueBean> results = facade.search(searchBean);
            logger.info("\n--- Results ---");
            for (int j = 0; j < results.size(); j++) {
                VenueBean v = results.get(j);
                logger.info((j + 1) + ". " + v.getName() + " - " + v.getLocation());
            }
            System.out.print("Select venue (0 to go back): ");
            String choice = scanner.nextLine().trim();
            int idx = Integer.parseInt(choice);
            if (idx > 0 && idx <= results.size()) {
                showVenueDetail(results.get(idx - 1));
            }
        } catch (NoVenuesFoundException e) {
            logger.warning("No venues found.");
        } catch (NumberFormatException e) {
            logger.warning("Invalid input.");
        } catch (InvalidFieldException e) {
            logger.warning("Invalid venue name: " + e.getMessage());
        }
    }

    private void showVenueDetail(VenueBean venue) {
        logger.info("\n--- " + venue.getName() + " ---");
        logger.info("Location: " + venue.getLocation());
        logger.info("Genres: " + venue.getGenres());
        logger.info("Description: " + venue.getDescription());

        List<TimeSlot> slots = venue.getAvailableSlots();
        if (slots.isEmpty()) {
            logger.info("No available slots.");
            return;
        }

        logger.info("\nAvailable slots:");
        for (int i = 0; i < slots.size(); i++) {
            TimeSlot s = slots.get(i);
            logger.info((i + 1) + ". " + s.getDate() + " at " + s.getTime());
        }

        System.out.print("Select slot (0 to go back): ");
        String choice = scanner.nextLine().trim();
        try {
            int idx = Integer.parseInt(choice);
            if (idx > 0 && idx <= slots.size()) {
                TimeSlot selected = slots.get(idx - 1);
                System.out.print("Notes (leave empty for none): ");
                String notes = scanner.nextLine().trim();
                try {
                    ReservationBean bean = new ReservationBean(venue.getId(), selected, notes);
                    facade.confirmReservation(bean);
                    logger.info("Reservation request sent successfully.");
                } catch (InvalidFieldException e) {
                    logger.warning("Invalid notes: " + e.getMessage());
                }
            }
        } catch (NumberFormatException e) {
            logger.warning("Invalid input.");
        }
    }

    // ============================================================
    // MANAGER MENU
    // ============================================================

    private void showManagerMenu() {
        while (true) {
            logger.info("\n--- Manager Menu ---");
            logger.info("1. Pending reservations");
            logger.info("2. Accepted reservations");
            logger.info("3. Rejected reservations");
            logger.info("4. Notifications");
            logger.info("5. Logout");

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
                    SessionManager.getInstance().logout();
                    return;
                default:
                    throw new IllegalStateException("Unexpected choice");
            }
        }
    }

    private void showReservations(ReservationStatus status) {
        try {
            List<ReservationBean> reservations = facade.fetchReservations(status);
            logger.info("\n--- " + status.name() + " Reservations ---");
            for (int i = 0; i < reservations.size(); i++) {
                ReservationBean r = reservations.get(i);
                logger.info((i + 1) + ". " + r.getArtistName() + " → " + r.getVenueName()
                        + " | " + r.getReservedSlot().getDate() + " at " + r.getReservedSlot().getTime());
            }

            if (status == ReservationStatus.PENDING) {
                System.out.print("Select reservation to manage (0 to go back): ");
                String choice = scanner.nextLine().trim();
                try {
                    int idx = Integer.parseInt(choice);
                    if (idx > 0 && idx <= reservations.size()) {
                        manageReservation(reservations.get(idx - 1));
                    }
                } catch (NumberFormatException e) {
                    logger.warning("Invalid input.");
                }
            }
        } catch (NoReservationsFoundException e) {
            logger.info("No reservations found.");
        }
    }

    private void manageReservation(ReservationBean reservation) {
        logger.info("\n--- Manage Reservation ---");
        logger.info("Artist: " + reservation.getArtistName());
        logger.info("Venue: " + reservation.getVenueName());
        logger.info("Date: " + reservation.getReservedSlot().getDate() + " at " + reservation.getReservedSlot().getTime());
        logger.info("Notes: " + reservation.getNotes());
        logger.info("1. Accept");
        logger.info("2. Reject");
        logger.info("3. Go back");

        switch (readChoice(3)) {
            case "1":
                facade.accept(reservation.getReservationId());
                logger.info("Reservation accepted.");
                break;
            case "2":
                facade.reject(reservation.getReservationId());
                logger.info("Reservation rejected.");
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
        logger.info("\n--- Notifications ---");
        if (notifications.isEmpty()) {
            logger.info("No notifications.");
            return;
        }
        for (int i = 0; i < notifications.size(); i++) {
            NotificationBean n = notifications.get(i);
            String readStatus = n.isRead() ? "[read]" : "[unread]";
            logger.info((i + 1) + ". " + readStatus + " " + n.getMessage());
        }
        System.out.print("Select notification to mark as read (0 to go back): ");
        String choice = scanner.nextLine().trim();
        try {
            int idx = Integer.parseInt(choice);
            if (idx > 0 && idx <= notifications.size()) {
                facade.markAsRead(notifications.get(idx - 1));
                logger.info("Notification marked as read.");
            }
        } catch (NumberFormatException e) {
            logger.warning("Invalid input.");
        }
    }

    // ============================================================
    // UTILITY
    // ============================================================

    private String readChoice(int max) {
        String input;
        do {
            System.out.print("> ");
            input = scanner.nextLine().trim();
        } while (!input.matches("[1-" + max + "]"));
        return input;
    }
}