package org.example.domain.room.strategy;

import org.example.domain.room.Room;
import org.example.domain.room.RoomStatus;
import org.example.domain.room.RoomType;
import java.util.ArrayList;
import java.util.List;

public class DefaultRoomDataStrategy implements RoomInitStrategy {
    @Override
    public List<Room> initializeList() {
        List<Room> list = new ArrayList<>();
        
        Room room1 = new Room();
        room1.setId(1);
        room1.setRoomName("101호");
        room1.setFloor(1);
        room1.setBuilding("A동");
        room1.setMaxPeople(2);
        room1.setDescription("아늑한 싱글룸");
        room1.setRoomStatus(RoomStatus.RESERVATION);
        room1.setRoomType(RoomType.SINGLE);
        
        Room room2 = new Room();
        room2.setId(2);
        room2.setRoomName("201호");
        room2.setFloor(2);
        room2.setBuilding("A동");
        room2.setMaxPeople(4);
        room2.setDescription("넓은 복층 객실");
        room2.setRoomStatus(RoomStatus.RESERVATION);
        room2.setRoomType(RoomType.DUPLEX);
        
        list.add(room1);
        list.add(room2);
        
        return list;
    }
}
