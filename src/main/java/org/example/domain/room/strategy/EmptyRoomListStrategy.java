package org.example.domain.room.strategy;

import org.example.domain.room.Room;
import java.util.ArrayList;
import java.util.List;

public class EmptyRoomListStrategy implements RoomInitStrategy {
    @Override
    public List<Room> initializeList() {
        return new ArrayList<>();
    }
}
