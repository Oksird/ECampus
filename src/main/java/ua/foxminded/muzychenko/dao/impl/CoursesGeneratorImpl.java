package ua.foxminded.muzychenko.dao.impl;

import java.util.ArrayList;
import java.util.List;
import ua.foxminded.muzychenko.dao.CourseDao;
import ua.foxminded.muzychenko.dao.CoursesGenerator;
import ua.foxminded.muzychenko.entity.CourseEntity;

public class CoursesGeneratorImpl implements CoursesGenerator {

    private final CourseDao courseDao;
    static final List<String> COURSE_NAMES = new ArrayList<>(
        List.of("Science", "Math", "English", "History", "Art", "Music", "Astronomy",
            "Physical Education", "Reading", "Geography"));
    static final List<String> COURSE_DESCRIPTIONS = new ArrayList<>(List.of(
        "Systematic endeavor that builds and organizes knowledge in the form of testable"
            + " explanations and predictions about the universe",
        "Area of knowledge that includes the topics of numbers, formulas and related structures,"
            + " shapes and the spaces in which they are contained,"
            + " and quantities and their changes",
        "Study of literature, media and language in which students critically and creatively engage"
            + " with a variety of texts in all language modes",
        "Academic discipline which uses narrative to describe, examine, question, and analyze"
            + " past events, and investigate their patterns of cause and effect",
        "Collection of disciplines which produce artworks (art as objects) that are compelled"
            + " by a personal drive (art as activity) and convey a message, mood, or symbolism"
            + " for the perceive to interpret (art as experience)",
        "Music as a school subject typically involves the study of various aspects of music,"
            + " including music theory, history, composition, and performance",
        "Scientific study of celestial objects such as stars, planets, galaxies, and other"
            + " phenomena that exist outside of the Earth's atmosphere",
        "Provides cognitive content and instruction designed to develop motor skills, knowledge,"
            + " and behaviors for physical activity and physical fitness",
        "Involves the development of critical thinking skills, such as analyzing, synthesizing,"
            + " and evaluating information. Students learn to distinguish between fact and opinion,"
            + " draw inferences and conclusions, and evaluate the credibility of sources.",
        "Study of the Earth's physical features, natural environment, and human societies and their"
            + " relationships with the physical world"));

    public CoursesGeneratorImpl(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    @Override
    public List<CourseEntity> generateData() {
        List<CourseEntity> courses = new ArrayList<>();
        for (int i = 0; i < COURSE_NAMES.size(); i++) {
            courses.add(new CourseEntity(i + 1L, COURSE_NAMES.get(i), COURSE_DESCRIPTIONS.get(i)));
        }
        return courses;
    }

    @Override
    public void insertCourses(List<CourseEntity> courses) {
        courseDao.createAll(courses);
    }
}
