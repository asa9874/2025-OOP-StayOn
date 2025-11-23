package org.example.domain.room;

import org.example.domain.room.strategy.RoomInitStrategy;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RoomRepository {
    private static RoomRepository instance;
    private List<Room> roomList;
    private final RoomInitStrategy initStrategy;
    private int nextId = 1;

    private RoomRepository(RoomInitStrategy initStrategy) {
        this.initStrategy = initStrategy;
        this.roomList = initStrategy.initializeList();
        updateNextId();
    }

    private void updateNextId() {
        int maxId = roomList.stream()
                .mapToInt(Room::getId)
                .max()
                .orElse(0);
        nextId = maxId + 1;
    }

    public static void initialize(RoomInitStrategy initStrategy) {
        if (instance == null) {
            instance = new RoomRepository(initStrategy);
        }
    }

    public static RoomRepository getInstance() {
        if (instance == null) {
            throw new IllegalStateException("RoomRepository가 초기화되지 않았습니다. Init.initializeDependencies()를 먼저 호출하세요.");
        }
        return instance;
    }

    // 여기부터 CRUD 메서드
    public List<Room> findAll() {
        return roomList;
    }

    public Optional<Room> findById(int id) {
        return roomList.stream()
                .filter(room -> room.getId() == id)
                .findFirst();
    }

    public Optional<Room> findByRoomName(String roomName) {
        return roomList.stream()
                .filter(room -> room.getRoomName().equals(roomName))
                .findFirst();
    }

    public List<Room> findByRoomType(RoomType roomType) {
        return roomList.stream()
                .filter(room -> room.getRoomType() == roomType)
                .collect(Collectors.toList());
    }

    public List<Room> findByRoomStatus(RoomStatus roomStatus) {
        return roomList.stream()
                .filter(room -> room.getRoomStatus() == roomStatus)
                .collect(Collectors.toList());
    }

    public List<Room> findByPensionId(int pensionId) {
        return roomList.stream()
                .filter(room -> room.getPensionId() == pensionId)
                .collect(Collectors.toList());
    }

    public Room save(Room room) {
        room.setId(nextId++);
        roomList.add(room);
        return room;
    }

    public void deleteById(int id) {
        roomList.removeIf(room -> room.getId() == id);
    }

    public void returnToDefaultData() {
        this.roomList = initStrategy.initializeList();
        updateNextId();
    }
}
