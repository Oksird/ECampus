package ua.foxminded.muzychenko.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import ua.foxminded.muzychenko.dao.CourseDao;
import ua.foxminded.muzychenko.dao.TeacherDao;
import ua.foxminded.muzychenko.dto.TeacherProfileResponse;
import ua.foxminded.muzychenko.dto.UserLoginRequest;
import ua.foxminded.muzychenko.dto.UserProfileResponse;
import ua.foxminded.muzychenko.dto.UserRegistrationRequest;
import ua.foxminded.muzychenko.entity.Teacher;
import ua.foxminded.muzychenko.exception.UserNotFoundException;
import ua.foxminded.muzychenko.service.util.PasswordEncoder;
import ua.foxminded.muzychenko.service.util.Validator;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TeacherService {

    private final CourseDao courseDao;
    private final TeacherDao teacherDao;
    private final Validator validator;
    private final PasswordEncoder passwordEncoder;

    public Teacher findTeacherById(UUID id) {
        return teacherDao.findById(id).orElse(null);
    }

    public List<Teacher> findAllTeachers() {
        return teacherDao.findAllByPage(1L,1L);
    }

    public List<Teacher> findTeachersByCourse(String nameOfCourse) {
        return teacherDao.findByCourse(nameOfCourse);
    }

    public void deleteTeacher(UUID id) {
        teacherDao.deleteById(id);
    }

    public void deleteTeacherFromCourse(UUID teacherId, String nameOfCourse) {
        teacherDao.deleteFromCourse(teacherId,nameOfCourse);
    }

    public void addTeacherToCourse(UUID teacherId, String nameOfCourse) {
        teacherDao.addToCourse(teacherId, nameOfCourse);
    }

    public void changePassword(String email, String oldPassword, String newPassword, String repeatNewPassword) {
        Teacher teacher = teacherDao.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
        validator.validatePasswordChange(oldPassword, newPassword, repeatNewPassword, teacher.getPassword());
        teacher.setPassword(newPassword);
        teacherDao.update(teacher.getUserId(), teacher);
    }

    public UserProfileResponse login(UserLoginRequest userLoginRequest) {
        String email = userLoginRequest.getEmail();
        String password = userLoginRequest.getPassword();

        Optional<Teacher> optionalTeacher = teacherDao.findByEmail(email);
// I'm not sure in this line, do we need double-check for Optional is it empty
        validator.validateLoginRequest(optionalTeacher, password, optionalTeacher.orElse(null).getPassword());

        Teacher teacher = optionalTeacher.get();
        return new TeacherProfileResponse(
            teacher.getFirstName(),
            teacher.getLastName(),
            teacher.getEmail(),
            courseDao.getUserCourses(teacher.getUserId(), teacher.getUserType())
        );
    }

    public void register(@NonNull UserRegistrationRequest userRegistrationRequest) {
        validator.validateUserRegistrationRequest(userRegistrationRequest);
        teacherDao.create(
            new Teacher(
                UUID.randomUUID(),
                userRegistrationRequest.getFirstName(),
                userRegistrationRequest.getLastName(),
                userRegistrationRequest.getEmail(),
                passwordEncoder.encode(userRegistrationRequest.getPassword())
            )
        );
    }
}
