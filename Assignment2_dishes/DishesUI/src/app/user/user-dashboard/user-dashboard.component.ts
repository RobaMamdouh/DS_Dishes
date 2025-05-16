import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-user-dashboard',
  templateUrl: './user-dashboard.component.html',
  styleUrls: ['./user-dashboard.component.css']
})
export class UserDashboardComponent {
  balance: number | null = null;

  constructor(private authService: AuthService) {
    this.fetchBalance();
  }

  fetchBalance() {
    this.authService.getUserBalance().subscribe({
      next: (res) => {
        // If backend returns {balance: number}
        this.balance = res.balance !== undefined ? res.balance : res;
      },
      error: () => {
        this.balance = null;
      }
    });
  }

  getUsername() {
    return this.authService.getUsername();
  }

  getBalance() {
    return this.balance !== null ? this.balance : 'Loading...';
  }
}
