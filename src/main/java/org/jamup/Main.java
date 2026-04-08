package org.jamup;

import javafx.application.Application;
import javafx.stage.Stage;
import org.jamup.dao.factory.DAOFactory;
import org.jamup.util.SceneManager;

import java.util.Scanner;

public class Main extends Application {

    @SuppressWarnings("unused")
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Jam Up!");
        System.out.println("Select persistence mode:");
        System.out.println("1. Demo (in-memory)");
        System.out.println("2. CSV (file system)");
        System.out.println("3. DB (MariaDB)");

        String persistenceMode = readChoice(scanner, 3);
        switch (persistenceMode) {
            case "1" -> DAOFactory.getInstance("DEMO");
            case "2" -> DAOFactory.getInstance("CSV");
            case "3" -> DAOFactory.getInstance("DB");
            default -> throw new IllegalStateException("Unexpected value: " + persistenceMode);
        }

        System.out.println("Select interface:");
        System.out.println("1. GUI");
        System.out.println("2. CLI");
        String mode = readChoice(scanner, 2);

        if (mode.equals("1")) {
            launch(args);
        } else {
            new CLIApp(scanner).start();
            System.exit(0);
        }
    }

    private static String readChoice(Scanner scanner, int max) {
        String input;
        do {
            System.out.print("> ");
            input = scanner.nextLine().trim();
        } while (!input.matches("[1-" + max + "]"));
        return input;
    }

    @Override
    public void start(Stage primaryStage) {
        SceneManager.getInstance().setStage(primaryStage);
        SceneManager.getInstance().navigateTo(SceneManager.SceneName.LOGIN);
        primaryStage.show();
    }
}