package dev.vitorzucon.DTOs;

import java.util.List;

public record ProductRequestDTO(
        String name,
        Double price,
        List<SupplyItemRequestDTO> supplyItems
) {
}
