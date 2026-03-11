package org.jamup.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import org.jamup.bean.LoginUserBean;
import org.jamup.controller.LoginController;
import org.jamup.exception.InvalidCredentialsException;
import org.jamup.exception.InvalidFieldException;
import org.jamup.util.SceneManager;
import org.jamup.util.SessionManager;

public class LoginViewController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final LoginController loginController = new LoginController();

    @FXML
    public void onLoginButtonClick() {
        try {
            LoginUserBean bean = new LoginUserBean(emailField.getText(), passwordField.getText());
            loginController.login(bean);

            if (SessionManager.getInstance().isArtistLoggedIn()) {
                //go to reserve venue screen
                SceneManager.getInstance().navigateTo(SceneManager.SceneName.RESERVE_VENUE);
            } else if (SessionManager.getInstance().isManagerLoggedIn()) {
                //go to manager home screen
                SceneManager.getInstance().navigateTo(SceneManager.SceneName.MANAGER_HOME);
            }
        } catch (InvalidFieldException e) {
            errorLabel.setText("Please enter a valid email and password");
            errorLabel.setVisible(true);
        } catch (InvalidCredentialsException e) {
            errorLabel.setText("Invalid email or password");
            errorLabel.setVisible(true);
        }
    }

}