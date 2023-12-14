package ua.foxminded.muzychenko.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ua.foxminded.muzychenko.entity.LessonTime;
import ua.foxminded.muzychenko.repository.LessonTimeRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class LessonTimeService {

    private LessonTimeRepository lessonTimeRepository;

    public List<LessonTime> lessonTimes() {
        return lessonTimeRepository.findAll();
    }

}
