package org.example.domain.review;

import org.example.domain.user.customer.Customer;
import org.example.domain.room.Room;
import java.time.LocalDate;

public class Review {
    private int id;
    private int rate;
    private String content;
    private LocalDate date;
    private Customer customer;
    private Room room;

    public Review() {
    }

    public Review(int rate, String content, LocalDate date, Customer customer, Room room) {
        validateRate(rate);
        this.rate = rate;
        this.content = content;
        this.date = date;
        this.customer = customer;
        this.room = room;
    }

    private void validateRate(int rate) {
        if (rate < 1 || rate > 5) {
            throw new IllegalArgumentException("평점은 1~5 사이의 값이어야 합니다.");
        }
    }

    public void updateReview(int rate, String content) {
        validateRate(rate);
        this.rate = rate;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        validateRate(rate);
        this.rate = rate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
