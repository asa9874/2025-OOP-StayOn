package org.example.domain.review.dto;

import org.example.domain.user.customer.Customer;
import java.time.LocalDate;

public record ReviewRequestDTO(int rate, String content, LocalDate date, Customer customer) {
}
