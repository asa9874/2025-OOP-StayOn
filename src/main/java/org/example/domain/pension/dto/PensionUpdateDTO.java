package org.example.domain.pension.dto;

public record PensionUpdateDTO(
    int id,
    String name,
    String address,
    String phoneNumber,
    String description
) {
}
