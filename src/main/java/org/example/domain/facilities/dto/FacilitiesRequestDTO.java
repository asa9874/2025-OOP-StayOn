package org.example.domain.facilities.dto;

import java.time.LocalDateTime;

public record FacilitiesRequestDTO(
    String name,
    LocalDateTime openingTime,
    LocalDateTime closingTime,
    boolean requireReservation,
    int pensionId,
    String image
) {
}
