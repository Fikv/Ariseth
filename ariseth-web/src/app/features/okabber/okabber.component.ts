import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { SidebarComponent } from '../../shared/sidebar/sidebar.component';

type CalendarDay = {
  label: string;
  date: number;
  isToday?: boolean;
};

type TimeSlot = {
  time: string;
};

type CalendarEvent = {
  title: string;
  category: string;
  dayIndex: number;
  startHour: number;
  durationHours: number;
  accent: 'amber' | 'blue' | 'mint' | 'rose';
};

@Component({
  selector: 'app-okabber',
  standalone: true,
  imports: [CommonModule, SidebarComponent],
  templateUrl: './okabber.component.html',
  styleUrl: './okabber.component.css'
})
export class OkabberComponent {
  readonly days: CalendarDay[] = [
    { label: 'Mon', date: 14 },
    { label: 'Tue', date: 15, isToday: true },
    { label: 'Wed', date: 16 },
    { label: 'Thu', date: 17 },
    { label: 'Fri', date: 18 },
    { label: 'Sat', date: 19 },
    { label: 'Sun', date: 20 }
  ];

  readonly miniCalendarDays = Array.from({ length: 35 }, (_, index) => ({
    value: index + 1,
    muted: index < 3 || index > 30,
    active: index === 15
  }));

  readonly timeSlots: TimeSlot[] = [
    { time: '08:00' },
    { time: '09:00' },
    { time: '10:00' },
    { time: '11:00' },
    { time: '12:00' },
    { time: '13:00' },
    { time: '14:00' },
    { time: '15:00' },
    { time: '16:00' },
    { time: '17:00' },
    { time: '18:00' }
  ];

  readonly events: CalendarEvent[] = [
    { title: 'Design sync', category: 'Team', dayIndex: 1, startHour: 9, durationHours: 1.5, accent: 'amber' },
    { title: 'Deep work block', category: 'Focus', dayIndex: 2, startHour: 11, durationHours: 2, accent: 'blue' },
    { title: 'Client review', category: 'Call', dayIndex: 3, startHour: 13, durationHours: 1, accent: 'rose' },
    { title: 'Content sprint', category: 'Marketing', dayIndex: 4, startHour: 10, durationHours: 2.5, accent: 'mint' },
    { title: 'Ops checkpoint', category: 'Internal', dayIndex: 0, startHour: 15, durationHours: 1, accent: 'amber' },
    { title: 'Launch prep', category: 'Release', dayIndex: 5, startHour: 12, durationHours: 2, accent: 'blue' }
  ];

  readonly upcomingTasks = [
    { time: '09:30', title: 'Update roadmap notes', meta: '15 min draft' },
    { time: '12:15', title: 'Reply to vendor questions', meta: 'Inbox zero batch' },
    { time: '16:00', title: 'Prepare tomorrow blocks', meta: 'Review energy + priorities' }
  ];

  trackByTime(_: number, slot: TimeSlot): string {
    return slot.time;
  }

  getEventStyle(event: CalendarEvent): Record<string, string> {
    const startOffset = (event.startHour - 8) * 84 + 56;
    const height = event.durationHours * 84 - 10;

    return {
      left: `calc(76px + ${event.dayIndex} * ((100% - 76px) / 7) + 6px)`,
      width: 'calc((100% - 76px) / 7 - 12px)',
      top: `${startOffset}px`,
      height: `${height}px`
    };
  }
}
