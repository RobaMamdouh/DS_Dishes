package com.dishes.dishes_service.Repo;

import com.dishes.dishes_service.Models.DishesModel;
import jakarta.ejb.Singleton;
import jakarta.persistence.*;

import java.util.List;

@Singleton
public class DishesRepo {

    @PersistenceContext(unitName = "dishesPU")
    private EntityManager em;

    public List<DishesModel> getAllDishes() {
        return em.createQuery("SELECT d FROM DishesModel d ", DishesModel.class).getResultList();
    }

    public List<DishesModel> getAvailableDishes() {
        return em.createQuery("SELECT d FROM DishesModel d WHERE d.quantity > 0", DishesModel.class).getResultList();
    }

    public DishesModel getDishById(Long id) {
        return em.find(DishesModel.class, id);
    }

    public void addDish(DishesModel dish) {
        em.persist(dish);
    }

    public void updateDish(DishesModel dish) {
        em.merge(dish);
    }

    public DishesModel findById(Long id) {
        return em.find(DishesModel.class, id);
    }
}
