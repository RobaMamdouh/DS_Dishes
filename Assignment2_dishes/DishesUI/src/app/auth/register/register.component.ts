import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {

  username: string = '';
  password: string = '';
  balance: number = 0;
  errorMessages: {balance?: string; password?: string; username?: string; general?: string } = {};
  successMessage: string = '';

  constructor(private authService: AuthService, private router: Router) { }

  onSubmit() {
    this.errorMessages = {};
    this.successMessage = '';

    // Validate inputs
    if (!this.username) {
      this.errorMessages.username = 'Username is required';
    }
    if (!this.password) {
      this.errorMessages.password = 'Password is required';
    } else {
      // Password validation
      if (this.password.length < 6) {
        this.errorMessages.password = 'Password must be at least 6 characters long';
      }
    }
    if (!this.password) {
      this.errorMessages.password = 'Password is required';
    }
    if (this.balance < 0) {
      this.errorMessages.balance = 'Balance cannot be negative';
    } 

    // If there are any errors, return early
    if (Object.keys(this.errorMessages).length > 0) {
      return;
    }

    // Add role: 'USER' to registration payload and ensure balance is a number
    const user = { 
      username: this.username, 
      password: this.password, 
      balance: Number(this.balance), // Ensure it's a number
      role: 'USER' 
    };
    console.log('Register payload:', user); // Debug: log payload
  
    this.authService.register(user).subscribe(
      response => {
        this.successMessage = response.message;
        this.successMessage = 'Registered successfully!';
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 2000);
      },
      error => {
        this.errorMessages.general = error.error?.error || 'Registration failed';
      }
    );
  }

}
