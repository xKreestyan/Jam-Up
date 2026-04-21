package org.jamup.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.jamup.util.JamUpFacade;
import org.jamup.util.SceneManager;

public class LogoutController {

    private LogoutController() {}

    public static void handle() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to logout?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                JamUpFacade.getInstance().logout();
                SceneManager.getInstance().navigateTo(SceneManager.SceneName.LOGIN);
            }
        });
    }

}