package org.example.domain.pension;

import org.example.domain.user.pensionManager.PensionManager;

public class Pension {
    private int id;
    private String name;
    private String address;
    private String phoneNumber;
    private String description;
    private PensionManager pensionManager;
    private String image;

    public Pension() {
    }

    public Pension(String name, String address, String phoneNumber, String description, PensionManager pensionManager, String image) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.pensionManager = pensionManager;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPensionManagerId() {
        return pensionManager != null ? pensionManager.getId() : 0;
    }

    public PensionManager getPensionManager() {
        return pensionManager;
    }

    public void setPensionManager(PensionManager pensionManager) {
        this.pensionManager = pensionManager;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
