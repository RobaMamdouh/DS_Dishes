package com.example.dishesservice.Controllers;


import com.example.dishesservice.Models.DishesModel;
import com.example.dishesservice.Services.DishesService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.util.List;

@Path("/dishes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DishesController {

    @Inject
    private DishesService dishesService;

    @GET
    public List<DishesModel> getAllDishes() {
        return dishesService.getAllDishes();
    }

    @GET
    @Path("/sold")
    public List<DishesModel> getSoldDishes() {
        return dishesService.getSoldDishes();
    }

    @POST
    public Response addDish(DishesModel dish) {
        dishesService.addDish(dish);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateDish(@PathParam("id") Long id, DishesModel dish) {
        dishesService.updateDish(id, dish);
        return Response.ok().build();
    }
}
