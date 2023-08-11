package ua.foxminded.muzychenko.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import ua.foxminded.muzychenko.dao.CourseDao;
import ua.foxminded.muzychenko.dao.GroupDao;
import ua.foxminded.muzychenko.dao.StudentDao;
import ua.foxminded.muzychenko.dto.StudentProfileResponse;
import ua.foxminded.muzychenko.dto.UserLoginRequest;
import ua.foxminded.muzychenko.dto.UserRegistrationRequest;
import ua.foxminded.muzychenko.entity.Student;
import ua.foxminded.muzychenko.exception.BadCredentialsException;
import ua.foxminded.muzychenko.exception.UserNotFoundException;
import ua.foxminded.muzychenko.service.util.PasswordEncoder;
import ua.foxminded.muzychenko.service.util.Validator;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class StudentService {
    private final StudentDao studentDao;
    private final CourseDao courseDao;
    private final GroupDao groupDao;
    private final Validator validator;
    private final PasswordEncoder passwordEncoder;

    public Student findStudentById(UUID id) {
        return studentDao.findById(id).orElse(null);
    }

    public List<Student> findAllStudents() {
        return studentDao.findAllByPage(1L,1L);
    }

    public List<Student> findStudentsByCourse(String nameOfCourse) {
        return studentDao.findByCourse(nameOfCourse);
    }

    public List<Student> findStudentsByGroup(String nameOfGroup) {
        return studentDao.findByGroup(nameOfGroup);
    }

    public StudentProfileResponse login(UserLoginRequest userLoginRequest) {
        String email = userLoginRequest.getEmail();
        String password = userLoginRequest.getPassword();

        Optional<Student> optionalStudent = studentDao.findByEmail(email);

        validator.validateLoginRequest(optionalStudent, password, optionalStudent.orElse(null).getPassword());

        Student student = optionalStudent.get();
        return new StudentProfileResponse(
            student.getFirstName(),
            student.getLastName(),
            student.getEmail(),
            groupDao.findUsersGroup(student.getUserId()).orElse(null),
            courseDao.getUserCourses(student.getUserId(), student.getUserType())
        );
    }

    public void register(@NonNull UserRegistrationRequest userRegistrationRequest) {
        validator.validateUserRegistrationRequest(userRegistrationRequest);
        studentDao.create(
            new Student(
                UUID.randomUUID(),
                userRegistrationRequest.getFirstName(),
                userRegistrationRequest.getLastName(),
                userRegistrationRequest.getEmail(),
                passwordEncoder.encode(userRegistrationRequest.getPassword()),
                null,
                null
            )
        );
    }

    public void deleteStudent(UUID id) {
        studentDao.deleteById(id);
    }

    public void deleteStudentFromGroup(UUID studentId, String nameOfGroup) {
        studentDao.deleteFromGroup(studentId, nameOfGroup);
    }

    public void deleteStudentFromCourse(UUID studentId, String nameOfCourse) {
        studentDao.deleteFromCourse(studentId,nameOfCourse);
    }

    public void addStudentToCourse(UUID studentID, String nameOfCourse) {
        studentDao.addToCourse(studentID, nameOfCourse);
    }

    public void addStudentToGroup(UUID studentId, String nameOfGroup) {
        studentDao.addToGroup(studentId, nameOfGroup);
    }

    public void changePassword(String email, String oldPassword, String newPassword, String repeatNewPassword) {
        Student student = studentDao.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
        validator.validatePasswordChange(oldPassword, newPassword, repeatNewPassword, student.getPassword());
        student.setPassword(newPassword);
        studentDao.update(student.getUserId(), student);
    }
}
