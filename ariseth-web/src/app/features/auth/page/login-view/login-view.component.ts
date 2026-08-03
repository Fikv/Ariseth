import { Component, OnInit, signal } from '@angular/core';
import { AuthService } from '../../../../core/auth/auth.service';
import { Router } from '@angular/router';
import { ThemeToggleComponent } from '../../../../shared/theme-toggle/theme-toggle.component';
import { LucideArrowUpRight } from '@lucide/angular';

@Component({
  selector: 'app-login-view',
  standalone: true,
  imports: [ThemeToggleComponent, LucideArrowUpRight],
  templateUrl: './login-view.component.html',
  styleUrl: './login-view.component.css',
})
export class LoginViewComponent implements OnInit {
  loginInput = signal('');
  passwordInput = signal('');

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {

  }

  onLogin(): void {
    const loginObj = {
      login: this.loginInput(),
      password: this.passwordInput(),
    };

    this.authService.onLogin(loginObj).subscribe({
      next: (resp) => {
        this.authService.setToken(resp.token);
        this.router.navigate(['/home']);
      },
      error: (err) => {
        console.error('LOGIN ERROR', err);
      },
    });
  }
}
