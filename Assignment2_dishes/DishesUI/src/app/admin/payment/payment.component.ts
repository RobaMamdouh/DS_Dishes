import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../services/admin.service';

@Component({
  selector: 'app-payment',
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.css']
})
export class PaymentComponent implements OnInit {
  failures: string[] = [];
  loading = false;
  error: string | null = null;

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.fetchFailures();
  }

  fetchFailures() {
    this.loading = true;
    this.adminService.getPaymentFailedMessages().subscribe({
      next: (data) => {
        this.failures = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load payment failures.';
        this.loading = false;
      }
    });
  }
}
