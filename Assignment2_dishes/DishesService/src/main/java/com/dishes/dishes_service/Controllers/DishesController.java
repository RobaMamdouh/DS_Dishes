package com.dishes.dishes_service.Controllers;

import com.dishes.dishes_service.Models.DishesModel;
import com.dishes.dishes_service.Services.DishesService;
import com.dishes.dishes_service.Models.SoldDishDTO;
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
    @Path("/available")
    public List<DishesModel> getAvailableDishes() {
        return dishesService.getAvailableDishes();
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

    @GET
    @Path("/{id}")
    public DishesModel getDishById(@PathParam("id") Long id) {
        return dishesService.getDishById(id);
    }

    @GET
    @Path("/sold-with-users")
    public List<SoldDishDTO> getSoldDishesWithUsers() {
        return dishesService.getSoldDishesWithUsers();
    }
}
