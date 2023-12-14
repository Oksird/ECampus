package ua.foxminded.muzychenko.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.muzychenko.entity.StudyWeek;
import ua.foxminded.muzychenko.enums.WeekNumber;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudyWeekRepository extends JpaRepository<StudyWeek, UUID> {

    Optional<StudyWeek> findStudyWeekByWeekNumber(WeekNumber weekNumber);

}
