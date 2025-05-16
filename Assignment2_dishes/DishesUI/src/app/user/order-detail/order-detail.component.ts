import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-order-detail',
  templateUrl: './order-detail.component.html',
  styleUrls: ['./order-detail.component.css']
})
export class OrderDetailComponent implements OnInit {
  order: any = null;
  errorMessage: string = '';

  constructor(
    private route: ActivatedRoute,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    const orderId = Number(this.route.snapshot.paramMap.get('id'));
    if (!orderId) {
      this.errorMessage = 'Invalid order ID.';
      return;
    }
    this.userService.getOrderById(orderId).subscribe({
      next: (order: any) => this.order = order,
      error: (err: any) => this.errorMessage = err.error?.message || 'Failed to load order details.'
    });
  }
}
