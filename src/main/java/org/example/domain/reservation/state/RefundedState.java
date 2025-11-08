package org.example.domain.reservation.state;

import org.example.domain.reservation.Reservation;

public class RefundedState implements ReservationState {
    @Override
    public void pay(Reservation reservation) {
        throw new IllegalStateException("환불된 예약은 결제할 수 없습니다.");
    }

    @Override
    public void cancel(Reservation reservation) {
        throw new IllegalStateException("이미 환불된 예약입니다.");
    }

    @Override
    public void refund(Reservation reservation) {
        throw new IllegalStateException("이미 환불된 예약입니다.");
    }

    @Override
    public String getStateName() {
        return "REFUNDED";
    }
}
