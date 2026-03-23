package fikv.ariseth.dtos;

import java.time.LocalDateTime;

import fikv.ariseth.entities.TaskPriority;
import fikv.ariseth.entities.TaskStatus;

public record TaskResponseDTO(
	Long id,
	Long dayPlanId,
	String title,
	String description,
	TaskStatus status,
	TaskPriority priority,
	LocalDateTime startsAt,
	LocalDateTime endsAt,
	LocalDateTime completedTimestamp,
	Integer sortOrder,
	LocalDateTime createdTimestamp,
	LocalDateTime updatedTimestamp
) {
}
