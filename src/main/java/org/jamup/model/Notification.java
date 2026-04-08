package org.jamup.model;

import java.time.LocalDateTime;

public class Notification {

    String id;
    String recipientId;
    String message;
    LocalDateTime timestamp;
    boolean isRead;

    public void markAsRead() {
        isRead = true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public Notification(String recipientId, String message) {
        this.recipientId = recipientId;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.isRead = false;
    }

}