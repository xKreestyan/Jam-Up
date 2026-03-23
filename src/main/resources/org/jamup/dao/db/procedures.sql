USE jamup;

-- ============================================================
-- USER PROCEDURES
-- ============================================================

DELIMITER $$

CREATE PROCEDURE FindArtistByEmail(IN p_email VARCHAR(100))
BEGIN
    SELECT a.id, a.email, a.password, a.name,
           GROUP_CONCAT(DISTINCT ai.instrument
                        ORDER BY ai.instrument
                        SEPARATOR '|') AS instruments,
           GROUP_CONCAT(DISTINCT ag.genre
                        ORDER BY ag.genre
                        SEPARATOR '|') AS genres
    FROM artist a
    LEFT JOIN artist_instrument ai ON a.id = ai.artist_id
    LEFT JOIN artist_genre      ag ON a.id = ag.artist_id
    WHERE a.email = p_email
    GROUP BY a.id, a.email, a.password, a.name;
END$$

CREATE PROCEDURE FindArtistById(IN p_id VARCHAR(36))
BEGIN
    SELECT a.id, a.email, a.password, a.name,
           GROUP_CONCAT(DISTINCT ai.instrument
                        ORDER BY ai.instrument
                        SEPARATOR '|') AS instruments,
           GROUP_CONCAT(DISTINCT ag.genre
                        ORDER BY ag.genre
                        SEPARATOR '|') AS genres
    FROM artist a
    LEFT JOIN artist_instrument ai ON a.id = ai.artist_id
    LEFT JOIN artist_genre      ag ON a.id = ag.artist_id
    WHERE a.id = p_id
    GROUP BY a.id, a.email, a.password, a.name;
END$$

CREATE PROCEDURE FindManagerByEmail(IN p_email VARCHAR(100))
BEGIN
    SELECT vm.id, vm.name, vm.email, vm.password,
           GROUP_CONCAT(v.id
                        ORDER BY v.id
                        SEPARATOR '|') AS venue_ids
    FROM venue_manager vm
    LEFT JOIN venue v ON vm.id = v.manager_id
    WHERE vm.email = p_email
    GROUP BY vm.id, vm.name, vm.email, vm.password;
END$$

CREATE PROCEDURE FindManagerById(IN p_id VARCHAR(36))
BEGIN
    SELECT vm.id, vm.name, vm.email, vm.password,
           GROUP_CONCAT(v.id
                        ORDER BY v.id
                        SEPARATOR '|') AS venue_ids
    FROM venue_manager vm
    LEFT JOIN venue v ON vm.id = v.manager_id
    WHERE vm.id = p_id
    GROUP BY vm.id, vm.name, vm.email, vm.password;
END$$

-- ============================================================
-- VENUE PROCEDURES
-- ============================================================

CREATE PROCEDURE FindVenueById(IN p_id VARCHAR(36))
BEGIN
    SELECT v.id, v.name, v.description, v.location, v.manager_id,
           GROUP_CONCAT(DISTINCT vg.genre
                        ORDER BY vg.genre
                        SEPARATOR '|') AS genres,
           GROUP_CONCAT(DISTINCT CONCAT(ts.slot_date, 'T', ts.slot_time)
                        ORDER BY ts.slot_date, ts.slot_time
                        SEPARATOR '|') AS slots
    FROM venue v
    LEFT JOIN venue_genre vg ON v.id = vg.venue_id
    LEFT JOIN time_slot   ts ON v.id = ts.venue_id
    WHERE v.id = p_id
    GROUP BY v.id, v.name, v.description, v.location, v.manager_id;
END$$

