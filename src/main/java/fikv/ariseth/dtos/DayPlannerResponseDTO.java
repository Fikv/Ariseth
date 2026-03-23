package fikv.ariseth.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record DayPlannerResponseDTO(
	Long id,
	Long userId,
	LocalDate planDate,
	String notes,
	LocalDateTime createdTimestamp,
	LocalDateTime updatedTimestamp,
	List<TaskResponseDTO> tasks
) {
}
