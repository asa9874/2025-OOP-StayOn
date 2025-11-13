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
        Customer[] customers = new Customer[25];
        String[] names = {"홍길동", "김철수", "이영희", "박민수", "최수지",
                         "정준호", "강지은", "조성민", "윤하늘", "장서연",
                         "임도윤", "한예준", "오시우", "서하준", "신서준",
                         "권지호", "황수빈", "안은우", "송유진", "류지우",
                         "전현우", "홍민지", "고태양", "문소율", "양채원"};
        
        for (int i = 0; i < 25; i++) {
            customers[i] = new Customer(
                names[i],
                "customer" + (i + 1),
                "password" + (i + 1),
                "010-" + (1000 + i * 100) + "-" + (2000 + i * 100),
                "customer" + (i + 1) + "@example.com",
                100000
            );
            customers[i].setId(i + 1);
        }

        // 테스트용 Room 생성
        Room[] rooms = new Room[25];
        RoomType[] types = {RoomType.SINGLE, RoomType.DUPLEX, RoomType.HOTEL};
        RoomStatus[] statuses = {RoomStatus.RESERVATION, RoomStatus.RESERVATIONED, RoomStatus.USING};
        
        for (int i = 0; i < 25; i++) {
            rooms[i] = new Room();
            rooms[i].setId(i + 1);
            rooms[i].setRoomName((101 + i) + "호");
            rooms[i].setFloor((i / 5) + 1);
            rooms[i].setBuilding(((char)('A' + (i % 3))) + "동");
            rooms[i].setMaxPeople((i % 4) + 2);
            rooms[i].setDescription("객실 " + (i + 1) + "번");
            rooms[i].setRoomStatus(statuses[i % 3]);
            rooms[i].setRoomType(types[i % 3]);
            rooms[i].setPrice(50000 + (i * 10000));
        }

        // Reservation 생성
        for (int i = 1; i <= 25; i++) {
            Reservation reservation = new Reservation();
            reservation.setId(i);
            reservation.setRoom(rooms[i - 1]);
            reservation.setCustomer(customers[i - 1]);
            
            // 다양한 상태 분포
            if (i % 4 == 0) {
                reservation.setState(new PendingState());
            } else if (i % 4 == 1) {
                reservation.setState(new ConfirmedState());
            } else if (i % 4 == 2) {
                reservation.setState(new org.example.domain.reservation.state.CancelledState());
            } else {
                reservation.setState(new org.example.domain.reservation.state.RefundedState());
            }
            
            list.add(reservation);
        }

        return list;
    }
}
