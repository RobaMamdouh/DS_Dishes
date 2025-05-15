import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {tap} from 'rxjs/operators';

interface LoginResponse {
  userType: string;
  message: string;
  userId: number;
  username: string;
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

  constructor(private http: HttpClient) {
  }

  // Login method
  login(credentials: { username: string, password: string }): Observable<LoginResponse> {
    return this.http.post<LoginResponse>('', credentials).pipe(
      tap(response => {
        if (response.message === 'Login successful') {
          this.userId = response.userId;
          this.userType = response.userType;
          this.username = response.username;
          localStorage.setItem('userId', String(response.userId));
          localStorage.setItem('userType', response.userType);
          localStorage.setItem('username', response.username);
        }
      })
    );
  }

  // Register method
  register(user: { username: string, password: string }): Observable<RegisterResponse> {
    return this.http.post<RegisterResponse>('', user);
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

  logout() {
    this.userId = null;
    localStorage.removeItem('userId');
  }

}
