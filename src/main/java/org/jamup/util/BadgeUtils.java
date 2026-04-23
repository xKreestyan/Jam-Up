package org.jamup.util;

import javafx.scene.control.Label;

public class BadgeUtils {

    private BadgeUtils() {}

    /**
     * Updates the visibility and text of a notification badge based on the unread count.
     *
     * @param badge  the Label component representing the badge
     * @param unread the number of unread items
     */
    public static void updateNotificationBadge(Label badge, int unread) {
        if (unread > 0) {
            badge.setText(String.valueOf(unread));
            badge.setVisible(true);
        } else {
            badge.setVisible(false);
        }
    }

}