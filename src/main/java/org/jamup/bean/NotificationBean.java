package org.jamup.bean;

import java.time.LocalDateTime;

/**
 * Notification bean sent from the Controller to the View.
 */
public record NotificationBean(String id, String message, LocalDateTime timestamp, boolean isRead) {}