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
        InMemoryStorage.getNotifications().add(newNotification);
    }

    @Override
    public Notification findById(String id) {
        for (Notification notification : InMemoryStorage.getNotifications()) {
            if (notification.getId().equals(id)) {
                return notification;
            }
        }
        return null;
    }

    @Override
    public List<Notification> findByRecipient(String recipientId) {
        List<Notification> notifications = new ArrayList<>();
        for(Notification notification : InMemoryStorage.getNotifications()) {
            if(notification.getRecipientId().equals(recipientId)) {
                notifications.add(notification);
            }
        }
        return notifications;
    }

    @Override
    public List<Notification> findUnreadByRecipient(String recipientId) {
        List<Notification> notifications = new ArrayList<>();
        for(Notification notification : InMemoryStorage.getNotifications()) {
            if(notification.getRecipientId().equals(recipientId) && !notification.isRead()) {
                notifications.add(notification);
            }
        }
        return notifications;
    }

    @Override
    public void update(Notification updatedNotification) {
        List<Notification> notifications = InMemoryStorage.getNotifications();
        for (int i = 0; i < notifications.size(); i++) {
            if (notifications.get(i).getId().equals(updatedNotification.getId())) {
                notifications.set(i, updatedNotification);
                return;
            }
        }
    }

    public NotificationDAOMemory() {
        System.out.println("Creato NotificationDAO versione DEMO");
    }

}