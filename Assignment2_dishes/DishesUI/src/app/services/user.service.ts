import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  private readonly Dish_API_BASE = 'http://localhost:8081/DishesService-1.0-SNAPSHOT/api/dishes'; 
  private readonly Order_API_BASE = 'http://localhost:8083/api/orders';

 getAvailableDishes(): Observable<any[]> {
    return this.http.get<any[]>(`${this.Dish_API_BASE}/available`);
  }

  getDishById(dishId: number): Observable<any> {
    return this.http.get<any>(`${this.Dish_API_BASE}/${dishId}`);
  }

  createOrder(userId: number, dishIdQuantityMap: { [key: number]: number }): Observable<any> {
    const payload = {
      userId: userId,
      dishIdQuantityMap: dishIdQuantityMap
    };
    return this.http.post(`${this.Order_API_BASE}/createOrder`, payload);
  }

  getOrdersByUserId(userId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.Order_API_BASE}/user/${userId}`);
  }

  

}
