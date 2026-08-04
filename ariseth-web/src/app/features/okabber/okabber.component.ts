import { ChangeDetectionStrategy, Component, OnInit, computed, inject, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import {
  LucideCalendarPlus,
  LucideChevronLeft,
  LucideChevronRight,
  LucideTrash2,
  LucideX
} from '@lucide/angular';
import { LocalStorageService } from '../../common/local-storage.service';
import { ContentPanelComponent } from '../../shared/content-panel/content-panel.component';
import { SidebarComponent } from '../../shared/sidebar/sidebar.component';

type PlannerView = 'week' | 'month' | 'year';

interface PlannerTask {
  id: string;
  title: string;
  date: string;
  startTime: string;
  durationMinutes: number;
}

interface CalendarDay {
  date: Date;
  key: string;
  inCurrentMonth: boolean;
}

@Component({
  selector: 'app-okabber',
  standalone: true,
  imports: [
    SidebarComponent,
    ContentPanelComponent,
    ReactiveFormsModule,
    LucideCalendarPlus,
    LucideChevronLeft,
    LucideChevronRight,
    LucideTrash2,
    LucideX
  ],
  templateUrl: './okabber.component.html',
  styleUrl: './okabber.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class OkabberComponent implements OnInit {
  private readonly storage = inject(LocalStorageService);
  private readonly storageKey = 'ariseth-planner-tasks';
  private readonly rowHeight = 64;
  private readonly timelineStartHour = 0;

  readonly view = signal<PlannerView>('week');
  readonly anchorDate = signal(this.startOfDay(new Date()));
  readonly tasks = signal<PlannerTask[]>([]);
  readonly isEditorOpen = signal(false);
  readonly editingTaskId = signal<string | null>(null);

  readonly hours = Array.from({ length: 24 }, (_, index) => this.timelineStartHour + index);
  readonly weekDayLabels = ['Pon', 'Wt', 'Sr', 'Czw', 'Pt', 'Sob', 'Niedz'];
  readonly monthLabels = [
    'Styczeń', 'Luty', 'Marzec', 'Kwiecień', 'Maj', 'Czerwiec',
    'Lipiec', 'Sierpień', 'Wrzesień', 'Październik', 'Listopad', 'Grudzień'
  ];

  readonly taskForm = new FormGroup({
    title: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.maxLength(80)]
    }),
    date: new FormControl(this.formatDateKey(new Date()), {
      nonNullable: true,
      validators: [Validators.required]
    }),
    startTime: new FormControl('09:00', {
      nonNullable: true,
      validators: [Validators.required]
    }),
    durationMinutes: new FormControl(60, {
      nonNullable: true,
      validators: [Validators.required, Validators.min(15)]
    })
  });

  readonly weekDays = computed(() => {
    const start = this.startOfWeek(this.anchorDate());
    return Array.from({ length: 7 }, (_, index) => this.addDays(start, index));
  });

  readonly monthDays = computed<CalendarDay[]>(() => {
    const anchor = this.anchorDate();
    const firstDay = new Date(anchor.getFullYear(), anchor.getMonth(), 1);
    const gridStart = this.startOfWeek(firstDay);

    return Array.from({ length: 42 }, (_, index) => {
      const date = this.addDays(gridStart, index);
      return {
        date,
        key: this.formatDateKey(date),
        inCurrentMonth: date.getMonth() === anchor.getMonth()
      };
    });
  });

  readonly yearMonths = computed(() => {
    const year = this.anchorDate().getFullYear();
    return this.monthLabels.map((label, monthIndex) => ({
      label,
      monthIndex,
      taskCount: this.tasks().filter(task => {
        const date = this.parseDateKey(task.date);
        return date.getFullYear() === year && date.getMonth() === monthIndex;
      }).length
    }));
  });

  readonly rangeTitle = computed(() => {
    const anchor = this.anchorDate();

    if (this.view() === 'year') {
      return String(anchor.getFullYear());
    }

    if (this.view() === 'month') {
      return new Intl.DateTimeFormat('pl-PL', {
        month: 'long',
        year: 'numeric'
      }).format(anchor);
    }

    const days = this.weekDays();
    const start = days[0];
    const end = days[6];
    const startText = new Intl.DateTimeFormat('pl-PL', {
      day: 'numeric',
      month: start.getMonth() === end.getMonth() ? undefined : 'short'
    }).format(start);
    const endText = new Intl.DateTimeFormat('pl-PL', {
      day: 'numeric',
      month: 'short',
      year: 'numeric'
    }).format(end);
    return `${startText} - ${endText}`;
  });

  ngOnInit(): void {
    const savedTasks = this.storage.getItem(this.storageKey);
    if (!savedTasks) return;

    try {
      const tasks = JSON.parse(savedTasks);
      if (Array.isArray(tasks)) {
        this.tasks.set(tasks);
      }
    } catch {
      this.storage.removeItem(this.storageKey);
    }
  }

  setView(view: PlannerView): void {
    this.view.set(view);
  }

  navigate(direction: -1 | 1): void {
    const anchor = this.anchorDate();
    if (this.view() === 'week') {
      this.anchorDate.set(this.addDays(anchor, direction * 7));
    } else if (this.view() === 'month') {
      this.anchorDate.set(new Date(anchor.getFullYear(), anchor.getMonth() + direction, 1));
    } else {
      this.anchorDate.set(new Date(anchor.getFullYear() + direction, 0, 1));
    }
  }

  goToToday(): void {
    this.anchorDate.set(this.startOfDay(new Date()));
  }

  openNewTask(date = this.anchorDate(), startTime = '09:00'): void {
    this.editingTaskId.set(null);
    this.taskForm.reset({
      title: '',
      date: this.formatDateKey(date),
      startTime,
      durationMinutes: 60
    });
    this.isEditorOpen.set(true);
  }

  editTask(task: PlannerTask, event?: Event): void {
    event?.stopPropagation();
    this.editingTaskId.set(task.id);
    this.taskForm.reset({
      title: task.title,
      date: task.date,
      startTime: task.startTime,
      durationMinutes: task.durationMinutes
    });
    this.isEditorOpen.set(true);
  }

  closeEditor(): void {
    this.isEditorOpen.set(false);
    this.editingTaskId.set(null);
  }

  saveTask(): void {
    if (this.taskForm.invalid) {
      this.taskForm.markAllAsTouched();
      return;
    }

    const formValue = this.taskForm.getRawValue();
    const editingId = this.editingTaskId();
    const task: PlannerTask = {
      id: editingId ?? `${Date.now()}-${Math.random().toString(16).slice(2)}`,
      ...formValue
    };

    this.tasks.update(tasks => editingId
      ? tasks.map(currentTask => currentTask.id === editingId ? task : currentTask)
      : [...tasks, task]
    );
    this.persistTasks();
    this.closeEditor();
  }

  deleteTask(): void {
    const editingId = this.editingTaskId();
    if (!editingId) return;
    this.tasks.update(tasks => tasks.filter(task => task.id !== editingId));
    this.persistTasks();
    this.closeEditor();
  }

  tasksForDate(date: Date | string): PlannerTask[] {
    const key = typeof date === 'string' ? date : this.formatDateKey(date);
    return this.tasks()
      .filter(task => task.date === key)
      .sort((first, second) => first.startTime.localeCompare(second.startTime));
  }

  taskTop(task: PlannerTask): number {
    const [hour, minute] = task.startTime.split(':').map(Number);
    return Math.max(0, ((hour - this.timelineStartHour) + minute / 60) * this.rowHeight);
  }

  taskHeight(task: PlannerTask): number {
    return Math.max(34, task.durationMinutes / 60 * this.rowHeight - 4);
  }

  selectMonth(monthIndex: number): void {
    this.anchorDate.set(new Date(this.anchorDate().getFullYear(), monthIndex, 1));
    this.view.set('month');
  }

  isToday(date: Date): boolean {
    return this.formatDateKey(date) === this.formatDateKey(new Date());
  }

  dayNumber(date: Date): string {
    return new Intl.DateTimeFormat('pl-PL', { day: '2-digit' }).format(date);
  }

  private persistTasks(): void {
    this.storage.setItem(this.storageKey, JSON.stringify(this.tasks()));
  }

  private startOfDay(date: Date): Date {
    return new Date(date.getFullYear(), date.getMonth(), date.getDate());
  }

  private startOfWeek(date: Date): Date {
    const result = this.startOfDay(date);
    const day = result.getDay() || 7;
    result.setDate(result.getDate() - day + 1);
    return result;
  }

  private addDays(date: Date, days: number): Date {
    const result = new Date(date);
    result.setDate(result.getDate() + days);
    return result;
  }

  private formatDateKey(date: Date): string {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  private parseDateKey(key: string): Date {
    const [year, month, day] = key.split('-').map(Number);
    return new Date(year, month - 1, day);
  }
}
