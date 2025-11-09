package org.example.domain.reservation.state;

import org.example.domain.reservation.Reservation;

public class ConfirmedState implements ReservationState {
    @Override
    public void pay(Reservation reservation) {
        throw new IllegalStateException("이미 결제가 완료된 예약입니다.");
    }

    @Override
    public void cancel(Reservation reservation) {
        reservation.setState(new CancelledState());
    }

    @Override
    public void refund(Reservation reservation) {
        int roomPrice = reservation.getRoom().getPrice();
        reservation.getCustomer().addMoney(roomPrice);
        reservation.setState(new RefundedState());
    }

    @Override
    public String getStateName() {
        return "CONFIRMED";
    }
}
