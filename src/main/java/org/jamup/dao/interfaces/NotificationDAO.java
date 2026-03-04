package org.jamup.dao.interfaces;

import org.jamup.model.Notification;

import java.util.List;

public interface NotificationDAO {

    void save(Notification newNotification);

    List<Notification> findByRecipient(String recipientId);

    List<Notification> findUnreadByRecipient(String recipientId);

    void update(Notification updatedNotification);

}