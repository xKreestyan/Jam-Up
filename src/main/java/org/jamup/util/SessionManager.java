package org.jamup.util;

import org.jamup.dao.factory.DAOFactory;
import org.jamup.model.User;

@SuppressWarnings("java:S6548")
public class SessionManager {

    //thread-safe initialization
    private static class InstanceHolder {
        private static final SessionManager instance = new SessionManager();
    }

    private SessionManager() {}

    public static SessionManager getInstance() {
        return InstanceHolder.instance;
    }

    private Session currentSession;

    public Session getCurrentSession() {
        return currentSession;
    }

    public void login(User user) {
        if (currentSession != null) {
            throw new IllegalStateException("A user is already logged in.");
        }
        this.currentSession = new Session(user);
    }

    public void logout() {
        this.currentSession = null;
        // Pulisce la cache in memoria per evitare leak o permessi errati alla prossima sessione
        DAOFactory.getInstance().clearCache();
    }

    public String getCurrentUserId() {
        return currentSession != null ? currentSession.getCurrentUserId() : null;
    }

    public boolean isArtistLoggedIn() {
        return currentSession != null && currentSession.isArtist();
    }

    public boolean isManagerLoggedIn() {
        return currentSession != null && currentSession.isManager();
    }

}