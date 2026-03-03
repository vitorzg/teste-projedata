package dev.vitorzucon.service;

import dev.vitorzucon.DTOs.ProductRequestDTO;
import dev.vitorzucon.DTOs.SupplyItemRequestDTO;
import dev.vitorzucon.entity.ProductEntity;
import dev.vitorzucon.entity.ProductSupplyItemEntity;
import dev.vitorzucon.entity.SupplyEntity;
import dev.vitorzucon.repository.ProductRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProductService {

    private final ProductRepository productRepository;
    private final SupplyService supplyService;

    public ProductService(ProductRepository productRepository, SupplyService supplyService) {
        this.productRepository = productRepository;
        this.supplyService = supplyService;
    }

    public ProductEntity save(ProductRequestDTO request) {

        if (request.supplyItems() == null || request.supplyItems().isEmpty()) {
            throw new BadRequestException("supplyItems is null or empty");
        }

        ProductEntity product = new ProductEntity();
        product.setName(request.name());
        product.setPrice(request.price());

        for (SupplyItemRequestDTO  item : request.supplyItems()) {
            SupplyEntity supply = supplyService.findById(item.supplyId());


            ProductSupplyItemEntity itemEntity = new ProductSupplyItemEntity();
            itemEntity.setProduct(product);
            itemEntity.setSupply(supply);
            itemEntity.setQuantity(item.quantity());

            product.supplyItems.add(itemEntity);
        }

        productRepository.persist(product);

        return product;
    }

    public List<ProductEntity> findAll(Integer page, Integer linesPerPage) {
        return productRepository.find("active", true)
                .page(page,linesPerPage)
                .list();
    }

    public ProductEntity findById(Long id) {
        return productRepository.findByIdOptional(id).orElseThrow(() -> new NotFoundException("product not found"));
    }

    public ProductEntity update(Long id, ProductRequestDTO request) {

        ProductEntity product = findById(id);

        product.setName(request.name());
        product.setPrice(request.price());

        Map<Long, ProductSupplyItemEntity> currentItems = product.supplyItems
                .stream()
                .collect(Collectors.toMap(
                        item -> item.getSupply().getId(),
                        item -> item
                ));

        Set<Long> receivedSupplyIds = new HashSet<>();

        for (SupplyItemRequestDTO dtoItem : request.supplyItems()) {

            SupplyEntity supply = supplyService.findById(dtoItem.supplyId());

            receivedSupplyIds.add(dtoItem.supplyId());

            if (currentItems.containsKey(dtoItem.supplyId())) {

                ProductSupplyItemEntity existingItem =
                        currentItems.get(dtoItem.supplyId());

                existingItem.setQuantity(dtoItem.quantity());

            } else {
                ProductSupplyItemEntity newItem = new ProductSupplyItemEntity();
                newItem.setProduct(product);
                newItem.setSupply(supply);
                newItem.setQuantity(dtoItem.quantity());

                product.supplyItems.add(newItem);
            }
        }
        product.supplyItems.removeIf(item ->
                !receivedSupplyIds.contains(item.getSupply().getId())
        );

        return product;
    }

    public void delete(Long id) {

        ProductEntity product = findById(id);
        product.setActive(false);
    }
}
