import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';
import { LocalStorageService } from '../../common/local-storage.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private tokenKey : string = 'token';

  constructor(private http: HttpClient, private ls: LocalStorageService) {}

  onLogin(obj: any) : Observable<any> {
    console.log("probuje")
    return this.http.post(environment.apiUrl + environment.auth.login, obj)
  }

  setToken(token: string) {
    this.ls.setItem(this.tokenKey, token);
  }

  getToken(): string | null {
    return this.ls.getItem(this.tokenKey);
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  logout() {
    this.ls.removeItem(this.tokenKey);
  }

}
