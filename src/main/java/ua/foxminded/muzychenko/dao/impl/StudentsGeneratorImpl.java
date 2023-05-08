package ua.foxminded.muzychenko.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import ua.foxminded.muzychenko.dao.StudentDao;
import ua.foxminded.muzychenko.dao.StudentsGenerator;
import ua.foxminded.muzychenko.entity.StudentEntity;

public class StudentsGeneratorImpl implements StudentsGenerator {

    private static final int COUNT_OF_STUDENTS = 200;
    private static final int COUNT_OF_GROUPS = 10;
    private final StudentDao studentDao;
    private final Random random;
    private static final List<String> FIRST_NAMES = new ArrayList<>(
        List.of("Brittany", "Ivan", "Shannon", "Valerie", "Juan", "Cleo", "Landry", "Melani",
            "Kelly", "Luella", "Lyric", "Jon", "Rex", "Eddie", "Nixon", "Kendall", "Arthur",
            "Harvey", "Vincent", "Le"));
    private static final List<String> LAST_NAMES = new ArrayList<>(
        List.of("Porter", "Hendrix", "Hogan", "Vance", "Page", "House", "Miles", "Cunningham",
            "Booker", "Barrett", "Larsen", "Reyes", "Brown", "Stewart", "Archer", "Santos", "Wade",
            "Turing", "Smith", "Walker"));

    public StudentsGeneratorImpl(StudentDao studentDao, Random random) {
        this.studentDao = studentDao;
        this.random = random;
    }

    @Override
    public List<StudentEntity> generateData() {
        List<StudentEntity> students = new ArrayList<>();
        for (int i = 0; i < COUNT_OF_STUDENTS; i++) {
            students.add(
                new StudentEntity(
                    i + 1L,
                    random.nextInt(COUNT_OF_GROUPS) + 1L,
                    FIRST_NAMES.get(random.nextInt(FIRST_NAMES.size())),
                    LAST_NAMES.get(random.nextInt(LAST_NAMES.size()))));
        }
        return students;
    }

    @Override
    public void insertStudents(List<StudentEntity> students) {
        List<String> courseNames = CoursesGeneratorImpl.COURSE_NAMES;
        studentDao.createAll(students);
        students.forEach(
            studentEntity -> studentDao.addToCourse(studentEntity,
                courseNames.get(random.nextInt(courseNames.size()))));
    }
}
