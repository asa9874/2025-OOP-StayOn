package org.example.domain.room;

import org.example.domain.room.dto.RoomRequestDTO;
import java.util.List;

public class RoomController {
    private static RoomController instance;
    private final RoomService roomService;
    
    private RoomController(RoomService roomService) {
        this.roomService = roomService;
    }
    
    public static void initialize(RoomService roomService) {
        if (instance == null) {
            instance = new RoomController(roomService);
        }
    }

    public static RoomController getInstance() {
        if (instance == null) {
            throw new IllegalStateException("RoomController가 초기화되지 않았습니다. Init.initializeDependencies()를 먼저 호출하세요.");
        }
        return instance;
    }

    public Room findById(int id) {
        return roomService.findById(id);
    }

    public Room findByRoomName(String roomName) {
        return roomService.findByRoomName(roomName);
    }

    public Room save(RoomRequestDTO requestDTO) {
        return roomService.save(requestDTO);
    }

    public List<Room> findAll() {
        return roomService.findAll();
    }

    public List<Room> findByRoomType(RoomType roomType) {
        return roomService.findByRoomType(roomType);
    }

    public List<Room> findByRoomStatus(RoomStatus roomStatus) {
        return roomService.findByRoomStatus(roomStatus);
    }

    public List<Room> findByPensionId(int pensionId) {
        return roomService.findByPensionId(pensionId);
    }

    public void deleteById(int id) {
        roomService.deleteById(id);
    }

    public Room updateRoomStatus(int id, RoomStatus newStatus) {
        return roomService.updateRoomStatus(id, newStatus);
    }
}
