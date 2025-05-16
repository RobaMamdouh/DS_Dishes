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

    public List<SoldDishDTO> getSoldDishesBySeller(Long sellerId) {
        return ExternalOrderClient.fetchSoldDishes(sellerId);
    }

    public List<DishesModel> getDishesBySellerId(Long sellerId) {
        return dishesRepo.getDishesBySellerId(sellerId);
    }

    public void reduceQuantity(List<com.dishes.dishes_service.Models.ReduceDishesDTO> reduceDishesList) {
        for (com.dishes.dishes_service.Models.ReduceDishesDTO dto : reduceDishesList) {
            DishesModel dish = getDishById(dto.getDishId());
            if (dish == null) {
                throw new EntityNotFoundException("Dish with ID " + dto.getDishId() + " not found.");
            }
            if (dish.getQuantity() < dto.getQuantity()) {
                throw new IllegalArgumentException("Not enough stock for dish ID " + dto.getDishId());
            }
            dish.setQuantity(dish.getQuantity() - dto.getQuantity());
            updateDish(dish.getId(), dish);
        }
    }

}
