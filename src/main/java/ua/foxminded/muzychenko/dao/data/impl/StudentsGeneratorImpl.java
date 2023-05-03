package ua.foxminded.muzychenko.dao.data.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import ua.foxminded.muzychenko.dao.StudentDao;
import ua.foxminded.muzychenko.dao.data.StudentsGenerator;
import ua.foxminded.muzychenko.entity.StudentEntity;

public class StudentsGeneratorImpl implements StudentsGenerator {

    private static final int COUNT_OF_STUDENTS = 200;
    private final StudentDao studentDao;
    private final Random random = new Random();
    private final List<String> firstNames = new ArrayList<>(
        List.of("Brittany", "Ivan", "Shannon", "Valerie", "Juan", "Cleo", "Landry", "Melani",
            "Kelly", "Luella", "Lyric", "Jon", "Rex", "Eddie", "Nixon", "Kendall", "Arthur",
            "Harvey", "Vincent", "Le"));
    private final List<String> lastNames = new ArrayList<>(
        List.of("Porter", "Hendrix", "Hogan", "Vance", "Page", "House", "Miles", "Cunningham",
            "Booker", "Barrett", "Larsen", "Reyes", "Brown", "Stewart", "Archer", "Santos", "Wade",
            "Turing", "Smith", "Walker"));

    public StudentsGeneratorImpl(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    @Override
    public List<StudentEntity> generateData() {
        List<StudentEntity> students = new ArrayList<>();
        for (int i = 0; i < COUNT_OF_STUDENTS; i++) {
            students.add(
                new StudentEntity(
                    i + 1L,
                    random.nextInt(10) + 1L,
                    firstNames.get(random.nextInt(20)),
                    lastNames.get(random.nextInt(20))));
        }
        return students;
    }

    @Override
    public void insertStudents(List<StudentEntity> students) {
        List<String> courseNames = CoursesGeneratorImpl.courseNames;
        generateData().forEach(studentDao::create);
        generateData().forEach(
            studentEntity -> studentDao.addToCourse(studentEntity,
                courseNames.get(random.nextInt(courseNames.size()))));
    }
}
