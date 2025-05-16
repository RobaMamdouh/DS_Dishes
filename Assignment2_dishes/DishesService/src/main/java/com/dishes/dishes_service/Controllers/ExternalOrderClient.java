package com.dishes.dishes_service.Controllers;

import com.dishes.dishes_service.Models.SoldDishDTO;
import jakarta.ejb.Stateless;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Stateless
public class ExternalOrderClient {

    public static List<SoldDishDTO> fetchSoldDishes(Long sellerId) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client
                .target("http://localhost:8083/api/orders/sold-dishes")
                .queryParam("sellerId", sellerId);

        GenericType<List<SoldDishDTO>> listType = new GenericType<>() {};
        return target.request(MediaType.APPLICATION_JSON).get(listType);
    }
}
