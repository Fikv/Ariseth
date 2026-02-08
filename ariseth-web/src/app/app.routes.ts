import { Routes } from '@angular/router';
import { LoginViewComponent } from './components/login-view/login-view.component'
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { loginGuard } from './core/auth/login.guard';
import { authGuard } from './core/auth/auth.guard';
export const routes: Routes = [
  {
    path: 'login',
    component: LoginViewComponent,
    canActivate: [loginGuard]
  },
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [authGuard]
  },
  {
    path: '',
    redirectTo: 'dashboard',
    pathMatch: 'full'
  },
  {
    path: '**',
    redirectTo: 'dashboard'
  }
];
