package org.jamup.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import org.jamup.bean.LoginUserBean;
import org.jamup.util.JamUpFacade;
import org.jamup.exception.InvalidCredentialsException;
import org.jamup.exception.InvalidFieldException;
import org.jamup.util.SceneManager;

public class LoginViewController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final JamUpFacade facade = JamUpFacade.getInstance();

    @FXML
    public void onLoginButtonClick() {
        try {
            LoginUserBean bean = new LoginUserBean(emailField.getText(), passwordField.getText());
            facade.login(bean);

            if (facade.isArtistLoggedIn()) {
                SceneManager.getInstance().navigateTo(SceneManager.SceneName.RESERVE_VENUE);
            } else if (facade.isManagerLoggedIn()) {
                SceneManager.getInstance().navigateTo(SceneManager.SceneName.MANAGER_HOME);
            }
        } catch (InvalidFieldException | InvalidCredentialsException e) {
            errorLabel.setText(e.getMessage());
            errorLabel.setVisible(true);
        }
    }

}