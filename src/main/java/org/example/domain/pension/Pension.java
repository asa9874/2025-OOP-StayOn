package org.example.domain.pension;

public class Pension {
    private int id;
    private String name;
    private String address;
    private String phoneNumber;
    private String description;
    private int pensionManagerId;

    public Pension() {
    }

    public Pension(String name, String address, String phoneNumber, String description, int pensionManagerId) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.pensionManagerId = pensionManagerId;
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
        return pensionManagerId;
    }

    public void setPensionManagerId(int pensionManagerId) {
        this.pensionManagerId = pensionManagerId;
    }
}
