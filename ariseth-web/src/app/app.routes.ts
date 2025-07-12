import { Routes } from '@angular/router';
import { LoginViewComponent } from './login-view/login-view.component'
import {CryptoComponent} from "./crypto/crypto.component";

export const routes: Routes = [
  { path: 'login', component: LoginViewComponent },
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'crypto', component: CryptoComponent },
];
