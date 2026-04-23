package org.jamup.util;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import org.jamup.bean.VenueBean;
import org.jamup.exception.SceneLoadException;
import org.jamup.view.artistview.VenueDetailViewController;

@SuppressWarnings("java:S6548")
public class SceneManager {

    public enum SceneName {
        LOGIN               ("/org/jamup/view/Login.fxml"),
        NOTIFICATIONS       ("/org/jamup/view/Notifications.fxml"),
        RESERVE_VENUE       ("/org/jamup/view/artistview/ReserveVenue.fxml"),
        VENUE_DETAIL        ("/org/jamup/view/artistview/VenueDetail.fxml"),
        MANAGER_HOME        ("/org/jamup/view/managerview/ManagerHome.fxml");

        private final String path;

        SceneName(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }

    //thread-safe initialization
    private static class InstanceHolder {
        private static final SceneManager instance = new SceneManager();
    }

    private Stage stage;
    private final StackPane rootPane = new StackPane();

    private SceneManager() {}

    public static SceneManager getInstance() {
        return InstanceHolder.instance;
    }

    /**
     * Configures the primary stage for the application.
     *
     * @param stage The primary Stage object provided by the JavaFX platform.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
        stage.setTitle("Jam Up!");
        stage.setScene(new Scene(rootPane, 960, 780));
    }

    /**
     * Navigates to a specific scene by loading the FXML file and updating the root pane.
     *
     * @param sceneName The enum value representing the target scene.
     * @throws SceneLoadException if the FXML file cannot be loaded.
     */
    public void navigateTo(SceneName sceneName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(sceneName.getPath()));
            Parent content = loader.load();
            rootPane.getChildren().clear();
            rootPane.getChildren().add(content);

            if (sceneName == SceneName.LOGIN) {
                stage.setWidth(470);
                stage.setHeight(640);
                stage.centerOnScreen();
            } else {
                stage.setWidth(960);
                stage.setHeight(780);
                stage.centerOnScreen();
            }

        } catch (IOException e) {
            throw new SceneLoadException("Failed to load scene: " + sceneName.getPath(), e);
        }
    }

    /**
     * Opens a popup overlay on top of the current scene and passes data to its controller.
     *
     * @param sceneName The enum value representing the target popup scene.
     * @param venue     The VenueBean data object to be passed to the controller.
     * @throws SceneLoadException if the FXML file cannot be loaded.
     */
    public void openPopup(SceneName sceneName, VenueBean venue) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(sceneName.getPath()));
            Parent popupContent = loader.load();

            if (venue != null && sceneName == SceneName.VENUE_DETAIL) {
                VenueDetailViewController controller = loader.getController();
                controller.setVenue(venue);
            }

            // overlay scuro semitrasparente
            StackPane overlay = new StackPane();
            overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.65);");
            StackPane.setAlignment(popupContent, Pos.CENTER);
            overlay.getChildren().add(popupContent);

            rootPane.getChildren().add(overlay);
        } catch (IOException e) {
            throw new SceneLoadException("Failed to load popup: " + sceneName.getPath(), e);
        }
    }

    /**
     * Closes the currently active popup by removing the top-most overlay from the root pane.
     */
    public void closePopup() {
        if (rootPane.getChildren().size() > 1) {
            rootPane.getChildren().remove(rootPane.getChildren().size() - 1);
        }
    }

}