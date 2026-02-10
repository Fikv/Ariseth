import { Routes } from '@angular/router';
import { LoginViewComponent } from './features/auth/page/login-view/login-view.component'
import { loginGuard } from './core/auth/login.guard';
import { authGuard } from './core/auth/auth.guard';
import { HomeComponent } from './features/home/home.component';
export const routes: Routes = [
  {
    path: 'login',
    component: LoginViewComponent,
    canActivate: [loginGuard]
  },
  {
    path: 'dashboard',
    component: HomeComponent,
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
