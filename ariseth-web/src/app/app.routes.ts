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
    path: 'dashboard',
    component: HomeComponent,
    canActivate: [authGuard]
  },
  {
    path: 'okabber',
    component: OkabberComponent,
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
