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

    /**
     * Retrieves the session associated with the given session ID.
     * @param sessionId the unique identifier of the session
     * @return the Session object, or null if not found or if sessionId is null
     */
    public Session getSession(String sessionId) {
        if (sessionId == null) return null;
        return sessions.get(sessionId);
    }

    /**
     * Creates a new session for the specified user.
     * @param user the user logging into the system
     * @return a unique session ID string
     */
    public String login(User user) {
        String sessionId = UUID.randomUUID().toString();
        sessions.put(sessionId, new Session(user));
        return sessionId;
    }

    /**
     * Terminates the session and clears associated caches.
     * @param sessionId the unique identifier of the session to be removed
     */
    public void logout(String sessionId) {
        if (sessionId != null) {
            sessions.remove(sessionId);
            // Clears the in-memory cache to prevent leaks or incorrect permissions in the next session
            DAOFactory.getInstance().clearCache();
        }
    }

    /**
     * Retrieves the user ID associated with the current session.
     * @param sessionId the unique identifier of the session
     * @return the user ID string, or null if the session is invalid
     */
    public String getCurrentUserId(String sessionId) {
        Session session = getSession(sessionId);
        return session != null ? session.getCurrentUserId() : null;
    }

    /**
     * Checks if the user in the current session is an Artist.
     * @param sessionId the unique identifier of the session
     * @return true if the user is an artist, false otherwise
     */
    public boolean isArtistLoggedIn(String sessionId) {
        Session session = getSession(sessionId);
        return session != null && session.isArtist();
    }

    /**
     * Checks if the user in the current session is a Manager.
     * @param sessionId the unique identifier of the session
     * @return true if the user is a manager, false otherwise
     */
    public boolean isManagerLoggedIn(String sessionId) {
        Session session = getSession(sessionId);
        return session != null && session.isManager();
    }

}