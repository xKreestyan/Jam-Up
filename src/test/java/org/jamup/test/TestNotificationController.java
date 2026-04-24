package org.jamup.test;

import org.jamup.bean.LoginUserBean;
import org.jamup.controller.LoginController;
import org.jamup.controller.NotificationController;
import org.jamup.dao.factory.DAOFactory;
import org.jamup.exception.InvalidCredentialsException;
import org.jamup.exception.InvalidFieldException;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestNotificationController {

    @BeforeClass
    public static void setup() {
        DAOFactory.getInstance("DEMO");
    }

    private String getValidSessionId() throws InvalidCredentialsException, InvalidFieldException {
        LoginController loginController = new LoginController();
        LoginUserBean bean = new LoginUserBean("mario.rossi@email.com", "artist123");
        return loginController.login(bean);
    }

    @Test
    public void testMarkAllNotificationsAsRead() {
        try {
            String sessionId = getValidSessionId();
            NotificationController controller = new NotificationController();
            
            String recipientId = org.jamup.util.SessionManager.getInstance().getCurrentUserId(sessionId);
            controller.createNotification(recipientId, "Test Notification 2");
            
            //mark all notifications as read
            controller.markAllNotificationsAsRead(sessionId);
            
            int unreadCount = controller.countUnreadNotifications(sessionId);

            assertEquals(0, unreadCount);
        } catch (InvalidCredentialsException | InvalidFieldException e) {
            System.out.println(e.getMessage());
        }
    }
}