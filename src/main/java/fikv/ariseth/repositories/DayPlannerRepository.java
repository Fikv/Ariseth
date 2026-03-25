package fikv.ariseth.repositories;

import fikv.ariseth.entities.DayPlanner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DayPlannerRepository extends JpaRepository<DayPlanner, Long> {

	List<DayPlanner> findByUserIdAndPlanDateBetweenOrderByPlanDateAsc(Long userId, LocalDate fromDate, LocalDate toDate);
}
