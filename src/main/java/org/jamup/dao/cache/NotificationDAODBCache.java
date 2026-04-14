package org.jamup.dao.cache;

import org.jamup.dao.db.NotificationDAODB;
import org.jamup.dao.interfaces.NotificationDAO;
import org.jamup.model.Notification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationDAODBCache extends AbstractDAOCache<Notification> implements NotificationDAO {

    private final NotificationDAODB notificationDAOComponent;

    // A flag to keep track if we have fully loaded the notifications for a specific user
    private final List<String> fullyCachedRecipients = new ArrayList<>();
    
    // Un indice secondario per trovare velocemente le notifiche per utente (recipientId)
    private final Map<String, List<Notification>> recipientIndex = new HashMap<>();

    public NotificationDAODBCache() {
        notificationDAOComponent = new NotificationDAODB();
    }
    
    @Override
    public void putInCache(String notificationId, Notification notification) {
        super.putInCache(notificationId, notification);
        
        // Aggiorniamo anche l'indice secondario
        String recipientId = notification.getRecipientId();
        // Se la chiave non esiste già, creiamo una nuova lista vuota e l'associamo a quella chiave
        // Se già esiste, non facciamo nulla
        recipientIndex.putIfAbsent(recipientId, new ArrayList<>());

        // Prendo il riferimento alla lista di notifiche associata alla chiave recipientId
        List<Notification> userNotifications = recipientIndex.get(recipientId);
        // Evitiamo duplicati nell'indice (capitano quando aggiorniamo lo stato della notifica)
        boolean exists = false;
        for (int i = 0; i < userNotifications.size(); i++) {
            if (userNotifications.get(i).getId().equals(notification.getId())) {
                // Aggiorniamo lo stato della notifica
                userNotifications.set(i, notification);
                //uscita dal ciclo
                exists = true;
                break;
            }
        }
        // Entriamo se non è stato rilevato un duplicato
        if (!exists) {
            userNotifications.add(notification);
        }
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
        //cache hit case
        if (fullyCachedRecipients.contains(recipientId)) {
            System.out.println("Tutte le notifiche dell'utente " + recipientId + " prese dalla CACHE!");
            //restituisce una COPIA della lista delle notifiche associata alla chiave recipientId (se esiste, altrimenti una lista vuota)
            return new ArrayList<>(recipientIndex.getOrDefault(recipientId, new ArrayList<>()));
        }

        //cache miss case
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
            List<Notification> userNotifications = recipientIndex.getOrDefault(recipientId, new ArrayList<>());
            for (Notification n : userNotifications) {
                if (!n.isRead()) {
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