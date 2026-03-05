package org.jamup.dao.db;

import org.jamup.dao.interfaces.NotificationDAO;
import org.jamup.model.Notification;

import java.util.List;

public class NotificationDAODB implements NotificationDAO {

    @Override
    public void save(Notification newNotification) {
        /* TODO */
    }

    @Override
    public Notification findById(String id) {
        /* TODO */
        return null;
    }

    @Override
    public List<Notification> findByRecipient(String recipientId) {
        /* TODO */
        return null;
    }

    @Override
    public List<Notification> findUnreadByRecipient(String recipientId) {
        /* TODO */
        return null;
    }

    @Override
    public void update(Notification updatedNotification) {
        /* TODO */
    }

}