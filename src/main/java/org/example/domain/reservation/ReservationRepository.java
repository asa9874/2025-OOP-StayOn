package org.example.domain.reservation;

import org.example.domain.reservation.strategy.ReservationInitStrategy;
import org.example.domain.user.customer.Customer;
import org.example.domain.room.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReservationRepository {
    private static ReservationRepository instance;
    private List<Reservation> reservationList;
    private int nextId = 1;
    private ReservationInitStrategy initStrategy;

    private ReservationRepository() {
    }

    public static ReservationRepository getInstance() {
        if (instance == null) {
            instance = new ReservationRepository();
        }
        return instance;
    }

    public void setInitStrategy(ReservationInitStrategy initStrategy) {
        this.initStrategy = initStrategy;
        this.reservationList = initStrategy.initializeList();
        if (!reservationList.isEmpty()) {
            this.nextId = reservationList.stream()
                    .mapToInt(Reservation::getId)
                    .max()
                    .orElse(0) + 1;
        }
    }

    public void returnToDefaultData() {
        if (initStrategy != null) {
            this.reservationList = initStrategy.initializeList();
            if (!reservationList.isEmpty()) {
                this.nextId = reservationList.stream()
                        .mapToInt(Reservation::getId)
                        .max()
                        .orElse(0) + 1;
            } else {
                this.nextId = 1;
            }
        }
    }

    public Reservation save(Reservation reservation) {
        reservation.setId(nextId++);
        reservationList.add(reservation);
        return reservation;
    }

    public Optional<Reservation> findById(int id) {
        return reservationList.stream()
                .filter(reservation -> reservation.getId() == id)
                .findFirst();
    }

    public List<Reservation> findAll() {
        return new ArrayList<>(reservationList);
    }
    public List<Reservation> findByCustomer(Customer customer) {
        return reservationList.stream()
                .filter(reservation -> reservation.getCustomer() != null && 
                        reservation.getCustomer().getId() == customer.getId())
                .toList();
    }

    public List<Reservation> findByRoom(Room room) {
        return reservationList.stream()
                .filter(reservation -> reservation.getRoom() != null && 
                        reservation.getRoom().getId() == room.getId())
                .toList();
    }

    public List<Reservation> findByStatus(ReservationStatus status) {
        return reservationList.stream()
                .filter(reservation -> reservation.getReservationStatus() == status)
                .toList();
    }

    public void deleteById(int id) {
        reservationList.removeIf(reservation -> reservation.getId() == id);
    }
}
