package fikv.ariseth.dtos;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record DayPlannerRequestDTO(
	@NotNull Long userId,
	@NotNull LocalDate planDate,
	@Size(max = 500) String notes
) {
}
