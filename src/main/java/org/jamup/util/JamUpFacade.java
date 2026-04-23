package org.jamup.util;

import org.jamup.bean.*;
import org.jamup.controller.*;
import org.jamup.exception.InvalidCredentialsException;
import org.jamup.exception.NoReservationsFoundException;
import org.jamup.exception.NoVenuesFoundException;
import org.jamup.model.enums.ReservationStatus;

import java.util.List;

@SuppressWarnings("java:S6548")
public class JamUpFacade {

    //thread-safe initialization
    private static class InstanceHolder {
        private static final JamUpFacade instance = new JamUpFacade();
    }

    private JamUpFacade() {}

    public static JamUpFacade getInstance() {
        return InstanceHolder.instance;
    }

    private String sessionId;

    /**
     * Authenticates a user and sets the session ID.
     * @param bean the login credentials
     * @throws InvalidCredentialsException if authentication fails
     */
    public void login(LoginUserBean bean) throws InvalidCredentialsException {
        LoginController loginController = new LoginController();
        this.sessionId = loginController.login(bean);
    }

    /**
     * Logs out the current user and clears the session.
     */
    public void logout() {
        SessionManager.getInstance().logout(sessionId);
        this.sessionId = null;
    }

    /**
     * Checks if the currently logged-in user is an Artist.
     * @return true if an artist is logged in, false otherwise
     */
    public boolean isArtistLoggedIn() {
        return SessionManager.getInstance().isArtistLoggedIn(sessionId);
    }

    /**
     * Checks if the currently logged-in user is a Manager.
     * @return true if a manager is logged in, false otherwise
     */
    public boolean isManagerLoggedIn() {
        return SessionManager.getInstance().isManagerLoggedIn(sessionId);
    }

    /**
     * Searches for venues based on the provided criteria.
     * @param bean the search criteria
     * @return a list of matching venues
     * @throws NoVenuesFoundException if no venues match the criteria
     */
    public List<VenueBean> search(VenueBean bean) throws NoVenuesFoundException {
        ReserveVenueController reserveVenueController = new ReserveVenueController();
        return reserveVenueController.search(bean);
    }

    /**
     * Confirms a reservation for the current user.
     * @param bean the reservation details
     */
    public void confirmReservation(ReservationBean bean) {
        ReserveVenueController reserveVenueController = new ReserveVenueController();
        reserveVenueController.confirmReservation(bean, this.sessionId);
    }

    /**
     * Fetches reservations filtered by status for the current user.
     * @param status the status of reservations to fetch
     * @return a list of reservations
     * @throws NoReservationsFoundException if no reservations are found
     */
    public List<ReservationBean> fetchReservations(ReservationStatus status) throws NoReservationsFoundException {
        ManageReservationsController manageReservationsController = new ManageReservationsController();
        return manageReservationsController.fetchReservations(status, this.sessionId);
    }

    /**
     * Accepts a specific reservation.
     * @param reservationId the ID of the reservation to accept
     */
    public void accept(String reservationId) {
        ManageReservationsController manageReservationsController = new ManageReservationsController();
        manageReservationsController.accept(reservationId);
    }

    /**
     * Rejects a specific reservation.
     * @param reservationId the ID of the reservation to reject
     */
    public void reject(String reservationId) {
        ManageReservationsController manageReservationsController = new ManageReservationsController();
        manageReservationsController.reject(reservationId);
    }

    /**
     * Fetches all notifications for the current user.
     * @return a list of notifications
     */
    public List<NotificationBean> fetchNotifications() {
        NotificationController notificationController = new NotificationController();
        return notificationController.fetchNotifications(this.sessionId);
    }

    /**
     * Marks a specific notification as read.
     * @param bean the notification to mark as read
     */
    public void markAsRead(NotificationBean bean) {
        NotificationController notificationController = new NotificationController();
        notificationController.markAsRead(bean);
    }

    /**
     * Counts the number of unread notifications for the current user.
     * @return the count of unread notifications
     */
    public int countUnreadNotifications() {
        NotificationController notificationController = new NotificationController();
        return notificationController.countUnreadNotifications(this.sessionId);
    }

    /**
     * Marks all notifications as read for the current user.
     */
    public void markAllNotificationsAsRead() {
        NotificationController notificationController = new NotificationController();
        notificationController.markAllNotificationsAsRead(this.sessionId);
    }

}