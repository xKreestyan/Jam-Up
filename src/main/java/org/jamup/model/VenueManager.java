package org.jamup.model;

import java.util.List;

public class VenueManager extends User {

    private List<Venue> venues;

    public VenueManager(String id, String email, String password, List<Venue> venues) {
        super(id, email, password);
        this.venues = venues;
    }

    public List<Venue> getVenues() {
        return venues;
    }

    public void setVenues(List<Venue> venues) {
        this.venues = venues;
    }

}