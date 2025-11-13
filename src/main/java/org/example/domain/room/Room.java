package org.example.domain.room;

public class Room {
    private int id;
    private String roomName;
    private int floor;
    private String building;
    private int maxPeople;
    private String description;
    private RoomStatus roomStatus;
    private RoomType roomType;
    private int price;
    private int pensionId;
    private String image;

    public Room() {
    }

    public Room(String roomName, int floor, String building, int maxPeople, String description, RoomStatus roomStatus, RoomType roomType, int price, int pensionId, String image) {
        this.roomName = roomName;
        this.floor = floor;
        this.building = building;
        this.maxPeople = maxPeople;
        this.description = description;
        this.roomStatus = roomStatus;
        this.roomType = roomType;
        this.price = price;
        this.pensionId = pensionId;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public int getMaxPeople() {
        return maxPeople;
    }

    public void setMaxPeople(int maxPeople) {
        this.maxPeople = maxPeople;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RoomStatus getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(RoomStatus roomStatus) {
        this.roomStatus = roomStatus;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPensionId() {
        return pensionId;
    }

    public void setPensionId(int pensionId) {
        this.pensionId = pensionId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
