import { Routes } from '@angular/router';
import { LoginViewComponent } from './components/login-view/login-view.component'
import { DashboardComponent } from './components/dashboard/dashboard.component';
export const routes: Routes = [
  {
    path: 'login',
    component: LoginViewComponent
  },
  {
    path: 'dashboard',
    component: DashboardComponent,
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
