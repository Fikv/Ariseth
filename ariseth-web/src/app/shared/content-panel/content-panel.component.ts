import { ChangeDetectionStrategy, Component } from '@angular/core';
import { ThemeToggleComponent } from '../theme-toggle/theme-toggle.component';

@Component({
  selector: 'app-content-panel',
  standalone: true,
  imports: [ThemeToggleComponent],
  templateUrl: './content-panel.component.html',
  styleUrl: './content-panel.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ContentPanelComponent {}
