# 🎸 Jam Up!

> Connecting musicians with venue owners for live music events.

Jam Up! is a desktop application developed as a university project for the **ISPW** (Software Engineering and Web Design) course at the University of Rome Tor Vergata.

The platform allows **artists** to search for venues and send booking requests, and **venue managers** to review and respond to those requests.

---

## ✨ Features

- 🔍 Search and filter venues by music genre, availability, and location
- 📅 Request a booking for a specific date and time slot
- ✅ Venue managers can accept or reject incoming requests
- 🔔 Real-time notifications on reservation status changes
- 👤 Separate flows for Artists and Venue Managers

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| UI | JavaFX 21 |
| Architecture | MVC + DAO |
| CSV persistence | OpenCSV |
| DB persistence | MariaDB |
| Build | Maven |
| Code quality | SonarCloud |

---

## 🗄️ Persistence Modes

The application supports three persistence modes, selectable at startup:

- **DEMO** — in-memory, no setup required
- **CSV** — file-based persistence via OpenCSV
- **DB** — MariaDB via stored procedures

---

## 🏗️ Design Patterns

- **Singleton** — `DAOFactory`, `SessionManager`, `JamUpFacade`
- **Abstract Factory + Factory Method** — DAO creation by persistence mode
- **Facade** — single entry point from all View Controllers
- **Observer** — reservation status changes trigger notifications
- **Decorator** — optional caching layer on top of DAO implementations

---

## 🚀 Running the Application

### Requirements

- Java 17+
- Maven
- (Optional) MariaDB for DB mode

### Steps

```bash
git clone https://github.com/xKreestyan/Jam-Up.git
cd Jam-Up
mvn clean javafx:run
```

For DB mode, run the SQL scripts located in `src/main/resources/org/jamup/dao/db` (such as `schema.sql`, `procedures.sql`, and `data.sql`) on your MariaDB instance. Then, configure `src/main/resources/org/jamup/dao/db/db.properties` with your credentials before launching.

---

## 📊 Code Quality

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=xKreestyan_Jam-Up&metric=alert_status)](https://sonarcloud.io/summary/overall?id=xKreestyan_Jam-Up&branch=main)

---

## 👨‍💻 Author

**Christian Giovannetti** — ISPW Project, A.Y. 2025/2026