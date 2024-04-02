package ua.foxminded.muzychenko.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ua.foxminded.muzychenko.dto.profile.CourseInfo;
import ua.foxminded.muzychenko.dto.profile.PendingUserProfile;
import ua.foxminded.muzychenko.dto.profile.TeacherProfile;
import ua.foxminded.muzychenko.dto.request.PasswordChangeRequest;
import ua.foxminded.muzychenko.entity.Course;
import ua.foxminded.muzychenko.entity.PendingUser;
import ua.foxminded.muzychenko.entity.Teacher;
import ua.foxminded.muzychenko.repository.CourseRepository;
import ua.foxminded.muzychenko.repository.PendingUserRepository;
import ua.foxminded.muzychenko.repository.TeacherRepository;
import ua.foxminded.muzychenko.service.mapper.CourseInfoMapper;
import ua.foxminded.muzychenko.service.mapper.TeacherProfileMapper;
import ua.foxminded.muzychenko.service.validator.RequestValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
classes = TeacherService.class)
class TeacherServiceTest {
    @MockBean
    private TeacherRepository teacherRepository;
    @MockBean
    private CourseRepository courseRepository;
    @MockBean
    private RequestValidator requestValidator;
    @MockBean
    private PendingUserRepository pendingUserRepository;
    @MockBean
    private TeacherProfileMapper teacherProfileMapper;
    @MockBean
    private CourseInfoMapper courseInfoMapper;
    @Autowired
    private TeacherService teacherService;

    @Test
    void findTeacherByIdShouldReturnTeacherWithCorrectID() {
        Teacher teacher = new Teacher(
            UUID.randomUUID(),
            "fn",
            "ln",
            "em",
            "pass",
            "380227738888",
            "address"
        );

        TeacherProfile teacherProfile = new TeacherProfile(
            teacher.getUserId().toString(),
            teacher.getFirstName(),
            teacher.getLastName(),
            teacher.getEmail(),
            teacher.getPhoneNumber(),
            teacher.getAddress()
        );

        when(teacherRepository.findById(any(UUID.class)))
            .thenReturn(Optional.of(teacher));

        when(teacherProfileMapper.mapTeacherEntityToProfile(eq(teacher)))
            .thenReturn(teacherProfile);

        assertEquals(teacherProfile, teacherService.findTeacherById(UUID.randomUUID()));
    }

    @Test
    void findAllTeachersShouldReturnListOfAllTeachersByPage() {
        List<Teacher> teacherList = new ArrayList<>(List.of(
            new Teacher(
                UUID.randomUUID(),
                "fn",
                "ln",
                "em",
                "pass",
                "380227738888",
                "address"
            ),
            new Teacher(
                UUID.randomUUID(),
                "fn2",
                "ln2",
                "em2",
                "pass2",
                "380227738888",
                "address"
            )
        ));

        Teacher teacher1 = teacherList.get(0);
        Teacher teacher2 = teacherList.get(1);

        TeacherProfile teacherProfile1 = new TeacherProfile(
            teacher1.getUserId().toString(),
            teacher1.getFirstName(),
            teacher1.getLastName(),
            teacher1.getEmail(),
            "380227738888",
            "address"
        );
        TeacherProfile teacherProfile2 = new TeacherProfile(
            teacher2.getUserId().toString(),
            teacher2.getFirstName(),
            teacher2.getLastName(),
            teacher2.getEmail(),
            "380227738888",
            "address"
        );

        List<TeacherProfile> teacherProfileList = new ArrayList<>(List.of(teacherProfile1, teacherProfile2));

        when(teacherProfileMapper.mapTeacherEntityToProfile(eq(teacher1)))
            .thenReturn(teacherProfile1);

        when(teacherProfileMapper.mapTeacherEntityToProfile(eq(teacher2)))
            .thenReturn(teacherProfile2);

        when(teacherRepository.findAll(any(Pageable.class)))
            .thenReturn(new PageImpl<>(teacherList));

        assertEquals(teacherProfileList, teacherService.findAll(1,1).getContent());
    }

