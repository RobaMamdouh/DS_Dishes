import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { SellerService } from '../../services/seller.service';

@Component({
  selector: 'app-update-dish',
  templateUrl: './update-dish.component.html',
  styleUrls: ['./update-dish.component.css']
})
export class UpdateDishComponent implements OnInit {
  dishForm!: FormGroup;
  dish: any = null;
  successMessage: string = '';
  errorMessages: { general?: string } = {};

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private sellerService: SellerService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const dishId = Number(this.route.snapshot.paramMap.get('id'));
    if (!dishId) {
      this.errorMessages.general = 'Invalid dish ID.';
      return;
    }
    this.sellerService.getDishById(dishId).subscribe({
      next: (dish) => {
        this.dish = dish;
        this.dishForm = this.fb.group({
          name: [dish.name, Validators.required],
          price: [dish.price, [Validators.required, Validators.min(0)]],
          quantity: [dish.quantity, [Validators.required, Validators.min(1)]]
        });
      },
      error: () => {
        this.errorMessages.general = 'Unable to load dish details.';
      }
    });
  }

  onSubmit(): void {
    if (this.dishForm.invalid) {
      this.errorMessages.general = 'Please fill in all required fields correctly.';
      return;
    }
    const updatedDish = { ...this.dish, ...this.dishForm.value };
    this.sellerService.updateDish(this.dish.id, updatedDish).subscribe({
      next: () => {
        this.successMessage = 'Dish updated successfully!';
        this.errorMessages = {};
        setTimeout(() => {
          this.router.navigateByUrl('/view-dishes');
        }, 1000);
      },
      error: () => {
        this.successMessage = '';
        this.errorMessages.general = 'Failed to update dish. Try again.';
      }
    });
  }
}
