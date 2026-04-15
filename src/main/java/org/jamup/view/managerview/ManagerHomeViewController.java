package org.jamup.view.managerview;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.jamup.bean.ReservationBean;
import org.jamup.exception.NoReservationsFoundException;
import org.jamup.model.enums.ReservationStatus;
import org.jamup.util.*;

import java.util.ArrayList;
import java.util.List;

public class ManagerHomeViewController {

    @FXML private Button tabPending;
    @FXML private Button tabAccepted;
    @FXML private Button tabRejected;
    @FXML private VBox reservationListContainer;
    @FXML private Label noReservationsLabel;
    @FXML private Label notificationBadge;

    private final JamUpFacade facade = JamUpFacade.getInstance();
    private ReservationStatus currentTab = ReservationStatus.PENDING;
    private List<ReservationBean> allReservations = new ArrayList<>();

    private static final String STYLE_ACTIVE_LEFT   = "-fx-background-color: #a855f7; -fx-text-fill: white; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 20 0 0 20; -fx-border-color: #a855f7; -fx-border-radius: 20 0 0 20; -fx-padding: 8 20 8 20; -fx-cursor: hand;";
    private static final String STYLE_ACTIVE_MID    = "-fx-background-color: #a855f7; -fx-text-fill: white; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 0; -fx-border-color: #a855f7; -fx-border-radius: 0; -fx-padding: 8 20 8 20; -fx-cursor: hand;";
    private static final String STYLE_ACTIVE_RIGHT  = "-fx-background-color: #a855f7; -fx-text-fill: white; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 0 20 20 0; -fx-border-color: #a855f7; -fx-border-radius: 0 20 20 0; -fx-padding: 8 20 8 20; -fx-cursor: hand;";
    private static final String STYLE_INACTIVE_LEFT = "-fx-background-color: transparent; -fx-text-fill: #888888; -fx-font-size: 13px; -fx-background-radius: 20 0 0 20; -fx-border-color: #2a2a3e; -fx-border-radius: 20 0 0 20; -fx-padding: 8 20 8 20; -fx-cursor: hand;";
    private static final String STYLE_INACTIVE_MID  = "-fx-background-color: transparent; -fx-text-fill: #888888; -fx-font-size: 13px; -fx-background-radius: 0; -fx-border-color: #2a2a3e; -fx-border-radius: 0; -fx-padding: 8 20 8 20; -fx-cursor: hand;";
    private static final String STYLE_INACTIVE_RIGHT= "-fx-background-color: transparent; -fx-text-fill: #888888; -fx-font-size: 13px; -fx-background-radius: 0 20 20 0; -fx-border-color: #2a2a3e; -fx-border-radius: 0 20 20 0; -fx-padding: 8 20 8 20; -fx-cursor: hand;";

    @FXML
    public void initialize() {
        try {
            allReservations = facade.fetchReservations(null); //null: every reservation
        } catch (NoReservationsFoundException e) {
            allReservations = new ArrayList<>();
        }
        showReservations(ReservationStatus.PENDING);
        BadgeUtils.updateNotificationBadge(notificationBadge, facade.countUnreadNotifications());
    }

    @FXML
    public void onTabPendingClick() {
        tabPending.setStyle(STYLE_ACTIVE_LEFT);
        tabAccepted.setStyle(STYLE_INACTIVE_MID);
        tabRejected.setStyle(STYLE_INACTIVE_RIGHT);
        currentTab = ReservationStatus.PENDING;
        showReservations(currentTab);
    }

    @FXML
    public void onTabAcceptedClick() {
        tabPending.setStyle(STYLE_INACTIVE_LEFT);
        tabAccepted.setStyle(STYLE_ACTIVE_MID);
        tabRejected.setStyle(STYLE_INACTIVE_RIGHT);
        currentTab = ReservationStatus.ACCEPTED;
        showReservations(currentTab);
    }

