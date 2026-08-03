import { DOCUMENT, isPlatformBrowser } from '@angular/common';
import { Inject, Injectable, PLATFORM_ID, computed, signal } from '@angular/core';

export type AppTheme = 'light' | 'dark';

@Injectable({
  providedIn: 'root'
})
export class ThemeService {
  private readonly storageKey = 'ariseth-theme';
  private readonly themeState = signal<AppTheme>('dark');
  private transitionTimer?: ReturnType<typeof setTimeout>;

  readonly theme = this.themeState.asReadonly();
  readonly isDark = computed(() => this.themeState() === 'dark');

  constructor(
    @Inject(DOCUMENT) private readonly document: Document,
    @Inject(PLATFORM_ID) private readonly platformId: object
  ) {
    if (!isPlatformBrowser(this.platformId)) {
      return;
    }

    const savedTheme = window.localStorage.getItem(this.storageKey);
    const systemTheme: AppTheme = window.matchMedia('(prefers-color-scheme: light)').matches
      ? 'light'
      : 'dark';

    this.setTheme(savedTheme === 'light' || savedTheme === 'dark' ? savedTheme : systemTheme, false);
  }

  toggleTheme(): void {
    this.changeTheme(this.isDark() ? 'light' : 'dark');
  }

  changeTheme(nextTheme: AppTheme): void {
    if (nextTheme === this.themeState()) {
      return;
    }

    if (!isPlatformBrowser(this.platformId)
      || window.matchMedia('(prefers-reduced-motion: reduce)').matches) {
      this.setTheme(nextTheme);
      return;
    }

    const root = this.document.documentElement;
    root.classList.remove('theme-transitioning');
    void root.offsetWidth;
    root.classList.add('theme-transitioning');
    this.setTheme(nextTheme);

    if (this.transitionTimer) {
      clearTimeout(this.transitionTimer);
    }

    this.transitionTimer = setTimeout(() => {
      root.classList.remove('theme-transitioning');
    }, 650);
  }

  setTheme(theme: AppTheme, persist = true): void {
    this.themeState.set(theme);
    this.document.documentElement.dataset['theme'] = theme;
    this.document.documentElement.style.colorScheme = theme;

    if (persist && isPlatformBrowser(this.platformId)) {
      window.localStorage.setItem(this.storageKey, theme);
    }
  }
}
