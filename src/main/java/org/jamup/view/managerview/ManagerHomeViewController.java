package org.jamup.view.managerview;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.jamup.util.SceneManager;
import org.jamup.util.SessionManager;

public class ManagerHomeViewController {

    @FXML
    public void onNotificationsClick() {
        SceneManager.getInstance().navigateTo(SceneManager.SceneName.NOTIFICATIONS);
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
    public void onTabPendingClick() {

    }

    @FXML
    public void onTabAcceptedClick() {

    }

    @FXML
    public void onTabRejectedClick() {

    }

}
