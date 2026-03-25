package fikv.ariseth.services;

import fikv.ariseth.dtos.DayPlannerRequestDTO;
import fikv.ariseth.dtos.DayPlannerResponseDTO;
import fikv.ariseth.dtos.TaskRequestDTO;
import fikv.ariseth.dtos.TaskResponseDTO;
import fikv.ariseth.entities.DayPlanner;
import fikv.ariseth.entities.Task;
import fikv.ariseth.entities.User;
import fikv.ariseth.repositories.DayPlannerRepository;
import fikv.ariseth.repositories.TaskRepository;
import fikv.ariseth.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class PlannerService {

	private final DayPlannerRepository dayPlannerRepository;
	private final TaskRepository taskRepository;
	private final UserRepository userRepository;

	public PlannerService(DayPlannerRepository dayPlannerRepository,
						  TaskRepository taskRepository,
						  UserRepository userRepository) {
		this.dayPlannerRepository = dayPlannerRepository;
		this.taskRepository = taskRepository;
		this.userRepository = userRepository;
	}

	public DayPlannerResponseDTO createDayPlan(DayPlannerRequestDTO requestDTO) {
		User user = userRepository.findById(requestDTO.userId())
				.orElseThrow(() -> new RuntimeException("User not found"));

		DayPlanner dayPlanner = new DayPlanner();
		dayPlanner.setUser(user);
		dayPlanner.setPlanDate(requestDTO.planDate());
		dayPlanner.setNotes(requestDTO.notes());
		dayPlanner.setCreatedTimestamp(LocalDateTime.now());
		dayPlanner.setUpdatedTimestamp(LocalDateTime.now());

		return toDayPlannerResponse(dayPlannerRepository.save(dayPlanner));
	}

	@Transactional(readOnly = true)
	public DayPlannerResponseDTO getDayPlanById(Long id) {
		DayPlanner dayPlanner = dayPlannerRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Day plan not found"));
		return toDayPlannerResponse(dayPlanner);
	}

	@Transactional(readOnly = true)
	public List<DayPlannerResponseDTO> getDayPlans(Long userId, LocalDate fromDate, LocalDate toDate) {
		return dayPlannerRepository.findByUserIdAndPlanDateBetweenOrderByPlanDateAsc(userId, fromDate, toDate)
				.stream()
				.map(this::toDayPlannerResponse)
				.toList();
	}

	public DayPlannerResponseDTO updateDayPlan(Long id, DayPlannerRequestDTO requestDTO) {
		DayPlanner dayPlanner = dayPlannerRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Day plan not found"));

		if (!dayPlanner.getUser().getId().equals(requestDTO.userId())) {
			User user = userRepository.findById(requestDTO.userId())
					.orElseThrow(() -> new RuntimeException("User not found"));
			dayPlanner.setUser(user);
		}

		dayPlanner.setPlanDate(requestDTO.planDate());
		dayPlanner.setNotes(requestDTO.notes());
		dayPlanner.setUpdatedTimestamp(LocalDateTime.now());

		return toDayPlannerResponse(dayPlannerRepository.save(dayPlanner));
	}

	public void deleteDayPlan(Long id) {
		dayPlannerRepository.deleteById(id);
	}

	public TaskResponseDTO createTask(TaskRequestDTO requestDTO) {
		DayPlanner dayPlanner = dayPlannerRepository.findById(requestDTO.dayPlanId())
				.orElseThrow(() -> new RuntimeException("Day plan not found"));

		Task task = new Task();
		task.setDayPlan(dayPlanner);
		task.setTitle(requestDTO.title());
		task.setDescription(requestDTO.description());
		task.setStatus(requestDTO.status());
		task.setPriority(requestDTO.priority());
		task.setStartsAt(requestDTO.startsAt());
		task.setEndsAt(requestDTO.endsAt());
		task.setCompletedTimestamp(requestDTO.completedTimestamp());
		task.setSortOrder(requestDTO.sortOrder());
		task.setCreatedTimestamp(LocalDateTime.now());
		task.setUpdatedTimestamp(LocalDateTime.now());

		return toTaskResponse(taskRepository.save(task));
	}

	@Transactional(readOnly = true)
	public TaskResponseDTO getTaskById(Long id) {
		Task task = taskRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Task not found"));
		return toTaskResponse(task);
	}

	@Transactional(readOnly = true)
	public List<TaskResponseDTO> getTasksByDayPlanId(Long dayPlanId) {
		return taskRepository.findByDayPlanIdOrderBySortOrderAsc(dayPlanId)
				.stream()
				.map(this::toTaskResponse)
				.toList();
	}

	public TaskResponseDTO updateTask(Long id, TaskRequestDTO requestDTO) {
		Task task = taskRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Task not found"));

		DayPlanner dayPlanner = dayPlannerRepository.findById(requestDTO.dayPlanId())
				.orElseThrow(() -> new RuntimeException("Day plan not found"));

		task.setDayPlan(dayPlanner);
		task.setTitle(requestDTO.title());
		task.setDescription(requestDTO.description());
		task.setStatus(requestDTO.status());
		task.setPriority(requestDTO.priority());
		task.setStartsAt(requestDTO.startsAt());
		task.setEndsAt(requestDTO.endsAt());
		task.setCompletedTimestamp(requestDTO.completedTimestamp());
		task.setSortOrder(requestDTO.sortOrder());
		task.setUpdatedTimestamp(LocalDateTime.now());

		return toTaskResponse(taskRepository.save(task));
	}

	public void deleteTask(Long id) {
		taskRepository.deleteById(id);
	}

	private DayPlannerResponseDTO toDayPlannerResponse(DayPlanner dayPlanner) {
		List<TaskResponseDTO> tasks = dayPlanner.getTasks()
				.stream()
				.map(this::toTaskResponse)
				.toList();

		return new DayPlannerResponseDTO(
				dayPlanner.getId(),
				dayPlanner.getUser().getId(),
				dayPlanner.getPlanDate(),
				dayPlanner.getNotes(),
				dayPlanner.getCreatedTimestamp(),
				dayPlanner.getUpdatedTimestamp(),
				tasks
		);
	}

	private TaskResponseDTO toTaskResponse(Task task) {
		return new TaskResponseDTO(
				task.getId(),
				task.getDayPlan().getId(),
				task.getTitle(),
				task.getDescription(),
				task.getStatus(),
				task.getPriority(),
				task.getStartsAt(),
				task.getEndsAt(),
				task.getCompletedTimestamp(),
				task.getSortOrder(),
				task.getCreatedTimestamp(),
				task.getUpdatedTimestamp()
		);
	}

}
