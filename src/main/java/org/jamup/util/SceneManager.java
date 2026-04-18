package org.jamup.util;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import org.jamup.exception.SceneLoadException;

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
    private Object transferData;

    private SceneManager() {}

    public static SceneManager getInstance() {
        return InstanceHolder.instance;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        stage.setTitle("Jam Up!");
        stage.setScene(new Scene(rootPane, 960, 780));
    }

    public void setTransferData(Object data) {
        this.transferData = data;
    }

    public Object getTransferData() {
        return transferData;
    }

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

    public void openPopup(SceneName sceneName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(sceneName.getPath()));
            Parent popupContent = loader.load();

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

    public void closePopup() {
        if (rootPane.getChildren().size() > 1) {
            rootPane.getChildren().remove(rootPane.getChildren().size() - 1);
        }
    }

}