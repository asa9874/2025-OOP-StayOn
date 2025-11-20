package org.example.domain.reservation.strategy;

import org.example.domain.reservation.Reservation;
import org.example.domain.reservation.state.PendingState;
import org.example.domain.reservation.state.ConfirmedState;
import org.example.domain.room.Room;
import org.example.domain.room.RoomRepository;
import org.example.domain.user.customer.Customer;
import org.example.domain.user.customer.CustomerRepository;

import java.util.ArrayList;
import java.util.List;

public class DefaultReservationDataStrategy implements ReservationInitStrategy {
    @Override
    public List<Reservation> initializeList() {
        List<Reservation> list = new ArrayList<>();

        // Customer와 Room 가져오기 (이미 초기화되어 있어야 함)
        CustomerRepository customerRepo = CustomerRepository.getInstance();
        RoomRepository roomRepo = RoomRepository.getInstance();
        
        List<Customer> customers = customerRepo.findAll();
        List<Room> rooms = roomRepo.findAll();
        
        if (customers.isEmpty() || rooms.isEmpty()) {
            throw new IllegalStateException("Customer와 Room이 먼저 초기화되어야 합니다.");
        }

        // Reservation 생성 - 고객과 객실을 순환하며 연결
        for (int i = 1; i <= 25; i++) {
            Reservation reservation = new Reservation();
            reservation.setId(i);
            
            // 실제 존재하는 Customer와 Room 객체 연결
            Customer customer = customers.get((i - 1) % customers.size());
            Room room = rooms.get((i - 1) % rooms.size());
            
            reservation.setCustomer(customer);
            reservation.setRoom(room);
            
            // 상태
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
