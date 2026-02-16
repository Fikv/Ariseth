import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { SidebarComponent } from "../../shared/sidebar/sidebar.component";

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, SidebarComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  readonly cards = [
    { title: 'Active Users', value: '1,248', trend: '+8.2% this week' },
    { title: 'Revenue', value: '$42,890', trend: '+5.1% this month' },
    { title: 'New Orders', value: '316', trend: '+12.4% today' },
    { title: 'Conversion Rate', value: '3.9%', trend: '+0.6% from last period' }
  ];
}
