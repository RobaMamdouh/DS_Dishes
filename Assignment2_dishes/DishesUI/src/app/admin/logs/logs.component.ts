import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../services/admin.service';

@Component({
  selector: 'app-logs',
  templateUrl: './logs.component.html',
  styleUrls: ['./logs.component.css']
})
export class LogsComponent implements OnInit {
  logs: string[] = [];
  loading = false;
  error: string | null = null;
  errorMessage: string = '';

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.fetchLogs();
  }

  fetchLogs() {
    this.loading = true;
    this.adminService.getErrorLogMessages().subscribe({
      next: (data) => {
        this.logs = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load error logs.';
        this.loading = false;
      }
    });
  }
}
