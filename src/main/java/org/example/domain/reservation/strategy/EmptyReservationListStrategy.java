package org.example.domain.reservation.strategy;

import org.example.domain.reservation.Reservation;
import java.util.ArrayList;
import java.util.List;

public class EmptyReservationListStrategy implements ReservationInitStrategy {
    @Override
    public List<Reservation> initializeList() {
        return new ArrayList<>();
    }
}
