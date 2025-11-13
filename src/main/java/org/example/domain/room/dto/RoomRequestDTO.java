package org.example.domain.room.dto;

import org.example.domain.room.RoomStatus;
import org.example.domain.room.RoomType;

public record RoomRequestDTO(String roomName, int floor, String building, int maxPeople, String description, RoomStatus roomStatus, RoomType roomType, int price, int pensionId, String image) {
}
