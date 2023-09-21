package ua.foxminded.muzychenko.university.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.university.dao.CourseRepository;
import ua.foxminded.muzychenko.university.dao.TeacherRepository;
import ua.foxminded.muzychenko.university.dao.exception.CourseNotFoundException;
import ua.foxminded.muzychenko.university.dao.exception.UserNotFoundException;
import ua.foxminded.muzychenko.university.dto.profile.CourseInfo;
import ua.foxminded.muzychenko.university.dto.profile.TeacherProfile;
import ua.foxminded.muzychenko.university.dto.request.PasswordChangeRequest;
import ua.foxminded.muzychenko.university.dto.request.UserLoginRequest;
import ua.foxminded.muzychenko.university.dto.request.UserRegistrationRequest;
import ua.foxminded.muzychenko.university.entity.Course;
import ua.foxminded.muzychenko.university.entity.Teacher;
import ua.foxminded.muzychenko.university.service.mapper.TeacherProfileMapper;
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
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;
    private final RequestValidator requestValidator;
    private final PasswordEncoder passwordEncoder;
    private final PasswordValidator passwordValidator;
    private final TeacherProfileMapper teacherProfileMapper;

    @Transactional(readOnly = true)
    public TeacherProfile findTeacherById(UUID id) {
        Teacher teacher = teacherRepository.findById(id).orElseThrow(UserNotFoundException::new);
        Set<Course> courses = courseRepository.findUsersCourses(id);
        return teacherProfileMapper.mapTeacherEntityToProfile(teacher, courses);
    }

    @Transactional(readOnly = true)
    public List<TeacherProfile> findTeachersByCourse(String nameOfCourse) {
        return getTeacherProfiles(teacherRepository.findByCourses_CourseName(nameOfCourse));
    }

    @Transactional
    public void deleteTeacher(String email) {
        teacherRepository.deleteById(getTeacherIdByEmail(email));
    }

    @Transactional
    public void excludeTeacherFromCourse(String email, String nameOfCourse) {
        Teacher teacher = teacherRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

        teacher.getCourses().remove(courseRepository.findByCourseName(nameOfCourse).orElseThrow(CourseNotFoundException::new));

        teacherRepository.save(teacher);
    }

    @Transactional
    public void addTeacherToCourse(String email, String nameOfCourse) {
        Teacher teacher = teacherRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

        teacher.getCourses().add(courseRepository.findByCourseName(nameOfCourse).orElseThrow(CourseNotFoundException::new));

        teacherRepository.save(teacher);
    }

    @Transactional
    public void changePassword(PasswordChangeRequest passwordChangeRequest) {
        Teacher teacher = teacherRepository.findByEmail(passwordChangeRequest.getEmail()).orElseThrow(BadCredentialsException::new);

        passwordValidator.validatePasswordChangeRequest(passwordChangeRequest);

        teacher.setPassword(passwordChangeRequest.getNewPassword());

        teacherRepository.save(teacher);
    }

    @Transactional
    public TeacherProfile login(UserLoginRequest userLoginRequest) {
        String email = userLoginRequest.getEmail();

        Teacher teacher = teacherRepository.findByEmail(email).orElseThrow(BadCredentialsException::new);

        requestValidator.validateUserLoginRequest(userLoginRequest, teacher.getPassword(), email);

        Set<Course> teacherCourses = courseRepository.findUsersCourses(teacher.getUserId());

        Set<CourseInfo> courseInfoSet = teacherCourses.stream()
            .map(course -> new CourseInfo(course.getCourseId().toString(), course.getCourseName(), course.getCourseDescription()))
            .collect(Collectors.toSet());

        return new TeacherProfile(
            teacher.getUserId().toString(),
            teacher.getFirstName(),
            teacher.getLastName(),
            teacher.getEmail(),
            courseInfoSet
        );
    }

    @Transactional
    public void register(UserRegistrationRequest userRegistrationRequest) {
        requestValidator.validateUserRegistrationRequest(userRegistrationRequest);
        teacherRepository.save(
            new Teacher(
                UUID.randomUUID(),
                userRegistrationRequest.getFirstName(),
                userRegistrationRequest.getLastName(),
                userRegistrationRequest.getEmail(),
                passwordEncoder.encode(userRegistrationRequest.getPassword())
            )
        );
    }

    @Transactional(readOnly = true)
    public TeacherProfile findTeacherByEmail(String email) {
        Teacher teacher = teacherRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        Set<Course> courses = courseRepository.findUsersCourses(teacher.getUserId());
        return teacherProfileMapper.mapTeacherEntityToProfile(teacher, courses);
    }

    @Transactional(readOnly = true)
    public Page<TeacherProfile> findAll(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<Teacher> teacherPage = teacherRepository.findAll(pageable);
        return teacherPage.map(teacher -> teacherProfileMapper.mapTeacherEntityToProfile(teacher, teacher.getCourses()));
    }

    private UUID getTeacherIdByEmail(String email) {
        return teacherRepository.findByEmail(email).orElseThrow(UserNotFoundException::new).getUserId();
    }

    private List<TeacherProfile> getTeacherProfiles(List<Teacher> students) {
        List<TeacherProfile> teacherProfiles = new ArrayList<>();

        for (Teacher teacher : students) {
            teacherProfiles.add(teacherProfileMapper.mapTeacherEntityToProfile(
                teacher,
                courseRepository.findUsersCourses(teacher.getUserId())
            ));
        }
        return teacherProfiles;
    }
}
