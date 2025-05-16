import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-shopping-cart',
  templateUrl: './shopping-cart.component.html',
  styleUrls: ['./shopping-cart.component.css']
})
export class ShoppingCartComponent implements OnInit {
  cart: any[] = [];
  total: number = 0;
  successMessage: string = '';
  errorMessage: string = '';

  constructor(private authService: AuthService, private http: HttpClient, private router: Router , private userService: UserService) {}

  ngOnInit(): void {
    this.loadCart();
  }

  loadCart(): void {
    this.cart = JSON.parse(localStorage.getItem('shoppingCart') || '[]');
    this.total = this.cart.reduce((sum, item) => sum + (item.price * item.quantity), 0);
  }

  removeItem(index: number): void {
    this.cart.splice(index, 1);
    localStorage.setItem('shoppingCart', JSON.stringify(this.cart));
    this.loadCart();
  }

  clearCart(): void {
    this.cart = [];
    localStorage.removeItem('shoppingCart');
    this.total = 0;
  }

checkout(): void {
  if (this.cart.length === 0) {
    this.errorMessage = 'Your cart is empty.';
    this.successMessage = '';
    return;
  }

  const userId = this.authService.getUserId();
  if (!userId) {
    this.errorMessage = 'User not logged in.';
    this.successMessage = '';
    return;
  }

  const dishIdQuantityMap: { [key: number]: number } = {};
  this.cart.forEach(item => {
    dishIdQuantityMap[item.id] = item.quantity;
  });

  this.userService.createOrder(userId, dishIdQuantityMap).subscribe({
    next: () => {
      this.successMessage = 'Order placed successfully!';
      this.errorMessage = '';
      this.clearCart();
      setTimeout(() => this.router.navigate(['/user-home']), 2000);
    },
    error: (err) => {
      this.errorMessage = err.error?.message || 'Order failed. Please try again.';
      this.successMessage = '';
    }
  });
}

}
