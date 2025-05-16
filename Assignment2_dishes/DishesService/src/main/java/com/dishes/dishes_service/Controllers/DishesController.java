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
    @Path("/sold-with-users/{sellerId}")
    public List<SoldDishDTO> getSoldDishesBySeller(@PathParam("sellerId") Long sellerId) {
        return dishesService.getSoldDishesBySeller(sellerId);
    }

    @GET
    @Path("/seller/{sellerId}")
    public List<DishesModel> getDishesBySeller(@PathParam("sellerId") Long sellerId) {
        return dishesService.getDishesBySellerId(sellerId);
    }

    @POST
    @Path("/reduceQuantity")
    public Response reduceQuantity(List<com.dishes.dishes_service.Models.ReduceDishesDTO> reduceDishesList) {
        try {
            for (com.dishes.dishes_service.Models.ReduceDishesDTO dto : reduceDishesList) {
                DishesModel dish = dishesService.getDishById(dto.getDishId());
                if (dish == null) {
                    return Response.status(Response.Status.NOT_FOUND)
                        .entity("Dish with ID " + dto.getDishId() + " not found.").build();
                }
                if (dish.getQuantity() < dto.getQuantity()) {
                    return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Not enough stock for dish ID " + dto.getDishId()).build();
                }
                dish.setQuantity(dish.getQuantity() - dto.getQuantity());
                dishesService.updateDish(dish.getId(), dish);
            }
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error reducing dish quantities: " + e.getMessage()).build();
        }
    }

}
