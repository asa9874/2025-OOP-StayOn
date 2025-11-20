package org.example.domain.reservation;

import org.example.domain.reservation.state.*;
import org.example.domain.room.Room;
import org.example.domain.user.customer.Customer;

public class Reservation {
    private int id;
    private Room room;
    private Customer customer;
    private ReservationState state;

    public Reservation() {
        this.state = new PendingState(); // 기본 상태
    }

    public Reservation(Room room, Customer customer, ReservationState state) {
        this.room = room;
        this.customer = customer;
        this.state = state != null ? state : new PendingState();
    }

    // 결제 로직
    public void pay() {
        state.pay(this);
    }

    // 취소 로직
    public void cancel() {
        state.cancel(this);
    }

    // 환불 로직
    public void refund() {
        state.refund(this);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public ReservationState getState() {
        return state;
    }

    public void setState(ReservationState state) {
        this.state = state;
    }

    public String getStateName() {
        return state.getStateName();
    }

    public ReservationStatus getReservationStatus() {
        return ReservationStatus.valueOf(state.getStateName());
    }

    public void setReservationStatus(ReservationStatus reservationStatus) {
        switch (reservationStatus) {
            case PENDING:
                this.state = new PendingState();
                break;
            case CONFIRMED:
                this.state = new ConfirmedState();
                break;
            case CANCELLED:
                this.state = new CancelledState();
                break;
            case REFUNDED:
                this.state = new RefundedState();
                break;
        }
    }
}
