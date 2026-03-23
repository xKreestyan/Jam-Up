CREATE DATABASE IF NOT EXISTS jamup;
USE jamup;

CREATE TABLE IF NOT EXISTS artist (
    id          VARCHAR(36)  PRIMARY KEY,
    email       VARCHAR(100) NOT NULL UNIQUE,
    password    VARCHAR(64)  NOT NULL,
    name        VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS artist_instrument (
    artist_id   VARCHAR(36) NOT NULL,
    instrument  VARCHAR(50) NOT NULL,
    PRIMARY KEY (artist_id, instrument),
    FOREIGN KEY (artist_id) REFERENCES artist(id)
);

CREATE TABLE IF NOT EXISTS artist_genre (
    artist_id   VARCHAR(36) NOT NULL,
    genre       VARCHAR(50) NOT NULL,
    PRIMARY KEY (artist_id, genre),
    FOREIGN KEY (artist_id) REFERENCES artist(id)
);

CREATE TABLE IF NOT EXISTS venue_manager (
    id          VARCHAR(36)  PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    email       VARCHAR(100) NOT NULL UNIQUE,
    password    VARCHAR(64)  NOT NULL
);

CREATE TABLE IF NOT EXISTS venue (
    id          VARCHAR(36)  PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description TEXT,
    location    VARCHAR(200) NOT NULL,
    manager_id  VARCHAR(36)  NOT NULL,
    FOREIGN KEY (manager_id) REFERENCES venue_manager(id)
);

CREATE TABLE IF NOT EXISTS venue_genre (
    venue_id    VARCHAR(36) NOT NULL,
    genre       VARCHAR(50) NOT NULL,
    PRIMARY KEY (venue_id, genre),
    FOREIGN KEY (venue_id) REFERENCES venue(id)
);

CREATE TABLE IF NOT EXISTS time_slot (
    id          INT          AUTO_INCREMENT PRIMARY KEY,
    venue_id    VARCHAR(36)  NOT NULL,
    slot_date   DATE         NOT NULL,
    slot_time   TIME         NOT NULL,
    UNIQUE (venue_id, slot_date, slot_time),
    FOREIGN KEY (venue_id) REFERENCES venue(id)
);

CREATE TABLE IF NOT EXISTS reservation (
    id          VARCHAR(36)  PRIMARY KEY,
    notes       TEXT,
    status      ENUM('PENDING','ACCEPTED','REJECTED') NOT NULL DEFAULT 'PENDING',
    artist_id   VARCHAR(36)  NOT NULL,
    venue_id    VARCHAR(36)  NOT NULL,
    slot_date   DATE         NOT NULL,
    slot_time   TIME         NOT NULL,
    FOREIGN KEY (artist_id) REFERENCES artist(id),
    FOREIGN KEY (venue_id)  REFERENCES venue(id)
);

CREATE TABLE IF NOT EXISTS notification (
    id           VARCHAR(36)   PRIMARY KEY,
    recipient_id VARCHAR(36)   NOT NULL,
    message      TEXT          NOT NULL,
    timestamp    DATETIME      NOT NULL,
    is_read      BOOLEAN       NOT NULL DEFAULT FALSE
);