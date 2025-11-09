package org.example.domain.reservation;

import org.example.domain.reservation.dto.ReservationRequestDTO;
import org.example.domain.user.customer.Customer;
import org.example.domain.room.Room;

import java.util.List;
import java.util.NoSuchElementException;

public class ReservationService {
    private static ReservationService instance;
    private ReservationRepository reservationRepository;

    private ReservationService() {
    }

    public static ReservationService getInstance() {
        if (instance == null) {
            instance = new ReservationService();
        }
        return instance;
    }

    public void setReservationRepository(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Reservation save(ReservationRequestDTO requestDTO) {
        Reservation newReservation = new Reservation(
                requestDTO.room(),
                requestDTO.customer(),
                null
        );
        // ReservationStatus를 State로 변환
        newReservation.setReservationStatus(requestDTO.reservationStatus());
        return reservationRepository.save(newReservation);
    }

    public Reservation findById(int id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 예약을 찾을 수 없습니다: " + id));
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public List<Reservation> findByCustomer(Customer customer) {
        return reservationRepository.findByCustomer(customer);
    }

    public List<Reservation> findByRoom(Room room) {
        return reservationRepository.findByRoom(room);
    }

    public List<Reservation> findByStatus(ReservationStatus status) {
        return reservationRepository.findByStatus(status);
    }

    public void deleteById(int id) {
        Reservation reservation = findById(id);
        reservationRepository.deleteById(reservation.getId());
    }

    // 결제 처리
    public Reservation pay(int reservationId) {
        Reservation reservation = findById(reservationId);
        reservation.pay();
        return reservation;
    }

    // 취소 처리
    public Reservation cancel(int reservationId) {
        Reservation reservation = findById(reservationId);
        reservation.cancel();
        return reservation;
    }

    // 환불 처리
    public Reservation refund(int reservationId) {
        Reservation reservation = findById(reservationId);
        reservation.refund();
        return reservation;
    }
}
