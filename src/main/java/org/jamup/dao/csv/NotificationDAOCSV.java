package org.jamup.dao.csv;

import org.jamup.dao.interfaces.NotificationDAO;
import org.jamup.model.Notification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NotificationDAOCSV implements NotificationDAO {

    private static final String NOTIFICATIONS_FILE = "notifications.csv";
    private static final String[] HEADER = {"id", "recipientId", "message", "timestamp", "isRead"};

    // notifications.csv: id, recipientId, message, timestamp, isRead
    /**
     * Converts a CSV row array into a Notification object.
     *
     * @param row An array of strings representing the columns of a notification record.
     * @return A Notification object populated with the data from the row.
     */
    private Notification rowToNotification(String[] row) {
        Notification notification = new Notification(row[1], row[2]);
        notification.setId(row[0]);
        notification.setTimestamp(LocalDateTime.parse(row[3]));
        if (Boolean.parseBoolean(row[4])) {
            notification.markAsRead();
        }
        return notification;
    }

    /**
     * Converts a Notification object into a CSV row array.
     *
     * @param notification The Notification object to convert.
     * @return An array of strings representing the notification record.
     */
    private String[] notificationToRow(Notification notification) {
        return new String[]{
                notification.getId(),
                notification.getRecipientId(),
                notification.getMessage(),
                notification.getTimestamp().toString(),
                String.valueOf(notification.isRead())
        };
    }

    @Override
    public void save(Notification newNotification) {
        String id = UUID.randomUUID().toString();
        newNotification.setId(id);
        CSVStorage.append(NOTIFICATIONS_FILE, notificationToRow(newNotification));
    }

    @Override
    public Notification findById(String id) {
        for (String[] row : CSVStorage.read(NOTIFICATIONS_FILE)) {
            if (row[0].equals(id)) {
                return rowToNotification(row);
            }
        }
        return null;
    }

    @Override
    public List<Notification> findByRecipient(String recipientId) {
        List<Notification> results = new ArrayList<>();
        for (String[] row : CSVStorage.read(NOTIFICATIONS_FILE)) {
            if (row[1].equals(recipientId)) {
                results.add(rowToNotification(row));
            }
        }
        return results;
    }

    @Override
    public List<Notification> findUnreadByRecipient(String recipientId) {
        List<Notification> results = new ArrayList<>();
        for (String[] row : CSVStorage.read(NOTIFICATIONS_FILE)) {
            if (row[1].equals(recipientId) && !Boolean.parseBoolean(row[4])) {
                results.add(rowToNotification(row));
            }
        }
        return results;
    }

    //update of the i-th notification following a status change (marked as read)
    @Override
    public void update(Notification updatedNotification) {
        List<String[]> rows = CSVStorage.read(NOTIFICATIONS_FILE);
        for (int i = 0; i < rows.size(); i++) {
            if (rows.get(i)[0].equals(updatedNotification.getId())) {
                rows.set(i, notificationToRow(updatedNotification));
                break;
            }
        }
        CSVStorage.rewrite(NOTIFICATIONS_FILE, HEADER, rows);
    }

    public NotificationDAOCSV() {
        System.out.println("Creato NotificationDAO versione CSV");
    }

}