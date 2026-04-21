package org.jamup.util;

import org.jamup.dao.factory.DAOFactory;
import org.jamup.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

    private final Map<String, Session> sessions = new HashMap<>();

    public Session getSession(String sessionId) {
        if (sessionId == null) return null;
        return sessions.get(sessionId);
    }

    public String login(User user) {
        String sessionId = UUID.randomUUID().toString();
        sessions.put(sessionId, new Session(user));
        return sessionId;
    }

    public void logout(String sessionId) {
        if (sessionId != null) {
            sessions.remove(sessionId);
            // Pulisce la cache in memoria per evitare leak o permessi errati alla prossima sessione
            DAOFactory.getInstance().clearCache();
        }
    }

    public String getCurrentUserId(String sessionId) {
        Session session = getSession(sessionId);
        return session != null ? session.getCurrentUserId() : null;
    }

    public boolean isArtistLoggedIn(String sessionId) {
        Session session = getSession(sessionId);
        return session != null && session.isArtist();
    }

    public boolean isManagerLoggedIn(String sessionId) {
        Session session = getSession(sessionId);
        return session != null && session.isManager();
    }

}