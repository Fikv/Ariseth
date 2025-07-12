import { Component } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { NgIf } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login-view',
  standalone: true,
  imports: [FormsModule, NgIf],
  templateUrl: './login-view.component.html',
  styleUrl: './login-view.component.css'
})
export class LoginViewComponent {
  login = '';
  password = '';
  error = '';

  constructor(private http: HttpClient, private router: Router) {}

  onLogin() {
    this.error = '';
    const credentials = btoa(`${this.login}:${this.password}`);
    const headers = new HttpHeaders({
      Authorization: `Basic ${credentials}`
    });

    this.http.get<{success: boolean}>('http://localhost:8080/api/me', { headers })
      .subscribe({
        next: (res) => {
          if (res.success) {
            this.router.navigate(['/crypto']);
          } else {
            this.error = 'Nieprawid這wy login lub has這!';
          }
        },
        error: (err) => {
          this.error = 'Nieprawid這wy login lub has這!';
        }
      });
  }
}
