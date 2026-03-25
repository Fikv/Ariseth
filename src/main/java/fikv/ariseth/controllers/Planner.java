package fikv.ariseth.controllers;

import fikv.ariseth.dtos.DayPlannerRequestDTO;
import fikv.ariseth.dtos.DayPlannerResponseDTO;
import fikv.ariseth.dtos.TaskRequestDTO;
import fikv.ariseth.dtos.TaskResponseDTO;
import fikv.ariseth.services.PlannerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/planner")
public class Planner {

	private final PlannerService plannerService;

	public Planner(PlannerService plannerService) {
		this.plannerService = plannerService;
	}

	@PostMapping("/day-plans")
	public DayPlannerResponseDTO createDayPlan(@RequestBody DayPlannerRequestDTO requestDTO) {
		return plannerService.createDayPlan(requestDTO);
	}

	@GetMapping("/day-plans/{id}")
	public DayPlannerResponseDTO getDayPlanById(@PathVariable Long id) {
		return plannerService.getDayPlanById(id);
	}

	@GetMapping("/day-plans")
	public List<DayPlannerResponseDTO> getDayPlans(
			@RequestParam Long userId,
			@RequestParam LocalDate fromDate,
			@RequestParam LocalDate toDate) {
		return plannerService.getDayPlans(userId, fromDate, toDate);
	}

	@PutMapping("/day-plans/{id}")
	public DayPlannerResponseDTO updateDayPlan(@PathVariable Long id, @RequestBody DayPlannerRequestDTO requestDTO) {
		return plannerService.updateDayPlan(id, requestDTO);
	}

	@DeleteMapping("/day-plans/{id}")
	public ResponseEntity<Void> deleteDayPlan(@PathVariable Long id) {
		plannerService.deleteDayPlan(id);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/tasks")
	public TaskResponseDTO createTask(@RequestBody TaskRequestDTO requestDTO) {
		return plannerService.createTask(requestDTO);
	}

	@GetMapping("/tasks/{id}")
	public TaskResponseDTO getTaskById(@PathVariable Long id) {
		return plannerService.getTaskById(id);
	}

	@GetMapping("/tasks/day-plan/{dayPlanId}")
	public List<TaskResponseDTO> getTasksByDayPlanId(@PathVariable Long dayPlanId) {
		return plannerService.getTasksByDayPlanId(dayPlanId);
	}

	@PutMapping("/tasks/{id}")
	public TaskResponseDTO updateTask(@PathVariable Long id, @RequestBody TaskRequestDTO requestDTO) {
		return plannerService.updateTask(id, requestDTO);
	}

	@DeleteMapping("/tasks/{id}")
	public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
		plannerService.deleteTask(id);
		return ResponseEntity.noContent().build();
	}

}
