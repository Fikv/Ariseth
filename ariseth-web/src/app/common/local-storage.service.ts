import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class LocalStorageService {
  private isBrowser(): boolean {
    return typeof window !== 'undefined' && !!window.localStorage;
  }

  setItem(key: string, value: string): void {
    if (!this.isBrowser()) return;
    window.localStorage.setItem(key, value);
  }

  getItem(key: string): string | null {
    if (!this.isBrowser()) return null;
    return window.localStorage.getItem(key);
  }

  removeItem(key: string): void {
    if (!this.isBrowser()) return;
    window.localStorage.removeItem(key);
  }

  clear(): void {
    if (!this.isBrowser()) return;
    window.localStorage.clear();
  }
}