package org.jamup.controller;

import org.jamup.bean.NotificationBean;
import org.jamup.dao.interfaces.NotificationDAO;
import org.jamup.dao.factory.DAOFactory;
import org.jamup.model.Notification;
import org.jamup.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class NotificationController {

    private final NotificationDAO notificationDAO = DAOFactory.getInstance().createNotificationDAO();

    /**
     * Fetches all notifications for the currently logged-in user.
     *
     * @return a list of NotificationBean objects containing notification details.
     */
    public List<NotificationBean> fetchNotifications() {
        String recipientId = SessionManager.getInstance().getCurrentUserId();
        List<Notification> fetchedNotifications = notificationDAO.findByRecipient(recipientId);

        List<NotificationBean> notificationBeans = new ArrayList<>();
        for (Notification notification : fetchedNotifications) {
            notificationBeans.add(
                    new NotificationBean(notification.getId(),
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
        Notification notification = notificationDAO.findById(bean.id());

        notification.markAsRead();
        notificationDAO.update(notification);
    }

    /**
     * Creates and saves a new notification for a specific recipient.
     *
     * @param recipientId the unique identifier of the notification recipient.
     * @param message     the content of the notification message.
     */
    public void createNotification(String recipientId, String message) {
        notificationDAO.save(new Notification(recipientId, message));
    }

    /**
     * Counts the number of unread notifications for the currently logged-in user.
     *
     * @return the total count of unread notifications.
     */
    public int countUnreadNotifications() {
        String recipientId = SessionManager.getInstance().getCurrentUserId();
        return notificationDAO.findUnreadByRecipient(recipientId).size();
    }

}
