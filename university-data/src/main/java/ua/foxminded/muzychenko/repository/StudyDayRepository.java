package ua.foxminded.muzychenko.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.muzychenko.entity.StudyDay;
import ua.foxminded.muzychenko.enums.DayOfWeek;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudyDayRepository extends JpaRepository<StudyDay, UUID> {

    Optional<StudyDay> findStudyDayByDayOfWeek(DayOfWeek dayOfWeek);

}
