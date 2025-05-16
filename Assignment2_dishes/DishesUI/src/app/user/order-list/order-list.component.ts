import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-order-list',
  templateUrl: './order-list.component.html',
  styleUrls: ['./order-list.component.css']
})
export class OrderListComponent implements OnInit {
  orders: any[] = [];
  errorMessage: string = '';

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const userId = this.authService.getUserId();
    if (!userId) {
      this.errorMessage = 'User not logged in.';
      return;
    }
    this.userService.getAllPastOrders(userId).subscribe({
      next: (orders: any) => {
        // If backend returns array of objects, use directly
        if (Array.isArray(orders)) {
          this.orders = orders;
        } else if (typeof orders === 'string') {
          // Fallback for string response (legacy)
          try {
            this.orders = JSON.parse(orders);
          } catch {
            this.orders = orders.split('\n').filter(line => line.trim() !== '');
          }
        } else {
          this.orders = [];
        }
      },
      error: (err) => this.errorMessage = err.error?.message || 'Failed to load orders.'
    });
  }

  goToOrderDetail(orderId: number): void {
    this.router.navigate(['/order-detail', orderId]);
  }
}
