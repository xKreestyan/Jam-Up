package org.jamup.view.artistview;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.jamup.bean.ReservationBean;
import org.jamup.bean.VenueBean;
import org.jamup.controller.ReserveVenueController;
import org.jamup.exception.InvalidFieldException;
import org.jamup.model.TimeSlot;
import org.jamup.util.SceneManager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class VenueDetailViewController {

    @FXML private Label nameLabel;
    @FXML private Label genresLabel;
    @FXML private Label locationLabel;
    @FXML private Label descriptionLabel;
    @FXML private HBox dateButtonContainer;
    @FXML private FlowPane timeButtonContainer;
    @FXML private TextArea notesArea;
    @FXML private Label errorLabel;

    private VenueBean currentVenue;
    private LocalDate selectedDate;
    private LocalTime selectedTime;
    private Button selectedDateButton = null;
    private Button selectedTimeButton = null;

    private final ReserveVenueController reserveVenueController = new ReserveVenueController();

    @FXML
    public void initialize() {
        currentVenue = (VenueBean) SceneManager.getInstance().getTransferData();
        // popoli la schermata con i dati del locale
        nameLabel.setText(currentVenue.getName());
        locationLabel.setText("📍 " + currentVenue.getLocation());
        descriptionLabel.setText(currentVenue.getDescription());

        // generi separati da "•"
        String genres = currentVenue.getGenres().stream()
                .map(g -> g.name().charAt(0) + g.name().substring(1).toLowerCase())
                .reduce((a, b) -> a + " • " + b)
                .orElse("");
        genresLabel.setText(genres);

        createDateButtons();
    }

    private void createDateButtons() {
        // estrai le date distinte dagli slot disponibili
        List<LocalDate> dates = currentVenue.getAvailableSlots().stream()
                .map(TimeSlot::getDate)
                .distinct()
                .sorted()
                .toList();

        for (LocalDate date : dates) {
            Button btn = new Button();
            btn.setText(date.getDayOfWeek().name().substring(0, 3) + "\n"
                    + date.getDayOfMonth() + "\n"
                    + date.getMonth().name().substring(0, 3));
            btn.setStyle(/* stile non selezionato */
                    "-fx-background-color: #1a1a28; -fx-text-fill: #cccccc;" +
                            "-fx-background-radius: 10; -fx-padding: 10 14 10 14;" +
                            "-fx-font-size: 12px; -fx-cursor: hand;");
            btn.setOnAction(e -> onDateButtonClick(btn, date));
            dateButtonContainer.getChildren().add(btn);
        }
    }

    private void onDateButtonClick(Button clickedButton, LocalDate date) {
        // deseleziona il bottone precedente
        if (selectedDateButton != null) {
            selectedDateButton.setStyle(
                    "-fx-background-color: #1a1a28; -fx-text-fill: #cccccc;" +
                            "-fx-background-radius: 10; -fx-padding: 10 14 10 14;" +
                            "-fx-font-size: 12px; -fx-cursor: hand;");
        }

        // seleziona il nuovo bottone
        selectedDateButton = clickedButton;
        selectedDateButton.setStyle(
                "-fx-background-color: #a855f7; -fx-text-fill: white;" +
                        "-fx-background-radius: 10; -fx-padding: 10 14 10 14;" +
                        "-fx-font-size: 12px; -fx-cursor: hand;");

        selectedDate = date;
        selectedTime = null; // resetta l'orario selezionato

        updateTimeButtons(date);
    }

    private void updateTimeButtons(LocalDate date) {
        timeButtonContainer.getChildren().clear();

        List<LocalTime> times = currentVenue.getAvailableSlots().stream()
                .filter(slot -> slot.getDate().equals(date))
                .map(TimeSlot::getTime)
                .sorted()
                .toList();

        for (LocalTime time : times) {
            Button btn = new Button("🕐 " + time.toString());
            btn.setStyle(
                    "-fx-background-color: #1a1a28; -fx-text-fill: #cccccc;" +
                            "-fx-background-radius: 20; -fx-padding: 8 16 8 16;" +
                            "-fx-font-size: 13px; -fx-cursor: hand;");
            btn.setOnAction(e -> onTimeButtonClick(btn, time));
            timeButtonContainer.getChildren().add(btn);
        }
    }

    private void onTimeButtonClick(Button clickedButton, LocalTime time) {
        if (selectedTimeButton != null) {
            selectedTimeButton.setStyle(
                    "-fx-background-color: #1a1a28; -fx-text-fill: #cccccc;" +
                            "-fx-background-radius: 20; -fx-padding: 8 16 8 16;" +
                            "-fx-font-size: 13px; -fx-cursor: hand;");
        }

        selectedTimeButton = clickedButton;
        selectedTimeButton.setStyle(
                "-fx-background-color: #a855f7; -fx-text-fill: white;" +
                        "-fx-background-radius: 20; -fx-padding: 8 16 8 16;" +
                        "-fx-font-size: 13px; -fx-cursor: hand;");

        selectedTime = time;
    }

    @FXML
    public void onCloseClick() {
        SceneManager.getInstance().closePopup();
    }

    @FXML
    public void onBookNowClick() {
        if (selectedDate == null || selectedTime == null) {
            errorLabel.setText("Please select a date and time.");
            errorLabel.setVisible(true);
            return;
        }

        try {
            TimeSlot slot = new TimeSlot(selectedDate, selectedTime);
            ReservationBean bean = new ReservationBean(currentVenue.getId(), slot, notesArea.getText());
            reserveVenueController.confirmReservation(bean);
            SceneManager.getInstance().closePopup();
        } catch (InvalidFieldException e) {
            errorLabel.setText(e.getMessage());
            errorLabel.setVisible(true);
        }
    }

}
