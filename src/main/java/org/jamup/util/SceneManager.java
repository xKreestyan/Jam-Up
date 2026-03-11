package org.jamup.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneManager {

    public enum SceneName {
        LOGIN          ("/org/jamup/view/Login.fxml"),
        NOTIFICATIONS  ("/org/jamup/view/Notifications.fxml"),
        RESERVE_VENUE  ("/org/jamup/view/artistview/ReserveVenue.fxml"),
        VENUE_DETAIL   ("/org/jamup/view/artistview/VenueDetail.fxml"),
        RESERVATION_SUMMARY ("/org/jamup/view/artistview/ReservationSummary.fxml"),
        MANAGER_HOME   ("/org/jamup/view/managerview/ManagerHome.fxml");

        private final String path;

        SceneName(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }

    public static SceneManager instance;
    private Stage stage;

    //private constructor (singleton pattern)
    private SceneManager() {}

    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
            return instance;
        }
        return instance;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void navigateTo(SceneName sceneName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(sceneName.getPath()));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load scene: " + sceneName.getPath(), e);
        }
    }

}