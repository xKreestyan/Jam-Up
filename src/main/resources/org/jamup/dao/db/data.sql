USE jamup;

-- reset data
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE notification;
TRUNCATE TABLE reservation;
TRUNCATE TABLE time_slot;
TRUNCATE TABLE venue_genre;
TRUNCATE TABLE venue;
TRUNCATE TABLE venue_manager;
TRUNCATE TABLE artist_genre;
TRUNCATE TABLE artist_instrument;
TRUNCATE TABLE artist;
SET FOREIGN_KEY_CHECKS = 1;

-- Artists
INSERT INTO artist (id, email, password, name) VALUES
('a7b8c9d0-e1f2-3456-0123-567890123457', 'mario.rossi@email.com', SHA2('artist123', 256), 'Mario Rossi');

INSERT INTO artist_instrument (artist_id, instrument) VALUES
('a7b8c9d0-e1f2-3456-0123-567890123457', 'GUITAR');

INSERT INTO artist_genre (artist_id, genre) VALUES
('a7b8c9d0-e1f2-3456-0123-567890123457', 'JAZZ'),
('a7b8c9d0-e1f2-3456-0123-567890123457', 'BLUES');

-- Venue Managers
INSERT INTO venue_manager (id, name, email, password) VALUES
('a1b2c3d4-e5f6-7890-abcd-ef1234567891', 'Giuseppe Verdi',  'manager1@email.com', SHA2('manager123', 256)),
('b2c3d4e5-f6a7-8901-bcde-f12345678912', 'Antonio Vivaldi', 'manager2@email.com', SHA2('manager456', 256));

-- Venues
INSERT INTO venue (id, name, description, location, manager_id) VALUES
('c3d4e5f6-a7b8-9012-cdef-123456789013', 'Jazz Club Milano',  'Historic jazz club in the heart of Milan, featuring live performances every weekend.', 'Via Garibaldi 23, Milano',      'a1b2c3d4-e5f6-7890-abcd-ef1234567891'),
('d4e5f6a7-b8c9-0123-def0-234567890124', 'Rock Arena',        'The loudest stage in Milan, home of rock and metal nights.',                          'Corso Buenos Aires 45, Milano', 'a1b2c3d4-e5f6-7890-abcd-ef1234567891'),
('e5f6a7b8-c9d0-1234-ef01-345678901235', 'Groove Bar',        'Underground electronic venue with state-of-the-art sound system.',                   'Via Torino 12, Milano',         'b2c3d4e5-f6a7-8901-bcde-f12345678912'),
('f6a7b8c9-d0e1-2345-f012-456789012346', 'Live Stage Milano', 'Versatile venue in the city center, open to all genres and emerging artists.',       'Piazza Duomo 8, Milano',        'b2c3d4e5-f6a7-8901-bcde-f12345678912');

-- Venue genres
INSERT INTO venue_genre (venue_id, genre) VALUES
('c3d4e5f6-a7b8-9012-cdef-123456789013', 'JAZZ'),
('c3d4e5f6-a7b8-9012-cdef-123456789013', 'BLUES'),
('d4e5f6a7-b8c9-0123-def0-234567890124', 'ROCK'),
('d4e5f6a7-b8c9-0123-def0-234567890124', 'METAL'),
('e5f6a7b8-c9d0-1234-ef01-345678901235', 'ELECTRONIC'),
('e5f6a7b8-c9d0-1234-ef01-345678901235', 'HOUSE'),
('f6a7b8c9-d0e1-2345-f012-456789012346', 'POP'),
('f6a7b8c9-d0e1-2345-f012-456789012346', 'INDIE');

-- Time slots - Jazz Club Milano
INSERT INTO time_slot (venue_id, slot_date, slot_time) VALUES
('c3d4e5f6-a7b8-9012-cdef-123456789013', '2026-03-15', '20:00:00'),
('c3d4e5f6-a7b8-9012-cdef-123456789013', '2026-03-15', '22:00:00'),
('c3d4e5f6-a7b8-9012-cdef-123456789013', '2026-03-20', '21:00:00'),
('c3d4e5f6-a7b8-9012-cdef-123456789013', '2026-03-22', '20:00:00'),
('c3d4e5f6-a7b8-9012-cdef-123456789013', '2026-03-22', '22:00:00'),
('c3d4e5f6-a7b8-9012-cdef-123456789013', '2026-03-27', '21:00:00'),
('c3d4e5f6-a7b8-9012-cdef-123456789013', '2026-03-27', '23:00:00'),
('c3d4e5f6-a7b8-9012-cdef-123456789013', '2026-04-03', '20:00:00'),
('c3d4e5f6-a7b8-9012-cdef-123456789013', '2026-04-03', '22:00:00'),
('c3d4e5f6-a7b8-9012-cdef-123456789013', '2026-04-10', '20:00:00'),
('c3d4e5f6-a7b8-9012-cdef-123456789013', '2026-04-10', '22:00:00'),
('c3d4e5f6-a7b8-9012-cdef-123456789013', '2026-04-17', '21:00:00');

