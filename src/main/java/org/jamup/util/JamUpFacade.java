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

    //singleton pattern
    private static JamUpFacade instance;

    private final LoginController loginController = new LoginController();
    private final ReserveVenueController reserveVenueController = new ReserveVenueController();
    private final ManageReservationsController manageReservationsController = new ManageReservationsController();
    private final NotificationController notificationController = new NotificationController();

    private JamUpFacade() {}

    public static JamUpFacade getInstance() {
        if (instance == null) {
            instance = new JamUpFacade();
        }
        return instance;
    }

    // Login
    public void login(LoginUserBean bean) throws InvalidCredentialsException {
        loginController.login(bean);
    }

    // ReserveVenue
    public List<VenueBean> search(VenueBean bean) throws NoVenuesFoundException {
        return reserveVenueController.search(bean);
    }

    public void confirmReservation(ReservationBean bean) {
        reserveVenueController.confirmReservation(bean);
    }

    // ManageReservations
    public List<ReservationBean> fetchReservations(ReservationStatus status) throws NoReservationsFoundException {
        return manageReservationsController.fetchReservations(status);
    }
    public void accept(String reservationId) {
        manageReservationsController.accept(reservationId);
    }
    public void reject(String reservationId) {
        manageReservationsController.reject(reservationId);
    }

    // Notifications
    public List<NotificationBean> fetchNotifications() {
        return notificationController.fetchNotifications();
    }
    public void markAsRead(NotificationBean bean) {
        notificationController.markAsRead(bean);
    }
    public int countUnreadNotifications() {
        return notificationController.countUnreadNotifications();
    }

}