package org.example.domain.room.strategy;

import org.example.domain.room.Room;
import java.util.List;

public interface RoomInitStrategy {
    List<Room> initializeList();
}
