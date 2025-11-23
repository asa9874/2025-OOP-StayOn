package org.example.domain.room;

import org.example.domain.pension.Pension;
import org.example.domain.pension.PensionRepository;
import org.example.domain.room.dto.RoomRequestDTO;
import java.util.List;
import java.util.NoSuchElementException;

public class RoomService {
    private static RoomService instance;
    private final RoomRepository roomRepository;
    private final PensionRepository pensionRepository;

    private RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
        this.pensionRepository = PensionRepository.getInstance();
    }

    public static void initialize(RoomRepository roomRepository) {
        if (instance == null) {
            instance = new RoomService(roomRepository);
        }
    }

    public static RoomService getInstance() {
        if (instance == null) {
            throw new IllegalStateException("RoomService가 초기화되지 않았습니다. Init.initializeDependencies()를 먼저 호출하세요.");
        }
        return instance;
    }

    public Room findById(int id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("ID가 " + id + "인 객실을 찾을 수 없습니다."));
    }

    public Room findByRoomName(String roomName) {
        return roomRepository.findByRoomName(roomName)
                .orElseThrow(() -> new NoSuchElementException("객실명이 " + roomName + "인 객실을 찾을 수 없습니다."));
    }

    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    public List<Room> findByRoomType(RoomType roomType) {
        return roomRepository.findByRoomType(roomType);
    }

    public List<Room> findByRoomStatus(RoomStatus roomStatus) {
        return roomRepository.findByRoomStatus(roomStatus);
    }

    public List<Room> findByPensionId(int pensionId) {
        return roomRepository.findByPensionId(pensionId);
    }

    public Room save(RoomRequestDTO requestDTO) {
        // Pension 조회
        Pension pension = pensionRepository.findById(requestDTO.pensionId())
            .orElseThrow(() -> new IllegalArgumentException("Pension not found: " + requestDTO.pensionId()));
        
        Room newRoom = new Room(
                requestDTO.roomName(),
                requestDTO.floor(),
                requestDTO.building(),
                requestDTO.maxPeople(),
                requestDTO.description(),
                requestDTO.roomStatus(),
                requestDTO.roomType(),
                requestDTO.price(),
                pension,
                requestDTO.image()
        );
        return roomRepository.save(newRoom);
    }

    public void deleteById(int id) {
        findById(id);
        roomRepository.deleteById(id);
    }

    public Room updateRoomStatus(int id, RoomStatus newStatus) {
        Room room = findById(id);
        room.setRoomStatus(newStatus);
        return room;
    }
}
