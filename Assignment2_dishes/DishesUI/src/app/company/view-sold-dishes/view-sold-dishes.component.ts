import { Component, OnInit } from '@angular/core';
import { SellerService } from '../../services/seller.service';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-view-sold-dishes',
  templateUrl: './view-sold-dishes.component.html',
  styleUrls: ['./view-sold-dishes.component.css']
})
export class ViewSoldDishesComponent implements OnInit {
  dishes: any[] = [];
  sellerId: number | null = null;

  constructor(
    private sellerService: SellerService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.sellerId = this.authService.getUserId();
    if (this.sellerId !== null) {
      this.sellerService.getSoldDishesBySellerId(this.sellerId).subscribe({
        next: (data) => this.dishes = data,
        error: (err) => console.error('Failed to load sold dishes', err)
      });
    }
  }

  viewDishDetails(dishId: number): void {
    this.router.navigate(['/sold-dish-detail', dishId]);
  }
}
