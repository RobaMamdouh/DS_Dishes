import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-create-order',
  templateUrl: './create-order.component.html',
  styleUrls: ['./create-order.component.css']
})
export class CreateOrderComponent implements OnInit {
  dishes: any[] = [];
  quantities: { [dishId: number]: number } = {};
  successMessage: string = '';
  errorMessage: string = '';

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.userService.getAvailableDishes().subscribe({
      next: (data) => {
        this.dishes = data;
        this.dishes.forEach(dish => {
          this.quantities[dish.id] = 1;
        });
      },
      error: () => {
        this.errorMessage = 'Failed to load dishes.';
      }
    });
  }

  addToCart(dish: any): void {
    const quantity = this.quantities[dish.id];
    if (quantity < 1 || quantity > dish.quantity) {
      this.errorMessage = 'Please enter a valid quantity.';
      this.successMessage = '';
      return;
    }
    const cart = JSON.parse(localStorage.getItem('shoppingCart') || '[]');
    const existing = cart.find((item: any) => item.id === dish.id);
    if (existing) {
      existing.quantity += quantity;
    } else {
      cart.push({ ...dish, quantity });
    }
    localStorage.setItem('shoppingCart', JSON.stringify(cart));
    this.successMessage = `Added ${quantity} of ${dish.name} to cart!`;
    this.errorMessage = '';
  }
}
