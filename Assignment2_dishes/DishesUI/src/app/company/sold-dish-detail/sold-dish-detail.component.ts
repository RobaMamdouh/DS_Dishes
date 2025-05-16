import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { SellerService } from '../../services/seller.service';

@Component({
  selector: 'app-sold-dish-detail',
  templateUrl: './sold-dish-detail.component.html',
  styleUrls: ['./sold-dish-detail.component.css']
})
export class SoldDishDetailComponent {
   dishId!: number;
    dishDetail: any = null;
    buyers: { username: string, quantity: number }[] = [];
    errorMessage: string = '';
  
    constructor(
      private route: ActivatedRoute,
      private sellerService: SellerService
    ) {}
  
    ngOnInit(): void {
      this.dishId = Number(this.route.snapshot.paramMap.get('id'));
      if (!this.dishId) {
        this.errorMessage = 'Invalid dish ID.';
        return;
      }
      // Fetch all sold dishes for the seller, then filter for this dish
      // (Assumes the backend returns buyerUsername and dishId for each sale)
      this.sellerService.getSoldDishesBySellerId(/* sellerId not needed for detail, but required by API */ 0).subscribe({
        next: (soldDishes) => {
          // Find all sales for this dish
          const buyersMap: { [username: string]: number } = {};
          let dishName = '';
          soldDishes.forEach((sold: any) => {
            if (sold.dishId === this.dishId) {
              dishName = sold.dishName;
              if (sold.buyerUsername) {
                buyersMap[sold.buyerUsername] = (buyersMap[sold.buyerUsername] || 0) + 1;
              }
            }
          });
          this.dishDetail = { id: this.dishId, name: dishName };
          this.buyers = Object.entries(buyersMap).map(([username, quantity]) => ({ username, quantity }));
          if (!this.dishDetail.name) {
            this.errorMessage = 'Dish details not found.';
          }
        },
        error: () => {
          this.errorMessage = 'Unable to load dish details.';
        }
      });
    }

}