    @FXML
    public void onTabRejectedClick() {
        tabPending.setStyle(STYLE_INACTIVE_LEFT);
        tabAccepted.setStyle(STYLE_INACTIVE_MID);
        tabRejected.setStyle(STYLE_ACTIVE_RIGHT);
        currentTab = ReservationStatus.REJECTED;
        showReservations(currentTab);
    }

    @FXML
    public void onNotificationsClick() {
        SceneManager.getInstance().navigateTo(SceneManager.SceneName.NOTIFICATIONS);
    }

    @FXML
    public void onLogoutClick() {
        LogoutController.handle();
    }

    private void showReservations(ReservationStatus status) {
        reservationListContainer.getChildren().clear();
        reservationListContainer.getChildren().add(noReservationsLabel);
        noReservationsLabel.setVisible(false);

        List<ReservationBean> filtered = allReservations.stream()
                .filter(r -> r.getStatus() == status)
                .toList();

        if (filtered.isEmpty()) {
            noReservationsLabel.setVisible(true);
        } else {
            for (ReservationBean r : filtered) {
                reservationListContainer.getChildren().add(createReservationCard(r));
            }
        }
    }

    private VBox createReservationCard(ReservationBean reservation) {
        VBox card = new VBox(6);
        card.setStyle("-fx-background-color: #1a1a28; -fx-background-radius: 12; -fx-padding: 16;");

        // nome artista + venue
        Label artistLabel = new Label(reservation.getArtistName() + " → " + reservation.getVenueName());
        artistLabel.setStyle("-fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold;");

        // data e ora
        Label slotLabel = new Label("📅 " + reservation.getReservedSlot().getDate()
                + "  🕐 " + reservation.getReservedSlot().getTime());
        slotLabel.setStyle("-fx-text-fill: #888888; -fx-font-size: 13px;");

        // strumenti e generi
        String instruments = reservation.getArtistInstruments().stream()
                .map(i -> i.name().charAt(0) + i.name().substring(1).toLowerCase())
                .reduce((a, b) -> a + ", " + b).orElse("");
        Label instrumentsLabel = new Label("🎸 " + instruments);
        instrumentsLabel.setStyle("-fx-text-fill: #666677; -fx-font-size: 12px;");

        // note
        Label notesLabel = new Label(reservation.getNotes().isEmpty() ? "" : "📝 " + reservation.getNotes());
        notesLabel.setStyle("-fx-text-fill: #555566; -fx-font-size: 12px;");
        notesLabel.setWrapText(true);

        card.getChildren().addAll(artistLabel, slotLabel, instrumentsLabel);
        if (!reservation.getNotes().isEmpty()) {
            card.getChildren().add(notesLabel);
        }

        // bottoni accept/reject solo se PENDING
        if (reservation.getStatus() == ReservationStatus.PENDING) {
            HBox buttons = new HBox(10);
            buttons.setStyle("-fx-padding: 8 0 0 0;");

            Button acceptBtn = new Button("✓  Accept");
            acceptBtn.setStyle("-fx-background-color: #22c55e; -fx-text-fill: white; " +
                    "-fx-background-radius: 8; -fx-padding: 6 16 6 16; -fx-cursor: hand;");
            acceptBtn.setOnAction(e -> onAccept(reservation));

            Button rejectBtn = new Button("✕  Reject");
            rejectBtn.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; " +
                    "-fx-background-radius: 8; -fx-padding: 6 16 6 16; -fx-cursor: hand;");
            rejectBtn.setOnAction(e -> onReject(reservation));

            buttons.getChildren().addAll(acceptBtn, rejectBtn);
            card.getChildren().add(buttons);
        }

        return card;
    }

    private void onAccept(ReservationBean reservation) {
        facade.accept(reservation.getReservationId());
        reloadAllReservations();
        showReservations(currentTab);
    }

    private void onReject(ReservationBean reservation) {
        facade.reject(reservation.getReservationId());
        reloadAllReservations();
        showReservations(currentTab);
    }

    private void reloadAllReservations() {
        try {
            allReservations = facade.fetchReservations(null);
        } catch (NoReservationsFoundException e) {
            allReservations = new ArrayList<>();
        }
    }

}