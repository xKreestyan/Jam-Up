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

    //login
    public void login(LoginUserBean bean) throws InvalidCredentialsException {
        LoginController loginController = new LoginController();
        this.sessionId = loginController.login(bean);
    }

    public void logout() {
        SessionManager.getInstance().logout(sessionId);
        this.sessionId = null;
    }

    public boolean isArtistLoggedIn() {
        return SessionManager.getInstance().isArtistLoggedIn(sessionId);
    }

    public boolean isManagerLoggedIn() {
        return SessionManager.getInstance().isManagerLoggedIn(sessionId);
    }

    //reserve venue
    public List<VenueBean> search(VenueBean bean) throws NoVenuesFoundException {
        ReserveVenueController reserveVenueController = new ReserveVenueController();
        return reserveVenueController.search(bean);
    }

    public void confirmReservation(ReservationBean bean) {
        ReserveVenueController reserveVenueController = new ReserveVenueController();
        reserveVenueController.confirmReservation(bean, this.sessionId);
    }

    //manage reservations
    public List<ReservationBean> fetchReservations(ReservationStatus status) throws NoReservationsFoundException {
        ManageReservationsController manageReservationsController = new ManageReservationsController();
        return manageReservationsController.fetchReservations(status, this.sessionId);
    }
    public void accept(String reservationId) {
        ManageReservationsController manageReservationsController = new ManageReservationsController();
        manageReservationsController.accept(reservationId);
    }
    public void reject(String reservationId) {
        ManageReservationsController manageReservationsController = new ManageReservationsController();
        manageReservationsController.reject(reservationId);
    }

    //notifications
    public List<NotificationBean> fetchNotifications() {
        NotificationController notificationController = new NotificationController();
        return notificationController.fetchNotifications(this.sessionId);
    }
    public void markAsRead(NotificationBean bean) {
        NotificationController notificationController = new NotificationController();
        notificationController.markAsRead(bean);
    }
    public int countUnreadNotifications() {
        NotificationController notificationController = new NotificationController();
        return notificationController.countUnreadNotifications(this.sessionId);
    }

    public void markAllNotificationsAsRead() {
        NotificationController notificationController = new NotificationController();
        notificationController.markAllNotificationsAsRead(this.sessionId);
    }

}