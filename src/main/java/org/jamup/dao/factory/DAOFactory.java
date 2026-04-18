package org.jamup.dao.factory;

import org.jamup.dao.interfaces.*;

public abstract class DAOFactory {

    //abstract methods (factory method pattern)
    public abstract VenueDAO createVenueDAO();
    public abstract ReservationDAO createReservationDAO();
    public abstract UserDAO createUserDAO();
    public abstract NotificationDAO createNotificationDAO();

    // Singleton instance
    private static DAOFactory instance;

    //get singleton instance (concrete factory)
    public static synchronized DAOFactory getInstance(String mode) {
        if (instance == null) {
            switch (mode) {
                case "DEMO":
                    instance = new MemoryDAOFactory();
                    break;
                case "CSV":
                    instance = new CSVDAOFactory();
                    break;
                case "DB":
                    instance = new DBDAOFactory();
                    break;
                default:
                    throw new IllegalArgumentException("Unknown mode: " + mode);
            }
            return instance;
        }
        else throw new IllegalStateException("DAOFactory already initialized");
    }

    public static synchronized DAOFactory getInstance() {
        if (instance == null) {
            throw new IllegalStateException("DAOFactory not initialized");
        }
        return instance;
    }

    // Metodo base per permettere alle factory concrete di resettare il proprio stato
    public void clearCache() {
        // Implementato di default come vuoto, in modo che chi non ne ha bisogno lo ignori.
        // DBDAOFactory lo sovrascriverà per pulire la cache.
    }

}