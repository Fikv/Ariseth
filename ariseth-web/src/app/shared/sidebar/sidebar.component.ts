import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SidebarComponent {
  readonly menu = [
    {
      label: 'Home',
      icon: 'home',
      route: '/dashboard'
    },
    {
      label: 'Okabber',
      icon: 'dayplanner',
      route: '/okabber'
    },
    {
      label: 'Logo',
      icon: 'logo',
      route: '/dashboard'
    }
  ];
}
