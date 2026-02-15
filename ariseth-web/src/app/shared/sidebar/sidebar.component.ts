import { Component } from '@angular/core';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
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