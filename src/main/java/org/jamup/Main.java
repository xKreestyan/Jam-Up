package org.jamup;

import javafx.application.Application;
import javafx.stage.Stage;
import org.jamup.dao.factory.DAOFactory;
import org.jamup.util.SceneManager;

import java.util.Scanner;
import java.util.logging.*;

public class Main extends Application {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%5$s%n");
        //logger configuration
        Logger rootLogger = Logger.getLogger("");
        for (Handler handler : rootLogger.getHandlers()) {
            rootLogger.removeHandler(handler);
        }
        ConsoleHandler consoleHandler = new ConsoleHandler() {
            {
                setOutputStream(System.out);
                setLevel(Level.INFO);
                setFormatter(new SimpleFormatter());
            }
        };
        rootLogger.addHandler(consoleHandler);

        Scanner scanner = new Scanner(System.in);

        logger.info("Welcome to Jam Up!");
        logger.info("Select persistence mode:");
        logger.info("1. Demo (in-memory)");
        logger.info("2. CSV (file system)");
        logger.info("3. DB (MariaDB)");

        String persistenceMode = readChoice(scanner, 3);
        switch (persistenceMode) {
            case "1" -> DAOFactory.getInstance("DEMO");
            case "2" -> DAOFactory.getInstance("CSV");
            case "3" -> DAOFactory.getInstance("DB");
            default -> throw new IllegalStateException("Unexpected value: " + persistenceMode);
        }

        logger.info("Select interface:");
        logger.info("1. GUI");
        logger.info("2. CLI");
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