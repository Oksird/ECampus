package ua.foxminded.muzychenko.university.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.university.dao.CourseRepository;
import ua.foxminded.muzychenko.university.dao.GroupRepository;
import ua.foxminded.muzychenko.university.dao.StudentRepository;
import ua.foxminded.muzychenko.university.dao.exception.CourseNotFoundException;
import ua.foxminded.muzychenko.university.dao.exception.GroupNotFoundException;
import ua.foxminded.muzychenko.university.dao.exception.UserNotFoundException;
import ua.foxminded.muzychenko.university.dto.profile.CourseInfo;
import ua.foxminded.muzychenko.university.dto.profile.GroupInfo;
import ua.foxminded.muzychenko.university.dto.profile.StudentProfile;
import ua.foxminded.muzychenko.university.dto.profile.UserProfile;
import ua.foxminded.muzychenko.university.dto.request.PasswordChangeRequest;
import ua.foxminded.muzychenko.university.dto.request.UserLoginRequest;
import ua.foxminded.muzychenko.university.dto.request.UserRegistrationRequest;
import ua.foxminded.muzychenko.university.entity.Course;
import ua.foxminded.muzychenko.university.entity.Group;
import ua.foxminded.muzychenko.university.entity.Student;
import ua.foxminded.muzychenko.university.service.mapper.StudentProfileMapper;
import ua.foxminded.muzychenko.university.service.util.PasswordEncoder;
import ua.foxminded.muzychenko.university.service.validator.PasswordValidator;
import ua.foxminded.muzychenko.university.service.validator.RequestValidator;
import ua.foxminded.muzychenko.university.service.validator.exception.BadCredentialsException;

import java.util.ArrayList;
import java.util.List;
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

    @Transactional(readOnly = true)
    public StudentProfile findStudentById(UUID id) {
        Student student = studentRepository.findById(id).orElseThrow(UserNotFoundException::new);
        Group group = groupRepository.findUsersGroup(id).orElse(null);
        Set<Course> courses = courseRepository.findUsersCourses(id);
        return studentProfileMapper.mapStudentInfoToProfile(student, group, courses);
    }

    @Transactional(readOnly = true)
    public List<StudentProfile> findStudentsByCourse(String nameOfCourse) {
        List<Student> students = studentRepository.findByCourses_CourseName(nameOfCourse);
        return getStudentProfiles(students);
    }

    @Transactional(readOnly = true)
    public List<StudentProfile> findStudentsByGroup(String nameOfGroup) {
        List<Student> students = studentRepository.findByGroup_GroupName(nameOfGroup);
        return getStudentProfiles(students);
    }

    @Transactional
    public UserProfile login(UserLoginRequest userLoginRequest) {
        String email = userLoginRequest.getEmail();

        Student student = studentRepository.findByEmail(email).orElseThrow(BadCredentialsException::new);

        requestValidator.validateUserLoginRequest(userLoginRequest, student.getPassword(), student.getEmail());

        Group group = groupRepository.findUsersGroup(student.getUserId()).orElseThrow(GroupNotFoundException::new);

        GroupInfo groupInfo = new GroupInfo(group.getGroupId().toString(), group.getGroupName());

        Set<Course> studentCourses = courseRepository.findUsersCourses(student.getUserId());

        Set<CourseInfo> courseInfoSet = studentCourses.stream()
            .map(course -> new CourseInfo(course.getCourseId().toString(), course.getCourseName(), course.getCourseDescription()))
            .collect(Collectors.toSet());

        return new StudentProfile(
            student.getUserId().toString(),
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

    @Transactional
    public void deleteStudent(String email) {
        studentRepository.deleteById(getStudentIdByEmail(email));
    }

    @Transactional
    public void excludeStudentFromGroup(String email) {
        Student student = studentRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        student.setGroup(null);
        studentRepository.save(student);
    }

    @Transactional
    public void excludeStudentFromCourse(String email, String nameOfCourse) {
        Student student = studentRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

        student.getCourses().remove(courseRepository.findByCourseName(nameOfCourse).orElseThrow(CourseNotFoundException::new));

        studentRepository.save(student);
    }

    @Transactional
    public void addStudentToCourse(String email, String nameOfCourse) {
        Student student = studentRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        student.getCourses().add(courseRepository.findByCourseName(nameOfCourse).orElseThrow(CourseNotFoundException::new));

        studentRepository.save(student);
    }

    @Transactional
    public void addStudentToGroup(String email, String nameOfGroup) {
        Student student = studentRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

        student.setGroup(groupRepository.findByGroupName(nameOfGroup).orElseThrow(GroupNotFoundException::new));

        studentRepository.save(student);
    }

    @Transactional(readOnly = true)
    public StudentProfile findStudentByEmail(String email) {
        Student student = studentRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        Group group = groupRepository.findUsersGroup(student.getUserId()).orElseThrow(GroupNotFoundException::new);
        Set<Course> courses = courseRepository.findUsersCourses(student.getUserId());
        return studentProfileMapper.mapStudentInfoToProfile(student, group, courses);
    }

    @Transactional(readOnly = true)
    public Page<StudentProfile> findAll(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<Student> studentPage = studentRepository.findAll(pageable);
        return studentPage.map(
            student -> studentProfileMapper
                .mapStudentInfoToProfile(
                    student,
                    student.getGroup(),
                    student.getCourses()
                )
        );
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
