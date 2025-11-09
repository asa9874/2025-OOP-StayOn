package org.example.domain.reservation.state;

import org.example.domain.reservation.Reservation;

public interface ReservationState {
    void pay(Reservation reservation);
    void cancel(Reservation reservation);
    void refund(Reservation reservation);
    String getStateName();
}
