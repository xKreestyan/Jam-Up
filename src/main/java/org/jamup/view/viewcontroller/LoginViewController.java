package org.jamup.view.viewcontroller;

import org.jamup.bean.LoginUserBean;
import org.jamup.controller.LoginController;
import org.jamup.exception.InvalidCredentialsException;
import org.jamup.util.SessionManager;

public class LoginViewController {

//    public void onLoginButtonClick() {
//        try {
//            LoginUserBean bean = new LoginUserBean(
//                    emailField.getText(),
//                    passwordField.getText()
//            );
//            LoginController controller = new LoginController();
//            controller.login(bean);
//
//            if (SessionManager.getInstance().isArtistLoggedIn()) {
//                sceneManager.navigateTo("ARTIST_HOME");
//            } else {
//                sceneManager.navigateTo("MANAGER_HOME");
//            }
//
//        } catch (InvalidCredentialsException e) {
//            errorLabel.setText("Invalid email or password");
//        }
//    }

}