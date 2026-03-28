package fikv.ariseth.services;

import fikv.ariseth.dtos.DayPlannerRequestDTO;
import fikv.ariseth.dtos.DayPlannerResponseDTO;
import fikv.ariseth.dtos.TaskRequestDTO;
import fikv.ariseth.dtos.TaskResponseDTO;
import fikv.ariseth.entities.DayPlanner;
import fikv.ariseth.entities.Task;
import fikv.ariseth.entities.TaskPriority;
import fikv.ariseth.entities.TaskStatus;
import fikv.ariseth.entities.User;
import fikv.ariseth.repositories.DayPlannerRepository;
import fikv.ariseth.repositories.TaskRepository;
import fikv.ariseth.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlannerServiceTest {

    @Mock
    private DayPlannerRepository dayPlannerRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PlannerService plannerService;

    private User user;
    private DayPlanner dayPlanner;
    private Task task;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setLogin("john");

        dayPlanner = new DayPlanner();
        dayPlanner.setId(10L);
        dayPlanner.setUser(user);
        dayPlanner.setPlanDate(LocalDate.of(2026, 3, 27));
        dayPlanner.setNotes("Plan notes");
        dayPlanner.setTasks(new ArrayList<>());
        dayPlanner.setCreatedTimestamp(LocalDateTime.of(2026, 3, 27, 8, 0));
        dayPlanner.setUpdatedTimestamp(LocalDateTime.of(2026, 3, 27, 8, 0));

        task = new Task();
        task.setId(100L);
        task.setDayPlan(dayPlanner);
        task.setTitle("Write tests");
        task.setDescription("Cover untested methods");
        task.setStatus(TaskStatus.TODO);
        task.setPriority(TaskPriority.HIGH);
        task.setSortOrder(1);
        task.setCreatedTimestamp(LocalDateTime.of(2026, 3, 27, 9, 0));
        task.setUpdatedTimestamp(LocalDateTime.of(2026, 3, 27, 9, 0));
    }

    @Test
    void createDayPlan_shouldCreateAndMapResponse() {
        DayPlannerRequestDTO request = new DayPlannerRequestDTO(
                1L,
                LocalDate.of(2026, 3, 28),
                "New plan"
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(dayPlannerRepository.save(any(DayPlanner.class))).thenAnswer(invocation -> {
            DayPlanner saved = invocation.getArgument(0);
            saved.setId(11L);
            saved.setTasks(new ArrayList<>());
            return saved;
        });

        DayPlannerResponseDTO response = plannerService.createDayPlan(request);

        assertThat(response.id()).isEqualTo(11L);
        assertThat(response.userId()).isEqualTo(1L);
        assertThat(response.planDate()).isEqualTo(LocalDate.of(2026, 3, 28));
        assertThat(response.notes()).isEqualTo("New plan");
        assertThat(response.tasks()).isEmpty();
    }

    @Test
    void createDayPlan_shouldThrowWhenUserNotFound() {
        DayPlannerRequestDTO request = new DayPlannerRequestDTO(
                99L,
                LocalDate.of(2026, 3, 28),
                "New plan"
        );
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> plannerService.createDayPlan(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");
    }

    @Test
    void getDayPlans_shouldMapResults() {
        dayPlanner.setTasks(List.of(task));
        when(dayPlannerRepository.findByUserIdAndPlanDateBetweenOrderByPlanDateAsc(
                1L, LocalDate.of(2026, 3, 1), LocalDate.of(2026, 3, 31))
        ).thenReturn(List.of(dayPlanner));

        List<DayPlannerResponseDTO> result = plannerService.getDayPlans(
                1L, LocalDate.of(2026, 3, 1), LocalDate.of(2026, 3, 31)
        );

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().tasks()).hasSize(1);
        assertThat(result.getFirst().tasks().getFirst().title()).isEqualTo("Write tests");
    }

    @Test
    void updateDayPlan_shouldSwitchUserWhenUserChanged() {
        User otherUser = new User();
        otherUser.setId(2L);
        dayPlanner.setTasks(new ArrayList<>());

        DayPlannerRequestDTO request = new DayPlannerRequestDTO(
                2L,
                LocalDate.of(2026, 4, 1),
                "Updated notes"
        );

        when(dayPlannerRepository.findById(10L)).thenReturn(Optional.of(dayPlanner));
        when(userRepository.findById(2L)).thenReturn(Optional.of(otherUser));
        when(dayPlannerRepository.save(any(DayPlanner.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DayPlannerResponseDTO result = plannerService.updateDayPlan(10L, request);

        assertThat(result.userId()).isEqualTo(2L);
        assertThat(result.planDate()).isEqualTo(LocalDate.of(2026, 4, 1));
        assertThat(result.notes()).isEqualTo("Updated notes");
    }

    @Test
    void createTask_shouldCreateAndMapResponse() {
        TaskRequestDTO request = new TaskRequestDTO(
                10L,
                "Task title",
                "Task description",
                TaskStatus.IN_PROGRESS,
                TaskPriority.CRITICAL,
                LocalDateTime.of(2026, 3, 27, 10, 0),
                LocalDateTime.of(2026, 3, 27, 11, 0),
                null,
                2
        );

        when(dayPlannerRepository.findById(10L)).thenReturn(Optional.of(dayPlanner));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
            Task saved = invocation.getArgument(0);
            saved.setId(101L);
            return saved;
        });

        TaskResponseDTO response = plannerService.createTask(request);

        assertThat(response.id()).isEqualTo(101L);
        assertThat(response.dayPlanId()).isEqualTo(10L);
        assertThat(response.title()).isEqualTo("Task title");
        assertThat(response.status()).isEqualTo(TaskStatus.IN_PROGRESS);
        assertThat(response.priority()).isEqualTo(TaskPriority.CRITICAL);
        assertThat(response.sortOrder()).isEqualTo(2);
    }

    @Test
    void getTaskById_shouldThrowWhenTaskNotFound() {
        when(taskRepository.findById(200L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> plannerService.getTaskById(200L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Task not found");
    }

    @Test
    void getTasksByDayPlanId_shouldMapResults() {
        when(taskRepository.findByDayPlanIdOrderBySortOrderAsc(10L)).thenReturn(List.of(task));

        List<TaskResponseDTO> result = plannerService.getTasksByDayPlanId(10L);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().id()).isEqualTo(100L);
        assertThat(result.getFirst().title()).isEqualTo("Write tests");
    }

    @Test
    void updateTask_shouldUpdateAndMapResponse() {
        DayPlanner otherDayPlan = new DayPlanner();
        otherDayPlan.setId(12L);

        TaskRequestDTO request = new TaskRequestDTO(
                12L,
                "Updated task",
                "Updated description",
                TaskStatus.DONE,
                TaskPriority.MEDIUM,
                LocalDateTime.of(2026, 3, 27, 13, 0),
                LocalDateTime.of(2026, 3, 27, 14, 0),
                LocalDateTime.of(2026, 3, 27, 14, 0),
                3
        );

        when(taskRepository.findById(100L)).thenReturn(Optional.of(task));
        when(dayPlannerRepository.findById(12L)).thenReturn(Optional.of(otherDayPlan));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TaskResponseDTO result = plannerService.updateTask(100L, request);

        assertThat(result.dayPlanId()).isEqualTo(12L);
        assertThat(result.title()).isEqualTo("Updated task");
        assertThat(result.status()).isEqualTo(TaskStatus.DONE);
        assertThat(result.sortOrder()).isEqualTo(3);
    }

    @Test
    void deleteMethods_shouldDelegateToRepositories() {
        plannerService.deleteDayPlan(10L);
        plannerService.deleteTask(100L);

        verify(dayPlannerRepository).deleteById(10L);
        verify(taskRepository).deleteById(100L);
    }
}
