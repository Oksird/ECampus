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
import ua.foxminded.muzychenko.entity.StudyWeek;
import ua.foxminded.muzychenko.enums.WeekNumber;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = DataConfiguration.class)
@SpringBootTest(classes = StudyWeekRepository.class)
@Import(DataTestConfig.class)
@Transactional
class StudyWeekRepositoryTest {

    @Autowired
    private StudyWeekRepository studyWeekRepository;

    @Test
    void findStudyWeekByWeekNumberShouldReturnCorrectEntity() {
         StudyWeek expectedStudyWeek = studyWeekRepository.findStudyWeekByWeekNumber(WeekNumber.FIRST)
            .orElseThrow(EntityNotFoundException::new);

         StudyWeek actualStudyWeek = studyWeekRepository.findAll()
             .stream()
             .filter(studyWeek -> studyWeek.getWeekNumber().equals(WeekNumber.FIRST))
             .findAny()
             .orElseThrow(EntityNotFoundException::new);

         assertEquals(expectedStudyWeek, actualStudyWeek);
    }
}