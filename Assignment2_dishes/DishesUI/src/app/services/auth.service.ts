import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

interface LoginResponse {
  message: string;
  role: string;    
}

interface RegisterResponse {
  message: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
 private userId: number | null = null;
  private userType: string | null = null;
  private username: string | null = null;
  private balance: number | null = null;

  private readonly API_BASE = 'http://localhost:8082/api/users'; 

  constructor(private http: HttpClient) {}


login(credentials: any): Observable<any> {
  const params = new HttpParams()
    .set('username', credentials.username)
    .set('password', credentials.password);
  return this.http.post(`${this.API_BASE}/login`, null, { params }).pipe(
    tap((response: any) => {
      let userType = '';
      if (response.message && response.message.includes('admin')) {
        userType = 'admin';
      } else if (response.message && response.message.includes('company')) {
        userType = 'company';
      } else if (response.message && response.message.includes('user')) {
        userType = 'user';
      }
      localStorage.setItem('userType', userType);
      this.userType = userType;
      localStorage.setItem('username', credentials.username);

      this.http.get<any>(`${this.API_BASE}/getUserByUsername`, {
        params: new HttpParams().set('username', credentials.username)
      }).subscribe(user => {
        if (user && user.id !== undefined && user.id !== null) {
          localStorage.setItem('userId', user.id.toString());
          this.userId = user.id;
        } else {
          console.error('User ID not found in response:', user);
        }
      });
    })
  );
}

  register(user: { username: string; password: string; balance: number; role: string }): Observable<RegisterResponse> {
  return this.http.post<RegisterResponse>(`${this.API_BASE}/register`, user);
}

  createCompany(username: string): Observable<{ message: string }> {
    const params = new HttpParams().set('username', username);
    return this.http.post<{ message: string }>(`${this.API_BASE}/createCompany`, null, { params });
  }

  getUserId(): number | null {
    if (this.userId === null) {
      const storedUserId = localStorage.getItem('userId');
      this.userId = storedUserId ? Number(storedUserId) : null;
    }
    return this.userId;
  }

  getUsername(): string | null {
    return this.username || localStorage.getItem('username');
  }

  getUserType(): string | null {
    return this.userType || localStorage.getItem('userType');
  }

  getUserBalance(): Observable<any> {
    const username = this.username || localStorage.getItem('username');
    if (!username) {
      throw new Error('Username not found');
    }
    return this.http.get<any>(`${this.API_BASE}/getUserByUsername`, {
      params: new HttpParams().set('username', username)
    }).pipe(
      tap(user => {
        if (user && user.balance !== undefined) {
          this.balance = user.balance;
        }
      })
    );
  }



}


