package fikv.ariseth.dtos;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import fikv.ariseth.entities.TaskPriority;
import fikv.ariseth.entities.TaskStatus;

public record TaskRequestDTO(
	@NotNull Long dayPlanId,
	@NotNull @Size(max = 120) String title,
	@Size(max = 1000) String description,
	@NotNull TaskStatus status,
	@NotNull TaskPriority priority,
	LocalDateTime startsAt,
	LocalDateTime endsAt,
	LocalDateTime completedTimestamp,
	@NotNull Integer sortOrder
) {
}
