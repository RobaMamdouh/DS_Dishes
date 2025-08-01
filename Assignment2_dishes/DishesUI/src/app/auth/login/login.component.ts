import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  username: string = '';
  password: string = '';
  errorMessages: { username?: string; password?: string; general?: string } = {};
  successMessage: string = '';

  constructor(private authService: AuthService, private router: Router) {
  }

  onSubmit() {
    this.errorMessages = {};
    this.successMessage = '';

    // Check for empty fields
    if (!this.username) {
      this.errorMessages.username = 'Username is required';
    }
    if (!this.password) {
      this.errorMessages.password = 'Password is required';
    }

    // If there are errors, do not proceed
    if (Object.keys(this.errorMessages).length > 0) {
      return;
    }

    const credentials = {username: this.username, password: this.password};

this.authService.login(credentials).subscribe(
  response => {
    if (
      response.message === 'Logged in as user successfully' ||
      response.message === 'Logged in as admin successfully' ||
      response.message === 'Logged in as company successfully'
    ) {
      this.successMessage = 'Login successful!';
      const userId = this.authService.getUserId();
      const userType = this.authService.getUserType();
      console.log('Logged in user ID:', userId, 'userType:', userType);
      if (userType && userType.toLowerCase() === "user") {
        setTimeout(() => {
          this.router.navigate(['/user-home']);
        }, 1000);
      } else if (userType && (userType.toLowerCase() === "seller" || userType.toLowerCase() === "company")) {
        setTimeout(() => {
          this.router.navigate(['/seller-home']);
        }, 1000);
      } else if (userType && userType.toLowerCase() === "admin") {
        setTimeout(() => {
          this.router.navigate(['/admin-home']);
        }, 1000);
      } else {
        this.errorMessages.general = 'Unknown user type: ' + userType;
      }
    } else {
      this.errorMessages.general = response.message || 'Login failed';
    }
  },
  error => {
    this.errorMessages.general = error.error?.error || 'Login failed';
  }
);
  }



}