    @Test
    void changePasswordShouldUpdatePasswordFieldOfTeacherInDataBase() {
        Teacher teacher = new Teacher(
            UUID.randomUUID(),
            "fN",
            "lN",
            "em",
            "pass",
            "380227738888",
            "address"
        );

        when(teacherRepository.findByEmail(any(String.class)))
            .thenReturn(Optional.of(teacher));

        when(teacherRepository.save(any(Teacher.class)))
            .thenReturn(teacher);

        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest(
            "em",
            "pppp",
            "pass",
            "nPass",
            "npass"
        );

        teacherService.changePassword(passwordChangeRequest);

        verify(teacherRepository).findByEmail(any(String.class));
        verify(teacherRepository).save(any(Teacher.class));
    }

    @Test
    void deleteTeacherShouldRemoveTeacherFromDataBase() {
        Teacher teacher = new Teacher(
            UUID.randomUUID(),
            "fN",
            "lN",
            "em",
            "pass",
            "asd",
            "asd"
        );

        when(teacherRepository.findByEmail(any(String.class)))
            .thenReturn(Optional.of(teacher));

        doNothing()
            .when(teacherRepository)
            .deleteById(any(UUID.class));

        teacherService.deleteTeacher(teacher.getEmail());

        verify(teacherRepository).findByEmail(teacher.getEmail());
        verify(teacherRepository).deleteById(teacher.getUserId());
    }

    @Test
    void excludeTeacherFromCourseShouldRemoveTeacherFromChosenCourse() {
        Teacher teacher = new Teacher(
            UUID.randomUUID(),
            "fN",
            "lN",
            "em",
            "pass",
            "380227738888",
            "address"
        );

        when(teacherRepository.findByEmail(any(String.class)))
            .thenReturn(Optional.of(teacher));

        when(courseRepository.findByCourseName(any(String.class)))
            .thenReturn(Optional.of(new Course(UUID.randomUUID(), "cn", "cd")));

        teacherService.excludeTeacherFromCourse(teacher.getEmail());

        verify(teacherRepository).findByEmail(teacher.getEmail());
        verify(teacherRepository).save(any(Teacher.class));
    }

    @Test
    void addTeacherToCourseShouldAddTeacherToCourseByItsName() {
        Teacher teacher = new Teacher(
            UUID.randomUUID(),
            "fN",
            "lN",
            "em",
            "pass",
            "380227738888",
            "address"
        );

        when(teacherRepository.findByEmail(any(String.class)))
            .thenReturn(Optional.of(teacher));

        when(courseRepository.findByCourseName(any(String.class)))
            .thenReturn(Optional.of(new Course(UUID.randomUUID(), "cn", "cd")));

        teacherService.addTeacherToCourse(teacher.getEmail(), "cN");

        verify(teacherRepository).findByEmail(teacher.getEmail());
        verify(teacherRepository).save(any(Teacher.class));
    }

    @Test
    void findTeacherByEmailShouldReturnTeacherProfile() {
        Teacher teacher = new Teacher(
            UUID.randomUUID(),
            "fn",
            "ln",
            "em",
            "pass",
            "380227738888",
            "address"
        );

        TeacherProfile teacherProfile = new TeacherProfile(
            teacher.getUserId().toString(),
            teacher.getFirstName(),
            teacher.getLastName(),
            teacher.getEmail(),
            teacher.getPhoneNumber(),
            teacher.getAddress()
        );

        when(teacherRepository.findByEmail(any(String.class)))
            .thenReturn(Optional.of(teacher));

        when(teacherProfileMapper.mapTeacherEntityToProfile(eq(teacher)))
            .thenReturn(teacherProfile);

        assertEquals(teacherProfile, teacherService.findTeacherByEmail("email"));
    }

    @Test
    void createTeacherFromPendingUserShouldCreateNewTeacherFromPUserInfo() {
        PendingUser pendingUser = new PendingUser(
            UUID.randomUUID(),
            "fn",
            "ln",
            "em",
            "pass",
            "380227738888",
            "address"
        );

        PendingUserProfile pendingUserProfile = new PendingUserProfile(
            pendingUser.getUserId().toString(),
            pendingUser.getFirstName(),
            pendingUser.getLastName(),
            pendingUser.getEmail(),
            pendingUser.getPhoneNumber(),
            pendingUser.getAddress()
        );

        Teacher teacher = new Teacher(
            UUID.randomUUID(),
            pendingUser.getFirstName(),
            pendingUser.getLastName(),
            pendingUser.getEmail(),
            pendingUser.getPassword(),
            pendingUser.getPhoneNumber(),
            pendingUser.getAddress()
        );

        when(pendingUserRepository.findById(any(UUID.class)))
            .thenReturn(Optional.of(pendingUser));

        doNothing().when(pendingUserRepository).delete(pendingUser);

        when(teacherRepository.save(teacher)).thenReturn(teacher);

        teacherService.createTeacherFromPendingUser(pendingUserProfile);

        assertEquals(pendingUserRepository.findById(UUID.randomUUID()).orElse(null), pendingUser);
        verify(pendingUserRepository).delete(pendingUser);
        assertEquals(teacherRepository.save(teacher), teacher);
    }

