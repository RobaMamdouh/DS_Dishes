import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface User {
  id: number;
  username: string;
  password: string;
  role: string;
  balance: number;
}

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  private apiUrl = 'http://localhost:8082/api/users';

  constructor(private http: HttpClient) {}

  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiUrl}/allUsers`);
  }

  getAllCompanies(): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiUrl}/allCompanies`);
  }
  
  createCompanyAccount(formData: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/createCompany`, formData);
  }
}
