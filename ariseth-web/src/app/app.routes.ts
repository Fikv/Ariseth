import { Routes } from '@angular/router';
import { LoginViewComponent } from './features/auth/page/login-view/login-view.component'
import { loginGuard } from './core/auth/login.guard';
import { authGuard } from './core/auth/auth.guard';
import { HomeComponent } from './features/home/home.component';
import { OkabberComponent } from './features/okabber/okabber.component';
export const routes: Routes = [
  {
    path: 'login',
    component: LoginViewComponent,
    canActivate: [loginGuard]
  },
  {
    path: 'home',
    component: HomeComponent,
    canActivate: [authGuard]
  },
  {
    path: 'planner',
    component: OkabberComponent,
    canActivate: [authGuard]
  },
  {
    path: 'dashboard',
    redirectTo: 'home',
    pathMatch: 'full'
  },
  {
    path: 'okabber',
    redirectTo: 'planner',
    pathMatch: 'full'
  },
  {
    path: '',
    redirectTo: 'home',
    pathMatch: 'full'
  },
  {
    path: '**',
    redirectTo: 'home'
  }
];
