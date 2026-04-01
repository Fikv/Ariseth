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
      icon: 'home',
      action: () => console.log('Home clicked')
    },
    {
      label: 'Settings',
      icon: 'settings',
      action: () => console.log('Settings clicked')
    },
    {
      label: 'Dayplanner',
      icon: 'dayplanner',
      action: () => console.log('Dayplanner clicked')
    },
    {
      label: 'Profile',
      icon: 'profile',
      action: () => console.log('Profile clicked')
    },
    {
      label: 'Logo',
      icon: 'logo',
      action: () => console.log('Logo clicked')
    }
  ];
}
