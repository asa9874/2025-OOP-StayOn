package org.example.domain.review.dto;

import org.example.domain.user.customer.Customer;
import org.example.domain.room.Room;
import java.time.LocalDate;

public record ReviewRequestDTO(int rate, String content, LocalDate date, Customer customer, Room room) {
}
