package org.jamup.util;

public class SessionManager {

    private static SessionManager instance;

    private String currentArtistId;
    private String currentManagerId;

    //private constructor (singleton pattern)
    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void setCurrentArtistId(String artistId) {
        this.currentArtistId = artistId;
        this.currentManagerId = null;
    }

    public void setCurrentManagerId(String managerId) {
        this.currentManagerId = managerId;
        this.currentArtistId = null;
    }

    public void logout() {
        this.currentArtistId = null;
        this.currentManagerId = null;
    }

    public String getCurrentArtistId() {
        return currentArtistId;
    }

    public String getCurrentManagerId() {
        return currentManagerId;
    }

    public String getCurrentUserId() {
        if (currentArtistId != null) return currentArtistId;
        if (currentManagerId != null) return currentManagerId;
        return null;
    }

    public boolean isArtistLoggedIn() {
        return currentArtistId != null;
    }

    public boolean isManagerLoggedIn() {
        return currentManagerId != null;
    }

}