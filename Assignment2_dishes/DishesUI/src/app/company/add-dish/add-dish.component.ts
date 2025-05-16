import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { SellerService } from '../../services/seller.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-add-dish',
  templateUrl: './add-dish.component.html',
  styleUrls: ['./add-dish.component.css']
})
export class AddDishComponent implements OnInit {

  dishForm!: FormGroup;
  successMessage: string = '';
  errorMessages: any = {};

  constructor(
  private fb: FormBuilder,
  private sellerService: SellerService, 
  private authService: AuthService
) {}
  

  ngOnInit(): void {
    this.dishForm = this.fb.group({
      name: ['', Validators.required],
      price: [null, [Validators.required, Validators.min(0)]],
      quantity: [1, [Validators.required, Validators.min(1)]],
    });
  }

  onSubmit(): void {
    if (this.dishForm.invalid) {
      this.errorMessages.general = 'Please fill in all required fields correctly.';
      return;
    }

    const sellerId = this.authService.getUserId();
    if (!sellerId) {
      this.errorMessages.general = 'Seller ID not found. Please login again.';
      return;
    }

    const dishData = {
      ...this.dishForm.value,
      sellerId: sellerId
    };

    this.sellerService.createDish(dishData).subscribe({
      next: () => {
        this.successMessage = 'Dish created successfully!';
        this.errorMessages = {};
        this.dishForm.reset();
      },
      error: (error) => {
        console.error('Dish creation failed', error);
        this.errorMessages.general = 'Failed to create dish. Try again.';
      }
    });
  }


}
