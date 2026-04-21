package org.jamup.controller;

import org.jamup.bean.NotificationBean;
import org.jamup.dao.interfaces.NotificationDAO;
import org.jamup.dao.factory.DAOFactory;
import org.jamup.model.Notification;
import org.jamup.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class NotificationController {

    /**
     * Fetches all notifications for the currently logged-in user.
     *
     * @param sessionId the ID of the current user's session
     * @return a list of NotificationBean objects containing notification details.
     */
    public List<NotificationBean> fetchNotifications(String sessionId) {
        NotificationDAO notificationDAO = DAOFactory.getInstance().createNotificationDAO();
        String recipientId = SessionManager.getInstance().getCurrentUserId(sessionId);
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
        NotificationDAO notificationDAO = DAOFactory.getInstance().createNotificationDAO();
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
        NotificationDAO notificationDAO = DAOFactory.getInstance().createNotificationDAO();
        notificationDAO.save(new Notification(recipientId, message));
    }

    /**
     * Counts the number of unread notifications for the currently logged-in user.
     *
     * @param sessionId the ID of the current user's session
     * @return the total count of unread notifications.
     */
    public int countUnreadNotifications(String sessionId) {
        NotificationDAO notificationDAO = DAOFactory.getInstance().createNotificationDAO();
        String recipientId = SessionManager.getInstance().getCurrentUserId(sessionId);
        return notificationDAO.findUnreadByRecipient(recipientId).size();
    }

    /**
     * Marks all unread notifications for the current user as read.
     *
     * @param sessionId the ID of the current user's session
     */
    public void markAllNotificationsAsRead(String sessionId) {
        NotificationDAO notificationDAO = DAOFactory.getInstance().createNotificationDAO();
        String recipientId = SessionManager.getInstance().getCurrentUserId(sessionId);
        List<Notification> unread = notificationDAO.findUnreadByRecipient(recipientId);
        for (Notification notification : unread) {
            notification.markAsRead();
            notificationDAO.update(notification);
        }
    }

}