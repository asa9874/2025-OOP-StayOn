package org.example.domain.reservation.state;

import org.example.domain.reservation.Reservation;

public class PendingState implements ReservationState {
    @Override
    public void pay(Reservation reservation) {
        int roomPrice = reservation.getRoom().getPrice();
        if (reservation.getCustomer().getMoney() < roomPrice) {
            throw new IllegalArgumentException("잔액이 부족합니다.");
        }
        
        reservation.getCustomer().subtractMoney(roomPrice);
        reservation.setState(new ConfirmedState());
    }

    @Override
    public void cancel(Reservation reservation) {
        reservation.setState(new CancelledState());
    }

    @Override
    public void refund(Reservation reservation) {
        throw new IllegalStateException("결제 전 예약은 환불할 수 없습니다. 취소를 사용하세요.");
    }

    @Override
    public String getStateName() {
        return "PENDING";
    }
}
