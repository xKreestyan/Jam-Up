package org.jamup.dao.memory;

import org.jamup.dao.interfaces.NotificationDAO;
import org.jamup.model.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NotificationDAOMemory implements NotificationDAO {

    @Override
    public void save(Notification newNotification) {
        String id = UUID.randomUUID().toString();
        newNotification.setId(id);
        InMemoryStorage.getNotifications().put(id, newNotification);
    }

    @Override
    public Notification findById(String id) {
        return InMemoryStorage.getNotifications().get(id);
    }

    @Override
    public List<Notification> findByRecipient(String recipientId) {
        List<Notification> notifications = new ArrayList<>();
        for(Notification notification : InMemoryStorage.getNotifications().values()) {
            if(notification.getRecipientId().equals(recipientId)) {
                notifications.add(notification);
            }
        }
        return notifications;
    }

    @Override
    public List<Notification> findUnreadByRecipient(String recipientId) {
        List<Notification> notifications = new ArrayList<>();
        for(Notification notification : InMemoryStorage.getNotifications().values()) {
            if(notification.getRecipientId().equals(recipientId) && !notification.isRead()) {
                notifications.add(notification);
            }
        }
        return notifications;
    }

    //update of the i-th notification following a status change (marked as read)
    @Override
    public void update(Notification updatedNotification) {
        InMemoryStorage.getNotifications().put(updatedNotification.getId(), updatedNotification);
    }

    public NotificationDAOMemory() {
        System.out.println("Creato NotificationDAO versione DEMO");
    }

}