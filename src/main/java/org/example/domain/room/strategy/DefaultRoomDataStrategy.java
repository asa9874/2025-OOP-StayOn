package org.example.domain.room.strategy;

import org.example.domain.room.Room;
import org.example.domain.room.RoomStatus;
import org.example.domain.room.RoomType;
import org.example.domain.pension.Pension;
import org.example.domain.pension.PensionRepository;
import java.util.ArrayList;
import java.util.List;

public class DefaultRoomDataStrategy implements RoomInitStrategy {
    @Override
    public List<Room> initializeList() {
        List<Room> list = new ArrayList<>();
        
        // Pension 가져오기 (이미 초기화되어 있어야 함)
        PensionRepository pensionRepo = PensionRepository.getInstance();
        List<Pension> pensions = pensionRepo.findAll();
        
        if (pensions.isEmpty()) {
            throw new IllegalStateException("Pension이 먼저 초기화되어야 합니다.");
        }
        
        String[] buildings = {"A", "B", "C"};
        RoomType[] types = {RoomType.SINGLE, RoomType.DUPLEX, RoomType.HOTEL};
        RoomStatus[] statuses = {RoomStatus.RESERVATION, RoomStatus.USING, RoomStatus.CLEANING, RoomStatus.CHECKING};
        int[] prices = {50000, 70000, 90000, 100000, 120000};
        
        for (int i = 1; i <= 25; i++) {
            Room room = new Room();
            room.setId(i);
            room.setRoomName((100 + i) + "호");
            room.setFloor((i - 1) / 5 + 1);
            room.setBuilding(buildings[i % 3]);
            room.setMaxPeople((i % 4) + 2);
            room.setDescription("객실 " + i + "번 - " + types[i % 3].name() + " 타입");
            room.setRoomStatus(statuses[i % 4]);
            room.setRoomType(types[i % 3]);
            room.setPrice(prices[i % 5]);
            
            // Pension 객체로 연관 설정
            Pension pension = pensions.get((i - 1) % pensions.size());
            room.setPension(pension);
            room.setImage("src/main/java/org/example/image/room/room ("+i+").jpeg");
            list.add(room);
        }
        
        return list;
    }
}
