import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { SidebarComponent } from "../../shared/sidebar/sidebar.component";

type TaskTemplate = {
  id: string;
  name: string;
  description: string;
  durationMinutes: number;
  priority: 'Low' | 'Medium' | 'High';
  checklist: string[];
};

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

  readonly taskTemplates: TaskTemplate[] = [
    {
      id: 'focus-block',
      name: 'Focus Block',
      description: 'Single deep-work session with strict distractions off.',
      durationMinutes: 90,
      priority: 'High',
      checklist: ['Set clear outcome', 'Turn on do-not-disturb', 'Start timer']
    },
    {
      id: 'quick-admin',
      name: 'Quick Admin',
      description: 'Process small admin items in one short batch.',
      durationMinutes: 30,
      priority: 'Medium',
      checklist: ['Review inbox', 'Answer pending messages', 'File receipts']
    },
    {
      id: 'planning-session',
      name: 'Planning Session',
      description: 'Plan tomorrow with priorities and realistic time blocks.',
      durationMinutes: 45,
      priority: 'Low',
      checklist: ['Collect tasks', 'Order by impact', 'Assign time blocks']
    }
  ];

  selectedTaskTemplateId = this.taskTemplates[0].id;

  selectTaskTemplate(templateId: string): void {
    this.selectedTaskTemplateId = templateId;
  }

  get selectedTaskTemplate(): TaskTemplate {
    return this.taskTemplates.find((template) => template.id === this.selectedTaskTemplateId) ?? this.taskTemplates[0];
  }
}
