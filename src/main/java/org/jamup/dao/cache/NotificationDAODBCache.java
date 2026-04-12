package org.jamup.dao.cache;

import org.jamup.dao.db.NotificationDAODB;
import org.jamup.dao.interfaces.NotificationDAO;
import org.jamup.model.Notification;

import java.util.ArrayList;
import java.util.List;

public class NotificationDAODBCache extends AbstractDAOCache<Notification> implements NotificationDAO {

    private final NotificationDAODB notificationDAOComponent;

    // A flag to keep track if we have fully loaded the notifications for a specific user
    // This assumes one user per session, but we can store it in a set if needed.
    // For simplicity, we'll keep a list of users whose notifications are fully cached.
    private final List<String> fullyCachedRecipients = new ArrayList<>();

    public NotificationDAODBCache() {
        notificationDAOComponent = new NotificationDAODB();
    }

    @Override
    public void save(Notification newNotification) {
        //updates db immediately
        notificationDAOComponent.save(newNotification);
        //cache invalidation
        putInCache(newNotification.getId(), newNotification);
    }

    @Override
    public Notification findById(String id) {
        //cache hit case
        if (isInCache(id)) {
            System.out.println("Dato preso dalla CACHE!");
            return fetchFromCache(id);
        }
        
        //cache miss case
        System.out.println("Dato NON in cache, interrogo il DB...");
        Notification notificationFromDB = notificationDAOComponent.findById(id);
        if (notificationFromDB != null) {
            putInCache(notificationFromDB.getId(), notificationFromDB);
        }

        return notificationFromDB;
    }

    @Override
    public List<Notification> findByRecipient(String recipientId) {
        if (fullyCachedRecipients.contains(recipientId)) {
            System.out.println("Tutte le notifiche dell'utente " + recipientId + " prese dalla CACHE!");
            List<Notification> cachedList = new ArrayList<>();
            for (Notification n : getAllFromCache()) {
                if (n.getRecipientId().equals(recipientId)) {
                    cachedList.add(n);
                }
            }
            return cachedList;
        }

        System.out.println("Notifiche dell'utente " + recipientId + " NON in cache, interrogo il DB...");
        List<Notification> results = notificationDAOComponent.findByRecipient(recipientId);
        for (Notification notification : results) {
            putInCache(notification.getId(), notification);
        }
        fullyCachedRecipients.add(recipientId);
        return results;
    }

    @Override
    public List<Notification> findUnreadByRecipient(String recipientId) {
        // If we have fully loaded this user's notifications, we can filter them from cache
        if (fullyCachedRecipients.contains(recipientId)) {
            System.out.println("Notifiche NON LETTE dell'utente " + recipientId + " prese dalla CACHE (filtrate)!");
            List<Notification> cachedList = new ArrayList<>();
            for (Notification n : getAllFromCache()) {
                if (n.getRecipientId().equals(recipientId) && !n.isRead()) {
                    cachedList.add(n);
                }
            }
            return cachedList;
        }

        System.out.println("Notifiche NON LETTE dell'utente " + recipientId + " NON in cache, interrogo il DB...");
        List<Notification> results = notificationDAOComponent.findUnreadByRecipient(recipientId);
        for (Notification notification : results) {
            putInCache(notification.getId(), notification);
        }
        
        // We DON'T add to fullyCachedRecipients here, because we only fetched UNREAD ones.
        // We still don't have the full picture for findByRecipient.
        
        return results;
    }

    @Override
    public void update(Notification updatedNotification) {
        notificationDAOComponent.update(updatedNotification);
        putInCache(updatedNotification.getId(), updatedNotification);
    }
}