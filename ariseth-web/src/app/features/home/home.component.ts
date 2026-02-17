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
    { title: 'Card1', value: '1', trend: '2', layoutClass: 'card-shape-wide' },
    { title: 'Card1', value: '1', trend: '2', layoutClass: 'card-shape-tall' },
    { title: 'Card1', value: '1', trend: '2', layoutClass: 'card-shape-round' },
    { title: 'Card1', value: '1', trend: '2', layoutClass: 'card-shape-angled' },
    { title: 'Card1', value: '1', trend: '2', layoutClass: 'card-shape-pill' },
    { title: 'Card1', value: '1', trend: '2', layoutClass: 'card-shape-cut' }
  ];
}
