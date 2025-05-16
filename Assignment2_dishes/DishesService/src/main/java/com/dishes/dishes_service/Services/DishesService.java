package com.dishes.dishes_service.Services;

import com.dishes.dishes_service.Controllers.ExternalOrderClient;
import com.dishes.dishes_service.Models.DishesModel;
import com.dishes.dishes_service.Models.SoldDishDTO;
import com.dishes.dishes_service.Repo.DishesRepo;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

@Stateless
public class DishesService {

    @Inject
    private DishesRepo dishesRepo;

    public List<DishesModel> getAllDishes() {
        return dishesRepo.getAllDishes();
    }

    public DishesModel getDishById(Long id) {
        return dishesRepo.getDishById(id);
    }

    public void addDish(DishesModel dish) {
        dishesRepo.addDish(dish);
    }

    public void updateDish(Long id, DishesModel updatedDish) {
        if (updatedDish == null) {
            throw new IllegalArgumentException("Updated dish cannot be null.");
        }

        DishesModel existingDish = dishesRepo.findById(id);
        if (existingDish != null) {
            existingDish.setName(updatedDish.getName());
            existingDish.setPrice(updatedDish.getPrice());
            existingDish.setQuantity(updatedDish.getQuantity());
        } else {
            throw new EntityNotFoundException("Dish with ID " + id + " not found.");
        }
    }

    public List<DishesModel> getAvailableDishes() {
        return dishesRepo.getAvailableDishes();
    }

    public List<SoldDishDTO> getSoldDishesWithUsers() {
        return ExternalOrderClient.fetchSoldDishes();
    }
}
