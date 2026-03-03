package dev.vitorzucon.service;

import dev.vitorzucon.entity.SupplyEntity;
import dev.vitorzucon.repository.SupplyRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

@ApplicationScoped
public class SupplyService {

    private final SupplyRepository supplyRepository;

    public SupplyService(SupplyRepository supplyRepository) {
        this.supplyRepository = supplyRepository;
    }

    public SupplyEntity save(SupplyEntity supplyEntity) {
        supplyRepository.persist(supplyEntity);
        return supplyEntity;
    }

    public List<SupplyEntity> findAll(Integer page, Integer linesPerPage) {
        return supplyRepository.find("active", true)
                .page(page, linesPerPage)
                .list();
    }

    public SupplyEntity findById(Long id) {
        return supplyRepository.findByIdOptional(id).orElseThrow(() -> new NotFoundException("Supply not found"));
    }

    public SupplyEntity update(Long id,SupplyEntity supplyEntity) {
        var supply = findById(id);
        supply.setName(supplyEntity.getName());
        supply.setStock(supplyEntity.getStock());
        supplyRepository.persist(supply);
        return supply;
    }

    public SupplyEntity delete(Long id) {
        var supply = findById(id);
        supply.setActive(false);
        supplyRepository.persist(supply);
        return supply;
    }
}
