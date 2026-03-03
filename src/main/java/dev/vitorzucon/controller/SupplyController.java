package dev.vitorzucon.controller;

import dev.vitorzucon.entity.SupplyEntity;
import dev.vitorzucon.service.SupplyService;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/supplies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SupplyController {

    private final SupplyService supplyService;

    public SupplyController(SupplyService supplyService) {
        this.supplyService = supplyService;
    }

    @POST
    @Transactional
    public Response saveSupply(SupplyEntity supplyEntity) {
        return Response.ok(supplyService.save(supplyEntity)).build();
    }

    @GET
    public Response getSupplies(@QueryParam("page") @DefaultValue("0") Integer page,
                                @QueryParam("linesPerPage") @DefaultValue("10") Integer linesPerPage) {
        return Response.ok(supplyService.findAll(page, linesPerPage)).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        return Response.ok(supplyService.findById(id)).build();
    }

    @DELETE
    @Transactional
    @Path("{id}")
    public Response deleteSupply(@PathParam("id")Long id) {
        supplyService.delete(id);
        return Response.noContent().build();
    }

    @PUT
    @Transactional
    @Path("{id}")
    public Response deleteSupply(@PathParam("id")Long id, SupplyEntity supplyEntity) {
        return Response.ok(supplyService.update(id, supplyEntity)).build();
    }

}
