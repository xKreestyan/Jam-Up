USE jamup;

-- -----------------------------------------------------------------------------
-- SESSION VARIABLES
-- -----------------------------------------------------------------------------
-- Artists
SET @artist_mario_id   = 'a7b8c9d0-e1f2-3456-0123-567890123457';

-- Venue Managers
SET @manager_verdi_id  = 'a1b2c3d4-e5f6-7890-abcd-ef1234567891';
SET @manager_vivaldi_id = 'b2c3d4e5-f6a7-8901-bcde-f12345678912';

-- Venues
SET @venue_jazz_id     = 'c3d4e5f6-a7b8-9012-cdef-123456789013';
SET @venue_rock_id     = 'd4e5f6a7-b8c9-0123-def0-234567890124';
SET @venue_groove_id   = 'e5f6a7b8-c9d0-1234-ef01-345678901235';
SET @venue_live_id     = 'f6a7b8c9-d0e1-2345-f012-456789012346';
-- -----------------------------------------------------------------------------

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
(@artist_mario_id, 'mario.rossi@email.com', SHA2('artist123', 256), 'Mario Rossi');

INSERT INTO artist_instrument (artist_id, instrument) VALUES
(@artist_mario_id, 'GUITAR');

INSERT INTO artist_genre (artist_id, genre) VALUES
(@artist_mario_id, 'JAZZ'),
(@artist_mario_id, 'BLUES');

-- Venue Managers
INSERT INTO venue_manager (id, name, email, password) VALUES
(@manager_verdi_id, 'Giuseppe Verdi',  'manager1@email.com', SHA2('manager123', 256)),
(@manager_vivaldi_id, 'Antonio Vivaldi', 'manager2@email.com', SHA2('manager456', 256));

-- Venues
INSERT INTO venue (id, name, description, location, manager_id) VALUES
(@venue_jazz_id, 'Jazz Club Milano',  'Historic jazz club in the heart of Milan, featuring live performances every weekend.', 'Via Garibaldi 23, Milano',      @manager_verdi_id),
(@venue_rock_id, 'Rock Arena',        'The loudest stage in Milan, home of rock and metal nights.',                            'Corso Buenos Aires 45, Milano', @manager_verdi_id),
(@venue_groove_id, 'Groove Bar',        'Underground electronic venue with state-of-the-art sound system.',                   'Via Torino 12, Milano',         @manager_vivaldi_id),
(@venue_live_id, 'Live Stage Milano', 'Versatile venue in the city center, open to all genres and emerging artists.',       'Piazza Duomo 8, Milano',        @manager_vivaldi_id);

-- Venue genres
INSERT INTO venue_genre (venue_id, genre) VALUES
(@venue_jazz_id, 'JAZZ'),
(@venue_jazz_id, 'BLUES'),
(@venue_rock_id, 'ROCK'),
(@venue_rock_id, 'METAL'),
(@venue_groove_id, 'ELECTRONIC'),
(@venue_groove_id, 'HOUSE'),
(@venue_live_id, 'POP'),
(@venue_live_id, 'INDIE');

-- Time slots - Jazz Club Milano
INSERT INTO time_slot (venue_id, slot_date, slot_time) VALUES
(@venue_jazz_id, '2026-03-15', '20:00:00'),
(@venue_jazz_id, '2026-03-15', '22:00:00'),
(@venue_jazz_id, '2026-03-20', '21:00:00'),
(@venue_jazz_id, '2026-03-22', '20:00:00'),
(@venue_jazz_id, '2026-03-22', '22:00:00'),
(@venue_jazz_id, '2026-03-27', '21:00:00'),
(@venue_jazz_id, '2026-03-27', '23:00:00'),
(@venue_jazz_id, '2026-04-03', '20:00:00'),
(@venue_jazz_id, '2026-04-03', '22:00:00'),
(@venue_jazz_id, '2026-04-10', '20:00:00'),
(@venue_jazz_id, '2026-04-10', '22:00:00'),
(@venue_jazz_id, '2026-04-17', '21:00:00');

-- Time slots - Rock Arena
INSERT INTO time_slot (venue_id, slot_date, slot_time) VALUES
(@venue_rock_id, '2026-03-16', '21:00:00'),
(@venue_rock_id, '2026-03-16', '23:00:00'),
(@venue_rock_id, '2026-03-21', '22:00:00'),
(@venue_rock_id, '2026-03-28', '21:00:00'),
(@venue_rock_id, '2026-03-28', '23:00:00'),
(@venue_rock_id, '2026-04-04', '21:00:00'),
(@venue_rock_id, '2026-04-04', '23:00:00'),
(@venue_rock_id, '2026-04-11', '22:00:00'),
(@venue_rock_id, '2026-04-18', '21:00:00'),
(@venue_rock_id, '2026-04-18', '23:00:00'),
(@venue_rock_id, '2026-04-25', '22:00:00');

-- Time slots - Groove Bar
INSERT INTO time_slot (venue_id, slot_date, slot_time) VALUES
(@venue_groove_id, '2026-03-17', '22:00:00'),
(@venue_groove_id, '2026-03-17', '23:30:00'),
(@venue_groove_id, '2026-03-19', '23:00:00'),
(@venue_groove_id, '2026-03-24', '22:00:00'),
(@venue_groove_id, '2026-03-24', '23:30:00'),
(@venue_groove_id, '2026-03-26', '23:00:00'),
(@venue_groove_id, '2026-04-02', '22:00:00'),
(@venue_groove_id, '2026-04-02', '23:30:00'),
(@venue_groove_id, '2026-04-09', '23:00:00'),
(@venue_groove_id, '2026-04-16', '22:00:00'),
(@venue_groove_id, '2026-04-16', '23:30:00');

-- Time slots - Live Stage Milano
INSERT INTO time_slot (venue_id, slot_date, slot_time) VALUES
(@venue_live_id, '2026-03-18', '19:00:00'),
(@venue_live_id, '2026-03-18', '20:30:00'),
(@venue_live_id, '2026-03-18', '22:30:00'),
(@venue_live_id, '2026-03-25', '19:00:00'),
(@venue_live_id, '2026-03-25', '21:00:00'),
(@venue_live_id, '2026-04-01', '19:00:00'),
(@venue_live_id, '2026-04-01', '20:30:00'),
(@venue_live_id, '2026-04-01', '22:30:00'),
(@venue_live_id, '2026-04-08', '19:00:00'),
(@venue_live_id, '2026-04-08', '21:00:00'),
(@venue_live_id, '2026-04-15', '19:00:00'),
(@venue_live_id, '2026-04-15', '20:30:00'),
(@venue_live_id, '2026-04-15', '22:30:00'),
(@venue_live_id, '2026-04-22', '20:00:00'),
(@venue_live_id, '2026-04-22', '22:00:00');