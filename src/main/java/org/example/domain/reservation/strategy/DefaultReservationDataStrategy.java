package org.example.domain.reservation.strategy;

import org.example.domain.reservation.Reservation;
import org.example.domain.reservation.state.PendingState;
import org.example.domain.reservation.state.ConfirmedState;
import org.example.domain.room.Room;
import org.example.domain.room.RoomStatus;
import org.example.domain.room.RoomType;
import org.example.domain.user.customer.Customer;

import java.util.ArrayList;
import java.util.List;

public class DefaultReservationDataStrategy implements ReservationInitStrategy {
    @Override
    public List<Reservation> initializeList() {
        List<Reservation> list = new ArrayList<>();

        // 테스트용 Customer 생성
        Customer customer1 = new Customer("홍길동", "customer1", "password1", "010-1234-5678", "hong@example.com", 100000);
        customer1.setId(1);

        // 테스트용 Room 생성
        Room room1 = new Room();
        room1.setId(1);
        room1.setRoomName("101호");
        room1.setFloor(1);
        room1.setBuilding("A동");
        room1.setMaxPeople(2);
        room1.setDescription("아늑한 싱글룸");
        room1.setRoomStatus(RoomStatus.RESERVATION);
        room1.setRoomType(RoomType.SINGLE);
        room1.setPrice(50000);

        // Reservation 생성
        Reservation reservation1 = new Reservation();
        reservation1.setId(1);
        reservation1.setRoom(room1);
        reservation1.setCustomer(customer1);
        reservation1.setState(new PendingState());

        Reservation reservation2 = new Reservation();
        reservation2.setId(2);
        reservation2.setRoom(room1);
        reservation2.setCustomer(customer1);
        reservation2.setState(new ConfirmedState());

        list.add(reservation1);
        list.add(reservation2);

        return list;
    }
}
