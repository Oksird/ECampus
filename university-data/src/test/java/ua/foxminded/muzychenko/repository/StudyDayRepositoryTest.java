package ua.foxminded.muzychenko.repository;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.DataConfiguration;
import ua.foxminded.muzychenko.DataTestConfig;
import ua.foxminded.muzychenko.entity.StudyDay;
import ua.foxminded.muzychenko.enums.DayOfWeek;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = DataConfiguration.class)
@SpringBootTest(classes = StudyDayRepository.class)
@Import(DataTestConfig.class)
@Transactional
class StudyDayRepositoryTest {

    @Autowired
    private StudyDayRepository studyDayRepository;

    @Test
    void findStudyDayByDayOfWeekShouldReturnCorrectEntity() {
        StudyDay actualStudyDay = studyDayRepository.findStudyDayByDayOfWeek(DayOfWeek.MONDAY)
            .orElseThrow(EntityNotFoundException::new);

        StudyDay expectedStudyDay = studyDayRepository.findAll()
            .stream()
            .filter(studyDay -> studyDay.getDayOfWeek().equals(DayOfWeek.MONDAY))
            .findAny()
            .orElseThrow(EntityNotFoundException::new);

        assertEquals(expectedStudyDay, actualStudyDay);
    }
}
