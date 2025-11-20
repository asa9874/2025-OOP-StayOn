package org.example.domain.facilities;

import java.time.LocalDateTime;

public class Facilities {
    private int id;
    private String name;
    private LocalDateTime openingTime;
    private LocalDateTime closingTime;
    boolean requireReservation;

    public Facilities(
        int id,
        String name,
        LocalDateTime openingTime,
        LocalDateTime closingTime,
        boolean requireReservation
    ) {
        this.id = id;
        this.name = name;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.requireReservation = requireReservation;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public LocalDateTime getOpeningTime() {
        return openingTime;
    }

    public LocalDateTime getClosingTime() {
        return closingTime;
    }
    public boolean isRequireReservation() {
        return requireReservation;
    }

}
