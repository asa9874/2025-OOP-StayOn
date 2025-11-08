package org.example.domain.reservation;

import org.example.domain.reservation.dto.ReservationRequestDTO;
import org.example.domain.user.customer.Customer;
import org.example.domain.room.Room;

import java.util.List;

public class ReservationController {
    private static ReservationController instance;
    private ReservationService reservationService;

    private ReservationController() {
    }

    public static ReservationController getInstance() {
        if (instance == null) {
            instance = new ReservationController();
        }
        return instance;
    }

    public void setReservationService(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    public Reservation save(ReservationRequestDTO requestDTO) {
        return reservationService.save(requestDTO);
    }

    public Reservation findById(int id) {
        return reservationService.findById(id);
    }

    public List<Reservation> findAll() {
        return reservationService.findAll();
    }

    public List<Reservation> findByCustomer(Customer customer) {
        return reservationService.findByCustomer(customer);
    }

    public List<Reservation> findByRoom(Room room) {
        return reservationService.findByRoom(room);
    }

    public List<Reservation> findByStatus(ReservationStatus status) {
        return reservationService.findByStatus(status);
    }

    public void deleteById(int id) {
        reservationService.deleteById(id);
    }

    // 결제 처리
    public Reservation pay(int reservationId) {
        return reservationService.pay(reservationId);
    }

    // 취소 처리
    public Reservation cancel(int reservationId) {
        return reservationService.cancel(reservationId);
    }

    // 환불 처리
    public Reservation refund(int reservationId) {
        return reservationService.refund(reservationId);
    }
}
