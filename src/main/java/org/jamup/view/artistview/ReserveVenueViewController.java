package org.jamup.view.artistview;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.jamup.bean.VenueBean;
import org.jamup.controller.LogoutController;
import org.jamup.exception.InvalidFieldException;
import org.jamup.exception.NoVenuesFoundException;
import org.jamup.model.enums.MusicGenre;
import org.jamup.util.*;

import java.util.ArrayList;
import java.util.List;

public class ReserveVenueViewController {

    @FXML private TextField searchField;
    @FXML private DatePicker datePicker;
    @FXML private Label noResultsLabel;
    @FXML private Label notificationBadge;
    @FXML private VBox venueListContainer;
    @FXML private CheckBox checkBlues;
    @FXML private CheckBox checkClassical;
    @FXML private CheckBox checkElectronic;
    @FXML private CheckBox checkFolk;
    @FXML private CheckBox checkFunk;
    @FXML private CheckBox checkHouse;
    @FXML private CheckBox checkIndie;
    @FXML private CheckBox checkJazz;
    @FXML private CheckBox checkMetal;
    @FXML private CheckBox checkPop;
    @FXML private CheckBox checkReggae;
    @FXML private CheckBox checkRock;

    private final JamUpFacade facade = JamUpFacade.getInstance();

    @FXML
    public void initialize() {
        onSearchAction();
        BadgeUtils.updateNotificationBadge(notificationBadge, facade.countUnreadNotifications());
    }

    @FXML
    public void onNotificationsClick() {
        SceneManager.getInstance().navigateTo(SceneManager.SceneName.NOTIFICATIONS);
    }

    @FXML
    public void onLogoutClick() {
        LogoutController.handle();
    }

    @FXML
    public void onSearchAction() {
        venueListContainer.getChildren().clear();
        venueListContainer.getChildren().add(noResultsLabel);
        noResultsLabel.setVisible(false);

        try {
            String venueName= searchField.getText().trim().isEmpty() ? null : searchField.getText().trim();
            VenueBean bean = new VenueBean(venueName, datePicker.getValue(), getSelectedGenres());
            List<VenueBean> results = facade.search(bean);

            //dummy map service call
            MapService.renderMap();
            
            noResultsLabel.setVisible(false);
            venueListContainer.getChildren().clear(); // pulisci i risultati precedenti
            for (VenueBean venue : results) {
                venueListContainer.getChildren().add(createVenueCard(venue));
            }
        } catch (InvalidFieldException | NoVenuesFoundException e) {
            noResultsLabel.setText(e.getMessage());
            noResultsLabel.setVisible(true);
        }
    }

    private List<MusicGenre> getSelectedGenres() {
        List<MusicGenre> genres = new ArrayList<>();
        if (checkBlues.isSelected())      genres.add(MusicGenre.BLUES);
        if (checkClassical.isSelected())  genres.add(MusicGenre.CLASSICAL);
        if (checkElectronic.isSelected()) genres.add(MusicGenre.ELECTRONIC);
        if (checkFolk.isSelected())       genres.add(MusicGenre.FOLK);
        if (checkFunk.isSelected())       genres.add(MusicGenre.FUNK);
        if (checkHouse.isSelected())      genres.add(MusicGenre.HOUSE);
        if (checkIndie.isSelected())      genres.add(MusicGenre.INDIE);
        if (checkJazz.isSelected())       genres.add(MusicGenre.JAZZ);
        if (checkMetal.isSelected())      genres.add(MusicGenre.METAL);
        if (checkPop.isSelected())        genres.add(MusicGenre.POP);
        if (checkReggae.isSelected())     genres.add(MusicGenre.REGGAE);
        if (checkRock.isSelected())       genres.add(MusicGenre.ROCK);
        return genres.isEmpty() ? null : genres;
    }

    private HBox createVenueCard(VenueBean venue) {
        HBox card = new HBox(12);
        card.setStyle("-fx-background-color: #1a1a28; -fx-background-radius: 12; -fx-padding: 16;");

        VBox info = new VBox(4);
        Label name = new Label(venue.getName());
        name.setStyle("-fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold;");
        Label genres = new Label(venue.getGenres().stream()
                .map(Enum::name)
                .reduce((a, b) -> a + " • " + b)
                .orElse(""));
        genres.setStyle("-fx-text-fill: #888888; -fx-font-size: 12px;");
        Label location = new Label(venue.getLocation());
        location.setStyle("-fx-text-fill: #666666; -fx-font-size: 11px;");
        info.getChildren().addAll(name, genres, location);

        card.getChildren().add(info);

        // click sulla card → apre il dettaglio
        card.setOnMouseClicked(e -> onVenueCardClick(venue));
        card.setStyle(card.getStyle() + "-fx-cursor: hand;");

        return card;
    }

    private void onVenueCardClick(VenueBean venue) {
        SceneManager.getInstance().setTransferData(venue);
        SceneManager.getInstance().openPopup(SceneManager.SceneName.VENUE_DETAIL);
    }

}
