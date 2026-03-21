package org.jamup.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.jamup.bean.NotificationBean;
import org.jamup.controller.NotificationController;
import org.jamup.util.SceneManager;
import org.jamup.util.SessionManager;

import java.util.List;

public class NotificationsViewController {

    @FXML private VBox notificationsContainer;
    @FXML private Label noNotificationsLabel;

    private final NotificationController notificationController = new NotificationController();

    @FXML
    public void initialize() {
        loadNotifications();
    }

    @FXML
    public void onBackClick() {
        if (SessionManager.getInstance().isArtistLoggedIn()) {
            SceneManager.getInstance().navigateTo(SceneManager.SceneName.RESERVE_VENUE);
        } else {
            SceneManager.getInstance().navigateTo(SceneManager.SceneName.MANAGER_HOME);
        }
    }

    @FXML
    public void onLogoutClick() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to logout?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                SessionManager.getInstance().logout();
                SceneManager.getInstance().navigateTo(SceneManager.SceneName.LOGIN);
            }
        });
    }

    @FXML
    public void onMarkAllReadClick() {
        List<NotificationBean> notifications = notificationController.fetchNotifications();
        for (NotificationBean notification : notifications) {
            if (!notification.isRead()) {
                notificationController.markAsRead(notification);
            }
        }
        loadNotifications();
    }

    private void loadNotifications() {
        notificationsContainer.getChildren().clear();
        notificationsContainer.getChildren().add(noNotificationsLabel);
        noNotificationsLabel.setVisible(false);

        List<NotificationBean> notifications = notificationController.fetchNotifications();

        if (notifications.isEmpty()) {
            noNotificationsLabel.setVisible(true);
        } else {
            for (NotificationBean notification : notifications) {
                notificationsContainer.getChildren().add(createNotificationCard(notification));
            }
        }
    }

    private VBox createNotificationCard(NotificationBean notification) {
        VBox card = new VBox(6);

        if (!notification.isRead()) {
            card.setStyle("-fx-background-color: #1e1e30; -fx-background-radius: 12; -fx-padding: 16; -fx-cursor: hand;");
        } else {
            card.setStyle("-fx-background-color: #1a1a28; -fx-background-radius: 12; -fx-padding: 16;");
        }

        // riga superiore: pallino (se non letta) + timestamp
        HBox topRow = new HBox(8);
        topRow.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        if (!notification.isRead()) {
            Label dot = new Label("●");
            dot.setStyle("-fx-text-fill: #a855f7; -fx-font-size: 8px;");
            topRow.getChildren().add(dot);
        }

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label timestampLabel = new Label(notification.getTimestamp().toLocalDate().toString()
                + "  " + notification.getTimestamp().toLocalTime().withSecond(0).withNano(0));
        timestampLabel.setStyle("-fx-text-fill: #555566; -fx-font-size: 11px;");

        topRow.getChildren().addAll(spacer, timestampLabel);

        // messaggio
        Label messageLabel = new Label(notification.getMessage());
        messageLabel.setWrapText(true);
        messageLabel.setStyle(!notification.isRead()
                ? "-fx-text-fill: white; -fx-font-size: 13px;"
                : "-fx-text-fill: #888888; -fx-font-size: 13px;");

        card.getChildren().addAll(topRow, messageLabel);

        // click → segna come letta
        if (!notification.isRead()) {
            card.setOnMouseClicked(e -> {
                notificationController.markAsRead(notification);
                loadNotifications();
            });
        }

        return card;
    }

}