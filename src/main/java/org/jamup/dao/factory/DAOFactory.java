package org.jamup.dao.factory;

import org.jamup.dao.interfaces.*;

public abstract class DAOFactory {

    //singleton instance
    private static DAOFactory instance;

    //abstract methods (factory method pattern)
    public abstract VenueDAO createVenueDAO();
    public abstract ReservationDAO createReservationDAO();
    public abstract UserDAO createUserDAO();
    public abstract NotificationDAO createNotificationDAO();

    //get singleton instance (concrete factory)
    public static DAOFactory getInstance(String mode) {
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
            }//considerare la sintassi compatta con le espressioni lambda
            return instance;
        }
        else throw new IllegalStateException("DAOFactory already initialized");
    }

    public static DAOFactory getInstance() {
        if (instance == null) {
            throw new IllegalStateException("DAOFactory not initialized");
        }
        return instance;
    }

}