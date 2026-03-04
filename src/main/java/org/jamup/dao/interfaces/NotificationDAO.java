package org.jamup.dao.interfaces;

import org.jamup.model.Notification;

import java.util.List;

public interface NotificationDAO {

    /**
     * Persists a new notification in the data store.
     *
     * @param newNotification the notification object to be saved
     */
    void save(Notification newNotification);

    /**
     * Retrieves all notifications associated with a specific recipient.
     *
     * @param recipientId the unique identifier of the recipient
     * @return a list of all notifications for the given recipient
     */
    List<Notification> findByRecipient(String recipientId);

    /**
     * Retrieves only the unread notifications for a specific recipient.
     *
     * @param recipientId the unique identifier of the recipient
     * @return a list of unread notifications for the given recipient
     */
    List<Notification> findUnreadByRecipient(String recipientId);

    /**
     * Updates the state of an existing notification (e.g., marking it as read).
     *
     * @param updatedNotification the notification object containing updated information
     */
    void update(Notification updatedNotification);

}