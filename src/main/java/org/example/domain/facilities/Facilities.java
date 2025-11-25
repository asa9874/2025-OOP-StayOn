package org.example.domain.facilities;

import java.time.LocalDateTime;

import org.example.domain.pension.Pension;

public class Facilities {
    private int id;
    private String name;
    private LocalDateTime openingTime;
    private LocalDateTime closingTime;
    boolean requireReservation;

    private Pension pension;
    private String image;    public Facilities(
        int id,
        String name,
        LocalDateTime openingTime,
        LocalDateTime closingTime,
        boolean requireReservation,
        Pension pension,
        String image
    ) {
        this.id = id;
        this.name = name;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.requireReservation = requireReservation;
        this.pension = pension;
        this.image = image;
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

    public Pension getPension() {
        return pension;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOpeningTime(LocalDateTime openingTime) {
        this.openingTime = openingTime;
    }

    public void setClosingTime(LocalDateTime closingTime) {
        this.closingTime = closingTime;
    }

    public void setRequireReservation(boolean requireReservation) {
        this.requireReservation = requireReservation;
    }    public void setPension(Pension pension) {
        this.pension = pension;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
