package org.jamup.dao.db;

import org.jamup.dao.factory.DBConnectionFactory;
import org.jamup.dao.interfaces.NotificationDAO;
import org.jamup.model.Notification;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NotificationDAODB implements NotificationDAO {

    /**
     * Converts a single row from a ResultSet into a Notification object.
     *
     * @param rs the ResultSet containing notification data
     * @return a populated Notification object
     * @throws SQLException if a database access error occurs or this method is called on a closed result set
     */
    private Notification resultSetToNotification(ResultSet rs) throws SQLException {
        Notification notification = new Notification(
                rs.getString("recipient_id"),
                rs.getString("message")
        );
        notification.setId(rs.getString("id"));
        notification.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
        if (rs.getBoolean("is_read")) {
            notification.markAsRead();
        }
        return notification;
    }

    @Override
    public void save(Notification newNotification) {
        try {
            newNotification.setId(UUID.randomUUID().toString());
            Connection conn = DBConnectionFactory.getConnection();
            CallableStatement stmt = conn.prepareCall("{CALL SaveNotification(?, ?, ?, ?)}");
            stmt.setString(1, newNotification.getId());
            stmt.setString(2, newNotification.getRecipientId());
            stmt.setString(3, newNotification.getMessage());
            stmt.setObject(4, newNotification.getTimestamp());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("DB error in saveNotification", e);
        }
    }

    @Override
    public Notification findById(String id) {
        try {
            Connection conn = DBConnectionFactory.getConnection();
            CallableStatement stmt = conn.prepareCall("{CALL FindNotificationById(?)}");
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return resultSetToNotification(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("DB error in findNotificationById", e);
        }
        return null;
    }

    @Override
    public List<Notification> findByRecipient(String recipientId) {
        List<Notification> results = new ArrayList<>();
        try {
            Connection conn = DBConnectionFactory.getConnection();
            CallableStatement stmt = conn.prepareCall("{CALL FindNotificationsByRecipient(?)}");
            stmt.setString(1, recipientId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                results.add(resultSetToNotification(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("DB error in findNotificationsByRecipient", e);
        }
        return results;
    }

    @Override
    public List<Notification> findUnreadByRecipient(String recipientId) {
        List<Notification> results = new ArrayList<>();
        try {
            Connection conn = DBConnectionFactory.getConnection();
            CallableStatement stmt = conn.prepareCall("{CALL FindUnreadNotificationsByRecipient(?)}");
            stmt.setString(1, recipientId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                results.add(resultSetToNotification(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("DB error in findUnreadNotificationsByRecipient", e);
        }
        return results;
    }

    @Override
    public void update(Notification updatedNotification) {
        try {
            Connection conn = DBConnectionFactory.getConnection();
            CallableStatement stmt = conn.prepareCall("{CALL UpdateNotification(?, ?)}");
            stmt.setString(1, updatedNotification.getId());
            stmt.setBoolean(2, updatedNotification.isRead());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("DB error in updateNotification", e);
        }
    }

}