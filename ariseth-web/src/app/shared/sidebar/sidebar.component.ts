import { ChangeDetectionStrategy, Component } from '@angular/core';
import { LucideCalendar } from '@lucide/angular';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [LucideCalendar],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SidebarComponent {}
