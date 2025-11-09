package org.example.domain.reservation.strategy;

import org.example.domain.reservation.Reservation;
import java.util.List;

public interface ReservationInitStrategy {
    List<Reservation> initializeList();
}
