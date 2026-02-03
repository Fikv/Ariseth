import { Routes } from '@angular/router';
import { LoginViewComponent } from './components/login-view/login-view.component'
import { DashboardComponent } from './dashboard/dashboard.component';
import { LoginGuard } from './core/guards/login.guard';
import { AuthGuard } from './core/auth/auth.guard';

export const routes: Routes = [
  {
    path: 'login',
    component: LoginViewComponent,
    canActivate: [LoginGuard]
  },
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [AuthGuard]
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
