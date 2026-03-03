package dev.vitorzucon.DTOs;

import java.util.List;

public record ProductResponseDTO(
        Long id,
        String name,
        Double price,
        List<SupplyItemResponseDTO> supplyItems
) {
}
