package org.jamup.dao.cache;

import org.jamup.dao.interfaces.NotificationDAO;
import org.jamup.model.Notification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationDAOCache extends AbstractDAOCache<Notification> implements NotificationDAO {

    private final NotificationDAO notificationDAOComponent;

    //a flag to keep track if we have fully loaded the notifications for a specific user
    private final List<String> fullyCachedRecipients = new ArrayList<>();

    //a secondary index to quickly find notifications per user (recipientId)
    private final Map<String, List<Notification>> notificationsForUser = new HashMap<>();

    public NotificationDAOCache(NotificationDAO notificationDAOComponent) {
        this.notificationDAOComponent = notificationDAOComponent;
    }

    /**
     * Adds a notification to the primary cache and updates the secondary index for user-specific lookups.
     *
     * @param notificationId the unique identifier of the notification
     * @param notification the notification object to cache
     */
    @Override
    public void putInCache(String notificationId, Notification notification) {
        super.putInCache(notificationId, notification);

        //update the secondary index as well
        String recipientId = notification.getRecipientId();
        //if the key doesn't exist yet, we create a new empty list and associate it with that key
        //if it already exists, we do nothing
        notificationsForUser.putIfAbsent(recipientId, new ArrayList<>());

        //get the reference to the list of notifications associated with the recipientId key
        List<Notification> userNotifications = notificationsForUser.get(recipientId);
        //avoid duplicates in the index (happens when we update the state of the notification)
        boolean exists = false;
        for (int i = 0; i < userNotifications.size(); i++) {
            if (userNotifications.get(i).getId().equals(notification.getId())) {
                //update the state of the notification
                userNotifications.set(i, notification);
                //exit the loop
                exists = true;
                break;
            }
        }
        //enter if no duplicate was detected
        if (!exists) {
            userNotifications.add(notification);
        }
    }

    /**
     * Saves a new notification to the persistent storage and updates the cache.
     *
     * @param newNotification the notification to be saved
     */
    @Override
    public void save(Notification newNotification) {
        //updates storage immediately
        notificationDAOComponent.save(newNotification);
        //cache invalidation
        putInCache(newNotification.getId(), newNotification);
    }

    /**
     * Retrieves a notification by its ID, checking the cache before querying storage.
     *
     * @param id the unique identifier of the notification
     * @return the notification if found, null otherwise
     */
    @Override
    public Notification findById(String id) {
        //cache hit case
        if (isInCache(id)) {
            System.out.println("Data fetched from CACHE!");
            return fetchFromCache(id);
        }

        //cache miss case
        System.out.println("Data NOT in cache, querying storage...");
        Notification notificationFromStorage = notificationDAOComponent.findById(id);
        if (notificationFromStorage != null) {
            putInCache(notificationFromStorage.getId(), notificationFromStorage);
        }

        return notificationFromStorage;
    }

    /**
     * Retrieves all notifications for a specific recipient. If the user's notifications
     * are fully cached, it returns the cached list; otherwise, it fetches from storage.
     *
     * @param recipientId the ID of the user receiving the notifications
     * @return a list of notifications for the specified user
     */
    @Override
    public List<Notification> findByRecipient(String recipientId) {
        //cache hit case
        if (fullyCachedRecipients.contains(recipientId)) {
            System.out.println("All notifications for user " + recipientId + " fetched from CACHE!");
            //returns a COPY of the list of notifications associated with the recipientId key (if it exists, otherwise an empty list)
            return new ArrayList<>(notificationsForUser.getOrDefault(recipientId, new ArrayList<>()));
        }

        //cache miss case
        System.out.println("Notifications for user " + recipientId + " NOT in cache, querying storage...");
        List<Notification> results = notificationDAOComponent.findByRecipient(recipientId);
        for (Notification notification : results) {
            putInCache(notification.getId(), notification);
        }
        fullyCachedRecipients.add(recipientId);
        return results;
    }

    /**
     * Retrieves all unread notifications for a specific recipient. Uses the cache
     * if the user's notifications are fully loaded.
     *
     * @param recipientId the ID of the user receiving the notifications
     * @return a list of unread notifications
     */
    @Override
    public List<Notification> findUnreadByRecipient(String recipientId) {
        //if we have fully loaded this user's notifications, we can filter them from cache
        if (fullyCachedRecipients.contains(recipientId)) {
            System.out.println("UNREAD notifications for user " + recipientId + " fetched from CACHE (filtered)!");
            List<Notification> cachedList = new ArrayList<>();
            List<Notification> userNotifications = notificationsForUser.getOrDefault(recipientId, new ArrayList<>());
            for (Notification n : userNotifications) {
                if (!n.isRead()) {
                    cachedList.add(n);
                }
            }
            return cachedList;
        }

        System.out.println("UNREAD notifications for user " + recipientId + " NOT in cache, querying storage...");
        List<Notification> results = notificationDAOComponent.findUnreadByRecipient(recipientId);
        for (Notification notification : results) {
            putInCache(notification.getId(), notification);
        }

        //we DON'T add to fullyCachedRecipients here, because we only fetched UNREAD ones.
        //we still don't have the full picture for findByRecipient.

        return results;
    }

    /**
     * Updates an existing notification in storage and refreshes it in the cache.
     *
     * @param updatedNotification the notification with updated information
     */
    @Override
    public void update(Notification updatedNotification) {
        notificationDAOComponent.update(updatedNotification);
        putInCache(updatedNotification.getId(), updatedNotification);
    }

}