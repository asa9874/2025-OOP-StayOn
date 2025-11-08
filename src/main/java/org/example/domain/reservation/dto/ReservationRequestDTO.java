package org.example.domain.reservation.dto;

import org.example.domain.reservation.ReservationStatus;
import org.example.domain.room.Room;
import org.example.domain.user.customer.Customer;

public record ReservationRequestDTO(Room room, Customer customer, ReservationStatus reservationStatus) {
}
