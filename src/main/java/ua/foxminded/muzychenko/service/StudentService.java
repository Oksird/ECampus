package ua.foxminded.muzychenko.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import ua.foxminded.muzychenko.dao.CourseDao;
import ua.foxminded.muzychenko.dao.GroupDao;
import ua.foxminded.muzychenko.dao.StudentDao;
import ua.foxminded.muzychenko.dto.CourseInfo;
import ua.foxminded.muzychenko.dto.GroupInfo;
import ua.foxminded.muzychenko.dto.StudentProfile;
import ua.foxminded.muzychenko.dto.UserLoginRequest;
import ua.foxminded.muzychenko.dto.UserProfile;
import ua.foxminded.muzychenko.dto.UserRegistrationRequest;
import ua.foxminded.muzychenko.entity.Course;
import ua.foxminded.muzychenko.entity.Student;
import ua.foxminded.muzychenko.entity.UserType;
import ua.foxminded.muzychenko.exception.BadCredentialsException;
import ua.foxminded.muzychenko.exception.PasswordMismatchException;
import ua.foxminded.muzychenko.dao.exception.UserNotFoundException;
import ua.foxminded.muzychenko.service.util.PasswordEncoder;
import ua.foxminded.muzychenko.service.validator.UserValidator;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class StudentService {
    private final StudentDao studentDao;
    private final CourseDao courseDao;
    private final GroupDao groupDao;
    private final UserValidator userValidator;
    private final PasswordEncoder passwordEncoder;

    public Student findStudentById(UUID id) {
        return studentDao.findById(id).orElse(null);
    }

    public List<Student> findAllStudents() {
        return studentDao.findAll(1L, 1L);
    }

    public List<Student> findStudentsByCourse(String nameOfCourse) {
        return studentDao.findByCourse(nameOfCourse);
    }

    public List<Student> findStudentsByGroup(String nameOfGroup) {
        return studentDao.findByGroup(nameOfGroup);
    }

    public UserProfile login(UserLoginRequest userLoginRequest) {
        String email = userLoginRequest.getEmail();
        String password = userLoginRequest.getPassword();

        Optional<Student> optionalStudent = studentDao.findByEmail(email);

        if (optionalStudent.isEmpty()) {
            throw new UserNotFoundException();
        }

        if (!passwordEncoder.matches(password, optionalStudent.get().getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        Student student = optionalStudent.get();
        GroupInfo groupInfo = new GroupInfo(Objects.requireNonNull(
            groupDao.findUsersGroup(student.getUserId()).orElse(null)).getGroupName());
        List<Course> studentCourses = courseDao.findCoursesByUserIdAndUserType(student.getUserId(), UserType.STUDENT);
        List<CourseInfo> courseInfoList = studentCourses.stream()
            .map(course -> new CourseInfo(course.getCourseName(), course.getCourseDescription()))
            .toList();
        return new StudentProfile(
            student.getFirstName(),
            student.getLastName(),
            student.getEmail(),
            groupInfo,
            courseInfoList
            );
    }

    public void register(@NonNull UserRegistrationRequest userRegistrationRequest) {
        userValidator.validateUserRegistrationRequest(userRegistrationRequest);
        studentDao.create(new Student(UUID.randomUUID(), userRegistrationRequest.getFirstName(), userRegistrationRequest.getLastName(), userRegistrationRequest.getEmail(), passwordEncoder.encode(userRegistrationRequest.getPassword()), null));

    }

    public void deleteStudent(UUID id) {
        studentDao.deleteById(id);
    }

    public void deleteStudentFromGroup(UUID studentId, String nameOfGroup) {
        studentDao.excludeFromGroup(studentId, nameOfGroup);
    }

    public void deleteStudentFromCourse(UUID studentId, String nameOfCourse) {
        studentDao.excludeFromCourse(studentId, nameOfCourse);
    }

    public void addStudentToCourse(UUID studentID, String nameOfCourse) {
        studentDao.addToCourse(studentID, nameOfCourse);
    }

    public void addStudentToGroup(UUID studentId, String nameOfGroup) {
        studentDao.addToGroup(studentId, nameOfGroup);
    }

    public void changePassword(String email, String oldPassword, String newPassword, String repeatNewPassword) {
        Student student = studentDao.findByEmail(email).orElseThrow(UserNotFoundException::new);
        if (!passwordEncoder.matches(oldPassword, student.getPassword())) {
            throw new BadCredentialsException("Wrong password");
        }
        if (!newPassword.equals(repeatNewPassword)) {
            throw new PasswordMismatchException();
        }
        student.setPassword(newPassword);
        studentDao.update(student.getUserId(), student);
    }
}
