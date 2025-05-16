import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SellerService {

  constructor(private http: HttpClient) { }

  private readonly API_BASE = 'http://localhost:8081/DishesService-1.0-SNAPSHOT/api/dishes'; 

  getDishesBySellerId(sellerId: number): Observable<any[]> {
    const url = `${this.API_BASE}/seller/${sellerId}`;
    return this.http.get<any[]>(url);
  }

   createDish(dish: any): Observable<any> {
    return this.http.post(`${this.API_BASE}`, dish);
  }

  updateDish(dishId: number, dish: any): Observable<any> {
    return this.http.put(`${this.API_BASE}/${dishId}`, dish);
  }

  getDishById(dishId: number): Observable<any> {
    return this.http.get(`${this.API_BASE}/${dishId}`);
  }

  getSoldDishesBySellerId(sellerId: number) {
    return this.http.get<any[]>(`${this.API_BASE}/sold-with-users/${sellerId}`);
  }

}
