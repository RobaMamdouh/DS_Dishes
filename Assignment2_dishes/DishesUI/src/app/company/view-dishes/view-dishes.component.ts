import { Component, OnInit } from '@angular/core';
import { SellerService } from '../../services/seller.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-view-dishes',
  templateUrl: './view-dishes.component.html',
  styleUrls: ['./view-dishes.component.css']
})
export class ViewDishesComponent implements OnInit {

  dishes: any[] = [];
  sellerId: number | null = null;

  constructor(private sellerService: SellerService, private authService: AuthService) {}

  ngOnInit(): void {
    this.sellerId = this.authService.getUserId();

    if (this.sellerId !== null) {
      this.sellerService.getDishesBySellerId(this.sellerId).subscribe({
        next: (data) => this.dishes = data,
        error: (err) => console.error('Failed to load dishes', err)
      });
    } else {
      console.warn('Seller ID is not available.');
    }
  }


}
