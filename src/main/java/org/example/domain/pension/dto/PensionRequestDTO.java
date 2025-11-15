package org.example.domain.pension.dto;

public record PensionRequestDTO(
    String name,
    String address,
    String phoneNumber,
    String description,
    int pensionManagerId,
    String image
) {
}