-- Time slots - Rock Arena
INSERT INTO time_slot (venue_id, slot_date, slot_time) VALUES
('d4e5f6a7-b8c9-0123-def0-234567890124', '2026-03-16', '21:00:00'),
('d4e5f6a7-b8c9-0123-def0-234567890124', '2026-03-16', '23:00:00'),
('d4e5f6a7-b8c9-0123-def0-234567890124', '2026-03-21', '22:00:00'),
('d4e5f6a7-b8c9-0123-def0-234567890124', '2026-03-28', '21:00:00'),
('d4e5f6a7-b8c9-0123-def0-234567890124', '2026-03-28', '23:00:00'),
('d4e5f6a7-b8c9-0123-def0-234567890124', '2026-04-04', '21:00:00'),
('d4e5f6a7-b8c9-0123-def0-234567890124', '2026-04-04', '23:00:00'),
('d4e5f6a7-b8c9-0123-def0-234567890124', '2026-04-11', '22:00:00'),
('d4e5f6a7-b8c9-0123-def0-234567890124', '2026-04-18', '21:00:00'),
('d4e5f6a7-b8c9-0123-def0-234567890124', '2026-04-18', '23:00:00'),
('d4e5f6a7-b8c9-0123-def0-234567890124', '2026-04-25', '22:00:00');

-- Time slots - Groove Bar
INSERT INTO time_slot (venue_id, slot_date, slot_time) VALUES
('e5f6a7b8-c9d0-1234-ef01-345678901235', '2026-03-17', '22:00:00'),
('e5f6a7b8-c9d0-1234-ef01-345678901235', '2026-03-17', '23:30:00'),
('e5f6a7b8-c9d0-1234-ef01-345678901235', '2026-03-19', '23:00:00'),
('e5f6a7b8-c9d0-1234-ef01-345678901235', '2026-03-24', '22:00:00'),
('e5f6a7b8-c9d0-1234-ef01-345678901235', '2026-03-24', '23:30:00'),
('e5f6a7b8-c9d0-1234-ef01-345678901235', '2026-03-26', '23:00:00'),
('e5f6a7b8-c9d0-1234-ef01-345678901235', '2026-04-02', '22:00:00'),
('e5f6a7b8-c9d0-1234-ef01-345678901235', '2026-04-02', '23:30:00'),
('e5f6a7b8-c9d0-1234-ef01-345678901235', '2026-04-09', '23:00:00'),
('e5f6a7b8-c9d0-1234-ef01-345678901235', '2026-04-16', '22:00:00'),
('e5f6a7b8-c9d0-1234-ef01-345678901235', '2026-04-16', '23:30:00');

-- Time slots - Live Stage Milano
INSERT INTO time_slot (venue_id, slot_date, slot_time) VALUES
('f6a7b8c9-d0e1-2345-f012-456789012346', '2026-03-18', '19:00:00'),
('f6a7b8c9-d0e1-2345-f012-456789012346', '2026-03-18', '20:30:00'),
('f6a7b8c9-d0e1-2345-f012-456789012346', '2026-03-18', '22:30:00'),
('f6a7b8c9-d0e1-2345-f012-456789012346', '2026-03-25', '19:00:00'),
('f6a7b8c9-d0e1-2345-f012-456789012346', '2026-03-25', '21:00:00'),
('f6a7b8c9-d0e1-2345-f012-456789012346', '2026-04-01', '19:00:00'),
('f6a7b8c9-d0e1-2345-f012-456789012346', '2026-04-01', '20:30:00'),
('f6a7b8c9-d0e1-2345-f012-456789012346', '2026-04-01', '22:30:00'),
('f6a7b8c9-d0e1-2345-f012-456789012346', '2026-04-08', '19:00:00'),
('f6a7b8c9-d0e1-2345-f012-456789012346', '2026-04-08', '21:00:00'),
('f6a7b8c9-d0e1-2345-f012-456789012346', '2026-04-15', '19:00:00'),
('f6a7b8c9-d0e1-2345-f012-456789012346', '2026-04-15', '20:30:00'),
('f6a7b8c9-d0e1-2345-f012-456789012346', '2026-04-15', '22:30:00'),
('f6a7b8c9-d0e1-2345-f012-456789012346', '2026-04-22', '20:00:00'),
('f6a7b8c9-d0e1-2345-f012-456789012346', '2026-04-22', '22:00:00');
