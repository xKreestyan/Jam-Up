package org.jamup.model;

import java.util.List;

public class VenueManager extends User {

    private String name;
    private List<String> venueIds;

    public VenueManager(String id, String name, String email, String password, List<String> venueIds) {
        super(id, email, password);
        this.name = name;
        this.venueIds = venueIds;
    }

    public List<String> getVenueIds() {
        return venueIds;
    }

}