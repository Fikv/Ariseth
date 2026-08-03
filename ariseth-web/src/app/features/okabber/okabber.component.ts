import { Component } from '@angular/core';
import { SidebarComponent } from '../../shared/sidebar/sidebar.component';
import { ContentPanelComponent } from "../../shared/content-panel/content-panel.component";

@Component({
  selector: 'app-okabber',
  standalone: true,
  imports: [SidebarComponent, ContentPanelComponent],
  templateUrl: './okabber.component.html',
  styleUrl: './okabber.component.css'
})
export class OkabberComponent {}
