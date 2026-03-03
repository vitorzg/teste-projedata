package dev.vitorzucon.DTOs;

public record ProductMaxProductionDTO(
        Long id,
        String name,
        Double price,
        Integer maxProduction
) {
}
