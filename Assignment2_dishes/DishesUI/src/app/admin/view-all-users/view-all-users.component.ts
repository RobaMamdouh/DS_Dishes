import { Component, OnInit } from '@angular/core';
import { AdminService, User } from '../../services/admin.service';

@Component({
  selector: 'app-view-all-users',
  templateUrl: './view-all-users.component.html',
  styleUrls: ['./view-all-users.component.css']
})
export class ViewAllUsersComponent implements OnInit {

  Users: User[] = [];
  successMessage: string = '';
  errorMessage: string = '';

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.getAllUsers();
  }

  getAllUsers(): void {
    this.adminService.getAllUsers().subscribe({
      next: (data) => {
        this.Users = data;
        this.successMessage = 'Users loaded successfully';
        this.errorMessage = '';
      },
      error: (err) => {
        this.errorMessage = 'Error loading users: ' + err.message;
        this.successMessage = '';
      }
    });
  }
}
