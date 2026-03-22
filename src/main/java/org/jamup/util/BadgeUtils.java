package org.jamup.util;

import javafx.scene.control.Label;

public class BadgeUtils {

    private BadgeUtils() {}

    public static void updateNotificationBadge(Label badge, int unread) {
        if (unread > 0) {
            badge.setText(String.valueOf(unread));
            badge.setVisible(true);
        } else {
            badge.setVisible(false);
        }
    }

}