    @Test
    void getTeacherCoursesShouldReturnCoursesRelatedToTeacher() {
        Teacher teacher = new Teacher(
            UUID.randomUUID(),
            "fn",
            "ln",
            "em",
            "pass",
            "phN",
            "address"
        );

        Course course = new Course(
            UUID.randomUUID(),
            "CourseName",
            "Course desc"
        );

        CourseInfo courseInfo = new CourseInfo(
            course.getCourseId().toString(),
            course.getCourseName(),
            course.getCourseDescription(),
            null
        );

        when(teacherRepository.findByEmail(any(String.class)))
            .thenReturn(Optional.of(teacher));
        when(courseRepository.findByTeacher(any(Teacher.class)))
            .thenReturn(Optional.of(course));
        when(courseInfoMapper.mapCourseEntityToCourseInfo(course))
            .thenReturn(courseInfo);

        assertEquals(teacherService.getTeachersCourse(teacher.getEmail()), courseInfo);
    }

    @Test
    void findTeacherByCourseShouldReturnCorrectTeacherRelatedToSpecificCourse() {
        Teacher teacher = new Teacher(
            UUID.randomUUID(),
            "fn",
            "ln",
            "em",
            "pass",
            "phN",
            "address"
        );

        Course course = new Course(
            UUID.randomUUID(),
            "CourseName",
            "Course desc"
        );

        teacher.setCourse(course);
        course.setTeacher(teacher);

        TeacherProfile teacherProfile = new TeacherProfile(
            teacher.getUserId().toString(),
            teacher.getFirstName(),
            teacher.getLastName(),
            teacher.getEmail(),
            teacher.getPhoneNumber(),
            teacher.getAddress()
        );

        when(courseRepository.findByCourseName(any(String.class)))
            .thenReturn(Optional.of(course));

        when(teacherProfileMapper.mapTeacherEntityToProfile(any(Teacher.class)))
            .thenReturn(teacherProfile);

        assertEquals(teacherProfile, teacherService.findTeacherByCourse("course"));
    }

    @Test
    void findAllShouldReturnListOfAllTeacherProfiles() {
        Teacher teacher1 = new Teacher(
            UUID.randomUUID(),
            "fn1",
            "ln1",
            "em1",
            "pass1",
            "pn1",
            "addr1"
        );

        Teacher teacher2 =  new Teacher(
            UUID.randomUUID(),
            "fn2",
            "ln2",
            "em2",
            "pass2",
            "pn2",
            "addr2"
        );

        TeacherProfile teacherProfile1 = new TeacherProfile(
            teacher1.getUserId().toString(),
            teacher1.getFirstName(),
            teacher1.getLastName(),
            teacher1.getEmail(),
            teacher1.getPhoneNumber(),
            teacher1.getAddress()
        );
        TeacherProfile teacherProfile2 = new TeacherProfile(
            teacher2.getUserId().toString(),
            teacher2.getFirstName(),
            teacher2.getLastName(),
            teacher2.getEmail(),
            teacher2.getPhoneNumber(),
            teacher2.getAddress()
        );

        when(teacherRepository.findAll())
            .thenReturn(List.of(teacher1, teacher2));
        when(teacherProfileMapper.mapTeacherEntityToProfile(teacher1))
            .thenReturn(teacherProfile1);
        when(teacherProfileMapper.mapTeacherEntityToProfile(teacher2))
            .thenReturn(teacherProfile2);

        List<TeacherProfile> actualTeacherProfiles = teacherService.findAll();

        assertEquals(2, actualTeacherProfiles.size());
        assertEquals(teacherProfile1, actualTeacherProfiles.get(0));
        assertEquals(teacherProfile2, actualTeacherProfiles.get(1));
    }
}
