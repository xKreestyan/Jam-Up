package org.jamup;

import javafx.application.Application;
import javafx.stage.Stage;

import org.jamup.factory.DAOFactory;
import org.jamup.util.SceneManager;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        DAOFactory.getInstance("DEMO");
        SceneManager.getInstance().setStage(primaryStage);
        SceneManager.getInstance().navigateTo(SceneManager.SceneName.LOGIN);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}