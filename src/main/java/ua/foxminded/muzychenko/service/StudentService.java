package ua.foxminded.muzychenko.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.dao.CourseRepository;
import ua.foxminded.muzychenko.dao.GroupRepository;
import ua.foxminded.muzychenko.dao.StudentRepository;
import ua.foxminded.muzychenko.dao.exception.CourseNotFoundException;
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
import ua.foxminded.muzychenko.service.mapper.StudentProfileMapper;
import ua.foxminded.muzychenko.service.validator.PasswordValidator;
import ua.foxminded.muzychenko.service.validator.exception.BadCredentialsException;
import ua.foxminded.muzychenko.dao.exception.UserNotFoundException;
import ua.foxminded.muzychenko.service.util.PasswordEncoder;
import ua.foxminded.muzychenko.service.validator.RequestValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final GroupRepository groupRepository;
    private final RequestValidator requestValidator;
    private final PasswordEncoder passwordEncoder;
    private final PasswordValidator passwordValidator;
    private final StudentProfileMapper studentProfileMapper;

    public StudentProfile findStudentById(UUID id) {
        Student student = studentRepository.findById(id).orElseThrow(UserNotFoundException::new);
        Group group = groupRepository.findUsersGroup(id).orElse(null);
        Set<Course> courses = courseRepository.findUsersCourses(id);
        return studentProfileMapper.mapStudentInfoToProfile(student, group, courses);
    }

    public List<StudentProfile> findAllStudents(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<Student> students = studentRepository.findAll(pageable).getContent();
        return getStudentProfiles(students);
    }

    public List<StudentProfile> findStudentsByCourse(String nameOfCourse) {
        List<Student> students = studentRepository.findByCourses_CourseName(nameOfCourse);
        return getStudentProfiles(students);
    }

    public List<StudentProfile> findStudentsByGroup(String nameOfGroup) {
        List<Student> students = studentRepository.findByGroup_GroupName(nameOfGroup);
        return getStudentProfiles(students);
    }

    @Transactional
    public UserProfile login(UserLoginRequest userLoginRequest) {
        String email = userLoginRequest.getEmail();

        Student student = studentRepository.findByEmail(email).orElseThrow(BadCredentialsException::new);

        requestValidator.validateUserLoginRequest(userLoginRequest, student.getPassword(), student.getEmail());

        GroupInfo groupInfo = new GroupInfo(Objects.requireNonNull(
            groupRepository.findUsersGroup(student.getUserId()).orElse(null)).getGroupName());

        Set<Course> studentCourses = courseRepository.findUsersCourses(student.getUserId());

        Set<CourseInfo> courseInfoSet = studentCourses.stream()
            .map(course -> new CourseInfo(course.getCourseName(), course.getCourseDescription()))
            .collect(Collectors.toSet());

        return new StudentProfile(
            student.getFirstName(),
            student.getLastName(),
            student.getEmail(),
            groupInfo,
            courseInfoSet
            );
    }

    @Transactional
    public void register(UserRegistrationRequest userRegistrationRequest) {
        requestValidator.validateUserRegistrationRequest(userRegistrationRequest);
        studentRepository.save(
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
        Student student = studentRepository
            .findByEmail(passwordChangeRequest.getEmail())
            .orElseThrow(UserNotFoundException::new);

        passwordValidator.validatePasswordChangeRequest(passwordChangeRequest);

        student.setPassword(passwordChangeRequest.getNewPassword());

        studentRepository.save(student);
    }

    public void deleteStudent(String email) {
        studentRepository.deleteById(getStudentIdByEmail(email));
    }

    public void excludeStudentFromGroup(String email) {
        Student student = studentRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        student.setGroup(null);
        studentRepository.save(student);
    }

    public void excludeStudentFromCourse(String email, String nameOfCourse) {
        Student student = studentRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

        student.getCourses().remove(courseRepository.findByCourseName(nameOfCourse).orElseThrow(CourseNotFoundException::new));

        studentRepository.save(student);
    }

    public void addStudentToCourse(String email, String nameOfCourse) {
        Student student = studentRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        student.getCourses().add(courseRepository.findByCourseName(nameOfCourse).orElseThrow(CourseNotFoundException::new));

        studentRepository.save(student);
    }

    public void addStudentToGroup(String email, String nameOfGroup) {
        Student student = studentRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

        student.setGroup(groupRepository.findByGroupName(nameOfGroup).orElseThrow(GroupNotFoundException::new));

        studentRepository.save(student);
    }

    public StudentProfile findStudentByEmail(String email) {
        Student student = studentRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        Group group = groupRepository.findUsersGroup(student.getUserId()).orElseThrow(GroupNotFoundException::new);
        Set<Course> courses = courseRepository.findUsersCourses(student.getUserId());
        return studentProfileMapper.mapStudentInfoToProfile(student, group, courses);
    }

    private UUID getStudentIdByEmail(String email) {
        return studentRepository.findByEmail(email).orElseThrow(UserNotFoundException::new).getUserId();
    }

    private List<StudentProfile> getStudentProfiles(List<Student> students) {
        List<StudentProfile> studentProfiles = new ArrayList<>();

        for (Student student : students) {
            studentProfiles.add(studentProfileMapper
                .mapStudentInfoToProfile(
                    student,
                    groupRepository.findUsersGroup(student.getUserId()).orElse(null),
                    courseRepository.findUsersCourses(student.getUserId())
                )
            );
        }
        return studentProfiles;
    }
}
