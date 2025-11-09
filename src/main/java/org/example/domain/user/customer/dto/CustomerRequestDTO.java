package org.example.domain.user.customer.dto;

public record CustomerRequestDTO(String name, String accountId, String password, String phone, String email, int money) {
}
