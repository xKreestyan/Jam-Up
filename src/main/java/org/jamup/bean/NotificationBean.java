package org.jamup.bean;

import java.time.LocalDateTime;

public class NotificationBean {

    private String id;
    private String recipientId;
    private String message;
    private LocalDateTime timestamp;
    private boolean isRead;

    public String getId() {
        return id;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public boolean isRead() {
        return isRead;
    }

    //notification creation bean constructor (Controller -> View)
    public NotificationBean(String id, String recipientId, String message, LocalDateTime timestamp, boolean isRead) {
        this.id = id;
        this.recipientId = recipientId;
        this.message = message;
        this.timestamp = timestamp;
        this.isRead = isRead;
    }

}