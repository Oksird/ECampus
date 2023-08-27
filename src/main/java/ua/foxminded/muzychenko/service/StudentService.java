package ua.foxminded.muzychenko.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.dao.CourseDao;
import ua.foxminded.muzychenko.dao.GroupDao;
import ua.foxminded.muzychenko.dao.StudentDao;
import ua.foxminded.muzychenko.dao.exception.GroupNotFoundException;
import ua.foxminded.muzychenko.dto.profile.CourseInfo;
import ua.foxminded.muzychenko.dto.profile.GroupInfo;
import ua.foxminded.muzychenko.dto.profile.StudentProfile;
import ua.foxminded.muzychenko.dto.request.PasswordChangeRequest;
import ua.foxminded.muzychenko.dto.request.UserLoginRequest;
import ua.foxminded.muzychenko.dto.profile.UserProfile;
import ua.foxminded.muzychenko.dto.request.UserRegistrationRequest;
import ua.foxminded.muzychenko.entity.Course;
import ua.foxminded.muzychenko.entity.Group;
import ua.foxminded.muzychenko.entity.Student;
import ua.foxminded.muzychenko.entity.UserType;
import ua.foxminded.muzychenko.service.mapper.StudentProfileMapper;
import ua.foxminded.muzychenko.service.validator.PasswordValidator;
import ua.foxminded.muzychenko.service.validator.exception.BadCredentialsException;
import ua.foxminded.muzychenko.dao.exception.UserNotFoundException;
import ua.foxminded.muzychenko.service.util.PasswordEncoder;
import ua.foxminded.muzychenko.service.validator.RequestValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class StudentService {
    private final StudentDao studentDao;
    private final CourseDao courseDao;
    private final GroupDao groupDao;
    private final RequestValidator requestValidator;
    private final PasswordEncoder passwordEncoder;
    private final PasswordValidator passwordValidator;
    private final StudentProfileMapper studentProfileMapper;

    public StudentProfile findStudentById(UUID id) {
        Student student = studentDao.findById(id).orElseThrow(UserNotFoundException::new);
        Group group = groupDao.findUsersGroup(id).orElse(null);
        List<Course> courses = courseDao.findCoursesByUserIdAndUserType(id, UserType.STUDENT);
        return studentProfileMapper.mapStudentInfoToProfile(student, group, courses);
    }

    public List<StudentProfile> findAllStudents(Long pageNumber, Long pageSize) {
        List<Student> students = studentDao.findAll(pageNumber, pageSize);
        return getStudentProfiles(students);
    }

    public List<StudentProfile> findStudentsByCourse(String nameOfCourse) {
        List<Student> students = studentDao.findByCourse(nameOfCourse);
        return getStudentProfiles(students);
    }

    public List<StudentProfile> findStudentsByGroup(String nameOfGroup) {
        List<Student> students = studentDao.findByGroup(nameOfGroup);
        return getStudentProfiles(students);
    }

    @Transactional
    public UserProfile login(UserLoginRequest userLoginRequest) {
        String email = userLoginRequest.getEmail();

        Student student = studentDao.findByEmail(email).orElseThrow(BadCredentialsException::new);

        requestValidator.validateUserLoginRequest(userLoginRequest, student.getPassword(), student.getEmail());

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

    @Transactional
    public void register(UserRegistrationRequest userRegistrationRequest) {
        requestValidator.validateUserRegistrationRequest(userRegistrationRequest);
        studentDao.create(
            new Student(
                UUID.randomUUID(),
                userRegistrationRequest.getFirstName(),
                userRegistrationRequest.getLastName(),
                userRegistrationRequest.getEmail(),
                passwordEncoder.encode(userRegistrationRequest.getPassword()),
                null
            )
        );
    }

    @Transactional
    public void changePassword(PasswordChangeRequest passwordChangeRequest) {
        Student student = studentDao
            .findByEmail(passwordChangeRequest.getEmail())
            .orElseThrow(UserNotFoundException::new);

        passwordValidator.validatePasswordChangeRequest(passwordChangeRequest);

        student.setPassword(passwordChangeRequest.getNewPassword());

        studentDao.update(student.getUserId(), student);
    }

    public void deleteStudent(String email) {
        studentDao.deleteById(getStudentIdByEmail(email));
    }

    public void excludeStudentFromGroup(String email, String nameOfGroup) {
        studentDao.excludeFromGroup(getStudentIdByEmail(email), nameOfGroup);
    }

    public void excludeStudentFromCourse(String email, String nameOfCourse) {
        studentDao.excludeFromCourse(getStudentIdByEmail(email), nameOfCourse);
    }

    public void addStudentToCourse(String email, String nameOfCourse) {
        studentDao.addToCourse(getStudentIdByEmail(email), nameOfCourse);
    }

    public void addStudentToGroup(String email, String nameOfGroup) {
        studentDao.addToGroup(getStudentIdByEmail(email), nameOfGroup);
    }

    public StudentProfile findStudentByEmail(String email) {
        Student student = studentDao.findByEmail(email).orElseThrow(UserNotFoundException::new);
        Group group = groupDao.findUsersGroup(student.getUserId()).orElseThrow(GroupNotFoundException::new);
        List<Course> courses = courseDao.findCoursesByUserIdAndUserType(student.getUserId(), UserType.STUDENT);
        return studentProfileMapper.mapStudentInfoToProfile(student, group, courses);
    }

    private UUID getStudentIdByEmail(String email) {
        return studentDao.findByEmail(email).orElseThrow(UserNotFoundException::new).getUserId();
    }

    private List<StudentProfile> getStudentProfiles(List<Student> students) {
        List<StudentProfile> studentProfiles = new ArrayList<>();

        for (Student student : students) {
            studentProfiles.add(studentProfileMapper
                .mapStudentInfoToProfile(
                    student,
                    groupDao.findUsersGroup(student.getUserId()).orElse(null),
                    courseDao.findCoursesByUserIdAndUserType(student.getUserId(), UserType.STUDENT)
                )
            );
        }
        return studentProfiles;
    }
}
