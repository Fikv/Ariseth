import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css'
})
export class SidebarComponent {
  menu = [
    {
      label: 'Home',
      iconClass: 'icon-home',
      action: () => console.log('Home clicked')
    },
    {
      label: 'Settings',
      iconClass: 'icon-settings',
      action: () => console.log('Settings clicked')
    },
    {
      label: 'Dayplanner',
      iconClass: 'icon-dayplanner',
      action: () => console.log('Dayplanner clicked')
    },
    {
      label: 'Profile',
      iconClass: 'icon-profile',
      action: () => console.log('Profile clicked')
    },
    {
      label: 'Logo',
      iconClass: 'icon-logo',
      action: () => console.log('Logo clicked')
    }
  ];
}
