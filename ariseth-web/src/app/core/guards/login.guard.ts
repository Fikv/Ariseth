import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthenticationService } from '../auth/authentication.service';

@Injectable({
  providedIn: 'root'
})
export class LoginGuard implements CanActivate {

  constructor(
    private auth: AuthenticationService,
    private router: Router
  ) {}

  canActivate(): boolean {
    if (this.auth.isTokenValid()) {
      this.router.navigate(['/dashboard']);
      return false;
    }
    return true;
  }
}