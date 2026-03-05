package org.jamup.controller;

import org.jamup.bean.NotificationBean;
import org.jamup.dao.interfaces.NotificationDAO;
import org.jamup.exception.NoReservationsFoundException;
import org.jamup.factory.DAOFactory;
import org.jamup.model.Notification;
import org.jamup.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class NotificationController {

    /**
     * Fetches all notifications for the currently logged-in user.
     *
     * @return a list of NotificationBean objects containing notification details.
     */
    public List<NotificationBean> fetchNotifications() {
        NotificationDAO notificationDAO = DAOFactory.getInstance().createNotificationDAO();
        String recipientId = SessionManager.getInstance().getCurrentUserId();
        List<Notification> fetchedNotifications = notificationDAO.findByRecipient(recipientId);

        List<NotificationBean> notificationBeans = new ArrayList<>();
        for (Notification notification : fetchedNotifications) {
            notificationBeans.add(
                    new NotificationBean(notification.getId(), notification.getRecipientId(),
                            notification.getMessage(), notification.getTimestamp(), notification.isRead())
            );
        }
        return notificationBeans;
    }

    /**
     * Marks a specific notification as read in the database.
     *
     * @param bean the NotificationBean representing the notification to be updated.
     */
    public void markAsRead(NotificationBean bean) {
        NotificationDAO notificationDAO = DAOFactory.getInstance().createNotificationDAO();
        Notification notification = notificationDAO.findById(bean.getId());

        notification.markAsRead();
        notificationDAO.update(notification);
    }

}
