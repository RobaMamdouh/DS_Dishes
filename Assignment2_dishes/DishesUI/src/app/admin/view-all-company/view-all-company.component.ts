import { Component, OnInit } from '@angular/core';
import { AdminService, User } from '../../services/admin.service';

@Component({
  selector: 'app-view-all-company',
  templateUrl: './view-all-company.component.html',
  styleUrls: ['./view-all-company.component.css']
})
export class ViewAllCompanyComponent implements OnInit {

  Companies: User[] = [];
  successMessage: string = '';
  errorMessage: string = '';

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.getAllCompanies();
  }

  getAllCompanies(): void {
    this.adminService.getAllCompanies().subscribe({
      next: (data) => {
        this.Companies = data;
        this.successMessage = 'Companies loaded successfully';
        this.errorMessage = '';
      },
      error: (err) => {
        this.errorMessage = 'Error loading companies: ' + err.message;
        this.successMessage = '';
      }
    });
  }

}
