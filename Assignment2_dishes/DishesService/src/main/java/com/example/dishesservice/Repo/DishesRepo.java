package com.example.dishesservice.Repo;

import com.example.dishesservice.Models.DishesModel;
import jakarta.ejb.Singleton;
import jakarta.persistence.*;

import java.util.List;

@Singleton
public class DishesRepo {

    @PersistenceContext(unitName = "dishesPU")
    private EntityManager em;

    public List<DishesModel> getAllDishes() {
        return em.createQuery("SELECT d FROM DishesModel d WHERE d.sold = false", DishesModel.class).getResultList();
    }

    public List<DishesModel> getSoldDishes() {
        return em.createQuery("SELECT d FROM DishesModel d WHERE d.sold = true", DishesModel.class).getResultList();
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
