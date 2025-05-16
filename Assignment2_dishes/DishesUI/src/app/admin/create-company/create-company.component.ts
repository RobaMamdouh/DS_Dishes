import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { AdminService } from '../../services/admin.service';

@Component({
  selector: 'app-create-company',
  templateUrl: './create-company.component.html',
  styleUrls: ['./create-company.component.css']
})
export class CreateCompanyComponent {
  accountForm: FormGroup;
  errorMessage: string | null = null;
  errorMessages: { username?: string; general?: string } = {};
  successMessage: string = '';

  
  constructor(private fb: FormBuilder, private authService: AuthService , private router: Router , private adminService: AdminService) {
    this.accountForm = this.fb.group({
      userId: [this.authService.getUserId(), Validators.required],
      username: ['', Validators.required]
    });
  }

  onSubmit() {
    if (this.accountForm.valid) {
      const formData = this.accountForm.value;
      this.adminService.createCompanyAccount(formData).subscribe(
        (response) => {
          this.successMessage = 'Company account created successfully!';
          this.errorMessage = null;
          this.errorMessages = {};
        },
        (error) => {
          this.successMessage = '';
          if (error.status === 400) {
            this.errorMessages.username = 'Username already exists.';
          } else {
            this.errorMessages.general = 'An error occurred. Please try again.';
          }
        }
      );
    } else {
      this.errorMessage = 'Please fill in all required fields.';
    }
  }

}
