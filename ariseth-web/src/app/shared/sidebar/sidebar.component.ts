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
      icon: 'fa fa-home',
      action: () => console.log('Home clicked')
    },
    {
      label: 'Settings',
      icon: 'fa fa-cog',
      action: () => console.log('Settings clicked')
    },
    {
      label: 'Profile',
      icon: 'fa fa-user',
      action: () => console.log('Profile clicked')
    }
  ];
}