CREATE PROCEDURE FindAllVenues()
BEGIN
    SELECT v.id, v.name, v.description, v.location, v.manager_id,
           GROUP_CONCAT(DISTINCT vg.genre
                        ORDER BY vg.genre
                        SEPARATOR '|') AS genres,
           GROUP_CONCAT(DISTINCT CONCAT(ts.slot_date, 'T', ts.slot_time)
                        ORDER BY ts.slot_date, ts.slot_time
                        SEPARATOR '|') AS slots
    FROM venue v
    LEFT JOIN venue_genre vg ON v.id = vg.venue_id
    LEFT JOIN time_slot   ts ON v.id = ts.venue_id
    GROUP BY v.id, v.name, v.description, v.location, v.manager_id;
END$$

CREATE PROCEDURE DeleteTimeSlot(IN p_venue_id VARCHAR(36), IN p_date DATE, IN p_time TIME)
BEGIN
    DELETE FROM time_slot
    WHERE venue_id = p_venue_id AND slot_date = p_date AND slot_time = p_time;
END$$

CREATE PROCEDURE InsertTimeSlot(IN p_venue_id VARCHAR(36), IN p_date DATE, IN p_time TIME)
BEGIN
    INSERT IGNORE INTO time_slot (venue_id, slot_date, slot_time)
    VALUES (p_venue_id, p_date, p_time);
END$$

-- ============================================================
-- RESERVATION PROCEDURES
-- ============================================================

CREATE PROCEDURE SaveReservation(
    IN p_id        VARCHAR(36),
    IN p_notes     TEXT,
    IN p_artist_id VARCHAR(36),
    IN p_venue_id  VARCHAR(36),
    IN p_date      DATE,
    IN p_time      TIME
)
BEGIN
    INSERT INTO reservation (id, notes, status, artist_id, venue_id, slot_date, slot_time)
    VALUES (p_id, p_notes, 'PENDING', p_artist_id, p_venue_id, p_date, p_time);
END$$

CREATE PROCEDURE FindReservationById(IN p_id VARCHAR(36))
BEGIN
    SELECT *
    FROM reservation
    WHERE id = p_id;
END$$

CREATE PROCEDURE FindReservationsByVenueId(IN p_venue_id VARCHAR(36), IN p_status VARCHAR(20))
BEGIN
    SELECT *
    FROM reservation
    WHERE venue_id = p_venue_id
      AND (p_status IS NULL OR status = p_status);
END$$

CREATE PROCEDURE UpdateReservationStatus(IN p_id VARCHAR(36), IN p_status VARCHAR(20))
BEGIN
    UPDATE reservation
    SET status = p_status
    WHERE id = p_id;
END$$

-- ============================================================
-- NOTIFICATION PROCEDURES
-- ============================================================

CREATE PROCEDURE SaveNotification(
    IN p_id           VARCHAR(36),
    IN p_recipient_id VARCHAR(36),
    IN p_message      TEXT,
    IN p_timestamp    DATETIME
)
BEGIN
    INSERT INTO notification (id, recipient_id, message, timestamp, is_read)
    VALUES (p_id, p_recipient_id, p_message, p_timestamp, FALSE);
END$$

CREATE PROCEDURE FindNotificationById(IN p_id VARCHAR(36))
BEGIN
    SELECT *
    FROM notification
    WHERE id = p_id;
END$$

CREATE PROCEDURE FindNotificationsByRecipient(IN p_recipient_id VARCHAR(36))
BEGIN
    SELECT *
    FROM notification
    WHERE recipient_id = p_recipient_id
    ORDER BY timestamp DESC;
END$$

CREATE PROCEDURE FindUnreadNotificationsByRecipient(IN p_recipient_id VARCHAR(36))
BEGIN
    SELECT *
    FROM notification
    WHERE recipient_id = p_recipient_id AND is_read = FALSE
    ORDER BY timestamp DESC;
END$$

CREATE PROCEDURE UpdateNotification(IN p_id VARCHAR(36), IN p_is_read BOOLEAN)
BEGIN
    UPDATE notification
    SET is_read = p_is_read
    WHERE id = p_id;
END$$

DELIMITER ;