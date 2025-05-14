package com.example.dishesservice.Services;

import com.example.dishesservice.Models.DishesModel;
import com.example.dishesservice.Repo.DishesRepo;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;

@Stateless
public class DishesService {

    @Inject
    private DishesRepo dishesRepo;

    public List<DishesModel> getAllDishes() {
        return dishesRepo.getAllDishes();
    }

    public List<DishesModel> getSoldDishes() {
        return dishesRepo.getSoldDishes();
    }

    public void addDish(DishesModel dish) {
        dishesRepo.addDish(dish);
    }

    public void updateDish(Long id, DishesModel updatedDish) {
        DishesModel existing = dishesRepo.findById(id);
        if (existing != null) {
            existing.setName(updatedDish.getName());
            existing.setPrice(updatedDish.getPrice());
            existing.setQuantity(updatedDish.getQuantity());
            dishesRepo.updateDish(existing);
        }
    }
}
