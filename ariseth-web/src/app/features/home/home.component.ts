import { Component } from '@angular/core';
import { SidebarComponent } from "../../shared/sidebar/sidebar.component";
import { ContentPanelComponent } from '../../shared/content-panel/content-panel.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [SidebarComponent, ContentPanelComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {}
