package org.jamup.test;

import org.jamup.bean.LoginUserBean;
import org.jamup.controller.LoginController;
import org.jamup.dao.factory.DAOFactory;
import org.jamup.exception.InvalidCredentialsException;
import org.jamup.exception.InvalidFieldException;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TestLoginController {

    @BeforeClass
    public static void setup() {
        DAOFactory.getInstance("DEMO");
    }

    @Test
    public void testLoginWithValidArtistCredentials() {
        LoginController controller = new LoginController();
        try {
            LoginUserBean bean = new LoginUserBean("mario.rossi@email.com", "artist123");
            String sessionId = controller.login(bean);
            assertTrue(sessionId != null && !sessionId.isEmpty());
        } catch (InvalidCredentialsException | InvalidFieldException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testLoginWithInvalidPassword() {
        LoginController controller = new LoginController();
        boolean exceptionThrown = false;
        try {
            LoginUserBean bean = new LoginUserBean("mario.rossi@email.com", "wrongpassword");
            controller.login(bean);
        } catch (InvalidCredentialsException | InvalidFieldException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

}