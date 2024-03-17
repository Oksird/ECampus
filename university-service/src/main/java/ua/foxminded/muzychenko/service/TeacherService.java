package ua.foxminded.muzychenko.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.dto.profile.PendingUserProfile;
import ua.foxminded.muzychenko.dto.profile.TeacherProfile;
import ua.foxminded.muzychenko.dto.request.PasswordChangeRequest;
import ua.foxminded.muzychenko.entity.Course;
import ua.foxminded.muzychenko.entity.PendingUser;
import ua.foxminded.muzychenko.entity.Teacher;
import ua.foxminded.muzychenko.exception.CourseNotFoundException;
import ua.foxminded.muzychenko.exception.UserNotFoundException;
import ua.foxminded.muzychenko.repository.CourseRepository;
import ua.foxminded.muzychenko.repository.PendingUserRepository;
import ua.foxminded.muzychenko.repository.TeacherRepository;
import ua.foxminded.muzychenko.service.mapper.TeacherProfileMapper;
import ua.foxminded.muzychenko.service.validator.RequestValidator;
import ua.foxminded.muzychenko.service.validator.exception.BadCredentialsException;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;
    private final TeacherProfileMapper teacherProfileMapper;
    private final PendingUserRepository pendingUserRepository;
    private final RequestValidator requestValidator;

    @Transactional
    public void createTeacherFromPendingUser(PendingUserProfile pendingUserProfile) {

        PendingUser pendingUser = pendingUserRepository.findById(UUID.fromString(pendingUserProfile.getUserId()))
            .orElseThrow(UserNotFoundException::new);

        pendingUserRepository.delete(pendingUser);

        Teacher teacher = new Teacher(
            UUID.randomUUID(),
            pendingUser.getFirstName(),
            pendingUser.getLastName(),
            pendingUser.getEmail(),
            pendingUser.getPassword(),
            pendingUser.getPhoneNumber(),
            pendingUser.getAddress()
        );

        teacherRepository.save(teacher);
    }

    @Transactional(readOnly = true)
    public TeacherProfile findTeacherById(UUID id) {
        Teacher teacher = teacherRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return teacherProfileMapper.mapTeacherEntityToProfile(teacher);
    }

    @Transactional(readOnly = true)
    public TeacherProfile findTeacherByCourse(String nameOfCourse) {
        Course course = courseRepository.findByCourseName(nameOfCourse)
            .orElseThrow(CourseNotFoundException::new);

        return teacherProfileMapper.mapTeacherEntityToProfile(course.getTeacher());
    }

    @Transactional
    public void deleteTeacher(String email) {
        teacherRepository.deleteById(getTeacherIdByEmail(email));
    }

    @Transactional
    public void excludeTeacherFromCourse(String email) {
        Teacher teacher = teacherRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

        teacher.setCourse(null);

        teacherRepository.save(teacher);
    }

    @Transactional
    public void addTeacherToCourse(String email, String nameOfCourse) {
        Teacher teacher = teacherRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

        teacher.setCourse(courseRepository.findByCourseName(nameOfCourse).orElseThrow(CourseNotFoundException::new));

        teacherRepository.save(teacher);
    }

    @Transactional
    public void changePassword(PasswordChangeRequest passwordChangeRequest) {
        requestValidator.validatePasswordChangeRequest(passwordChangeRequest);

        Teacher teacher = teacherRepository.findByEmail(passwordChangeRequest.getEmail()).orElseThrow(BadCredentialsException::new);

        teacher.setPassword(passwordChangeRequest.getNewPassword());

        teacherRepository.save(teacher);
    }

    @Transactional(readOnly = true)
    public TeacherProfile findTeacherByEmail(String email) {
        Teacher teacher = teacherRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        return teacherProfileMapper.mapTeacherEntityToProfile(teacher);
    }

    @Transactional(readOnly = true)
    public Page<TeacherProfile> findAll(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<Teacher> teacherPage = teacherRepository.findAll(pageable);
        return teacherPage.map(teacherProfileMapper::mapTeacherEntityToProfile);
    }

    @Transactional(readOnly = true)
    public List<Course> getTeacherCourses(String teacherEmail) {
        return courseRepository.findByTeacher(
            teacherRepository
                .findByEmail(teacherEmail)
                .orElseThrow(UserNotFoundException::new)
        );
    }

    private UUID getTeacherIdByEmail(String email) {
        return teacherRepository.findByEmail(email).orElseThrow(UserNotFoundException::new).getUserId();
    }
}
