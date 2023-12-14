package ua.foxminded.muzychenko.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.dto.profile.PendingUserProfile;
import ua.foxminded.muzychenko.dto.profile.StudentProfile;
import ua.foxminded.muzychenko.dto.request.PasswordChangeRequest;
import ua.foxminded.muzychenko.entity.PendingUser;
import ua.foxminded.muzychenko.entity.Student;
import ua.foxminded.muzychenko.exception.GroupNotFoundException;
import ua.foxminded.muzychenko.exception.UserNotFoundException;
import ua.foxminded.muzychenko.repository.GroupRepository;
import ua.foxminded.muzychenko.repository.PendingUserRepository;
import ua.foxminded.muzychenko.repository.StudentRepository;
import ua.foxminded.muzychenko.service.mapper.StudentProfileMapper;
import ua.foxminded.muzychenko.service.validator.RequestValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;
    private final StudentProfileMapper studentProfileMapper;
    private final PendingUserRepository pendingUserRepository;
    private final RequestValidator requestValidator;

    @Transactional
    public void createStudentFromPendingUser(PendingUserProfile pendingUserProfile) {

        PendingUser pendingUser = pendingUserRepository.findById(UUID.fromString(pendingUserProfile.getUserId()))
            .orElseThrow(UserNotFoundException::new);

        pendingUserRepository.delete(pendingUser);

        Student student = new Student(
            UUID.randomUUID(),
            pendingUser.getFirstName(),
            pendingUser.getLastName(),
            pendingUser.getEmail(),
            pendingUser.getPassword(),
            null,
            pendingUser.getPhoneNumber(),
            pendingUser.getAddress()
        );

        studentRepository.save(student);
    }

    @Transactional(readOnly = true)
    public StudentProfile findStudentById(UUID id) {
        Student student = studentRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return studentProfileMapper.mapStudentInfoToProfile(student);
    }

    @Transactional(readOnly = true)
    public List<StudentProfile> findStudentsByGroup(String nameOfGroup) {
        List<Student> students = studentRepository.findByGroup_GroupName(nameOfGroup);
        return getStudentProfiles(students);
    }

    @Transactional
    public void changePassword(PasswordChangeRequest passwordChangeRequest) {
        requestValidator.validatePasswordChangeRequest(passwordChangeRequest);

        Student student = studentRepository
            .findByEmail(passwordChangeRequest.getEmail())
            .orElseThrow(UserNotFoundException::new);

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
    public void addStudentToGroup(String email, String nameOfGroup) {
        Student student = studentRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

        student.setGroup(groupRepository.findByGroupName(nameOfGroup).orElseThrow(GroupNotFoundException::new));

        studentRepository.save(student);
    }

    @Transactional(readOnly = true)
    public StudentProfile findStudentByEmail(String email) {
        Student student = studentRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        return studentProfileMapper.mapStudentInfoToProfile(student);
    }

    @Transactional(readOnly = true)
    public Page<StudentProfile> findAll(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<Student> studentPage = studentRepository.findAll(pageable);
        return studentPage.map(studentProfileMapper::mapStudentInfoToProfile);
    }

    private UUID getStudentIdByEmail(String email) {
        return studentRepository.findByEmail(email).orElseThrow(UserNotFoundException::new).getUserId();
    }

    private List<StudentProfile> getStudentProfiles(List<Student> students) {
        List<StudentProfile> studentProfiles = new ArrayList<>();

        for (Student student : students) {
            studentProfiles.add(studentProfileMapper
                .mapStudentInfoToProfile(student)
            );
        }
        return studentProfiles;
    }
}
