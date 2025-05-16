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
    const stringKeyMap: { [key: string]: number } = {};
    Object.keys(dishIdQuantityMap).forEach((k: string) => {
      stringKeyMap[k] = (dishIdQuantityMap as any)[k];
    });
    const payload = {
      userId: userId,
      dishIdQuantityMap: stringKeyMap
    };
    return this.http.post(`${this.Order_API_BASE}/createOrder`, payload);
  }

  getAllPastOrders(userId: number): Observable<string> {
    return this.http.get(`${this.Order_API_BASE}/allPastOrders?userId=${userId}`, { responseType: 'text' });
  }

  getOrderById(orderId: number): Observable<any> {
    return this.http.get<any>(`${this.Order_API_BASE}/order/${orderId}`);
  }

}
