package org.jamup.model;

import java.util.List;

public class VenueManager extends User {

    private final List<String> venueIds;

    public VenueManager(String id, String email, String password, List<String> venueIds) {
        super(id, email, password);
        this.venueIds = venueIds;
    }

    public List<String> getVenueIds() {
        return venueIds;
    }

}