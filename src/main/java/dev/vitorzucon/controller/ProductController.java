package dev.vitorzucon.controller;

import dev.vitorzucon.DTOs.ProductRequestDTO;
import dev.vitorzucon.DTOs.ProductResponseDTO;
import dev.vitorzucon.DTOs.SupplyItemResponseDTO;
import dev.vitorzucon.entity.ProductEntity;
import dev.vitorzucon.service.ProductService;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @POST
    @Transactional
    public Response create(ProductRequestDTO request) {
        return Response.ok(ProductEntitytoDTO(productService.save(request))).build();
    }

    @GET
    public Response findAll(@QueryParam("page") @DefaultValue("0") Integer page,
                            @QueryParam("linesPerPage") @DefaultValue("10") Integer linesPerPage) {
        List<ProductResponseDTO> response = productService.findAll(page,linesPerPage)
                .stream()
                .map(this::ProductEntitytoDTO)
                .toList();

        return Response.ok(response).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        return Response.ok(ProductEntitytoDTO(productService.findById(id))).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, ProductRequestDTO request) {
        return Response.ok(ProductEntitytoDTO(productService.update(id, request))).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteById(@PathParam("id") Long id) {
        productService.delete(id);
        return Response.notModified().build();
    }

    private ProductResponseDTO ProductEntitytoDTO(ProductEntity product) {

        List<SupplyItemResponseDTO> supplyItems = product.supplyItems
                .stream()
                .map(item -> new SupplyItemResponseDTO(
                        item.getSupply().getName(),
                        item.getQuantity()
                ))
                .toList();

        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getPrice(),
                supplyItems
        );
    }
}
