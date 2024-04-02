package ua.foxminded.muzychenko.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ua.foxminded.muzychenko.dto.UserRequestDTO;
import ua.foxminded.muzychenko.dto.profile.ShortUserInfo;
import ua.foxminded.muzychenko.entity.PendingUser;
import ua.foxminded.muzychenko.entity.RequestStatus;
import ua.foxminded.muzychenko.entity.RequestType;
import ua.foxminded.muzychenko.entity.Staff;
import ua.foxminded.muzychenko.entity.Student;
import ua.foxminded.muzychenko.entity.Teacher;
import ua.foxminded.muzychenko.entity.UserRequest;
import ua.foxminded.muzychenko.enums.RequestStatusEnum;
import ua.foxminded.muzychenko.enums.RequestTypeEnum;
import ua.foxminded.muzychenko.repository.PendingUserRepository;
import ua.foxminded.muzychenko.repository.RequestStatusRepository;
import ua.foxminded.muzychenko.repository.RequestTypeRepository;
import ua.foxminded.muzychenko.repository.StaffRepository;
import ua.foxminded.muzychenko.repository.StudentRepository;
import ua.foxminded.muzychenko.repository.TeacherRepository;
import ua.foxminded.muzychenko.repository.UserRequestRepository;
import ua.foxminded.muzychenko.service.mapper.UserRequestMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = UserRequestService.class)
class UserRequestServiceTest {

    @MockBean
    private UserRequestRepository userRequestRepository;
    @MockBean
    private UserRequestMapper userRequestMapper;
    @MockBean
    private StudentRepository studentRepository;
    @MockBean
    private StaffRepository staffRepository;
    @MockBean
    private TeacherRepository teacherRepository;
    @MockBean
    private RequestTypeRepository requestTypeRepository;
    @MockBean
    private RequestStatusRepository requestStatusRepository;
    @MockBean
    private PendingUserRepository pendingUserRepository;
    @Autowired
    private UserRequestService userRequestService;

    @Test
    void createRequestShouldCreateRequestWithCorrectTypeForSpecificUser() {
        String[] roles = new String[]{"ROLE_STUDENT", "ROLE_TEACHER", "ROLE_STAFF", "ROLE_PENDING"};

        Student student = new Student(
            UUID.randomUUID(),
            "fn",
            "ln",
            "em",
            "pass",
            null,
            "num",
            "adr"
        );

        Teacher teacher = new Teacher(
          UUID.randomUUID(),
            "fn",
            "ln",
            "em",
            "pass",
            "num",
            "adr"
        );

        Staff staff = new Staff(
            UUID.randomUUID(),
            "fn",
            "ln",
            "em",
            "pass",
            "num",
            "adr"
        );

        PendingUser pendingUser = new PendingUser(
            UUID.randomUUID(),
            "fn",
            "ln",
            "em",
            "pass",
            "num",
            "adr"
        );

        RequestType requestType = new RequestType(
            UUID.randomUUID(),
            RequestTypeEnum.BECOME_STAFF
        );

        RequestStatus requestStatus = new RequestStatus(
            UUID.randomUUID(),
            RequestStatusEnum.PENDING
        );


        when(studentRepository.findById(any(UUID.class)))
            .thenReturn(Optional.of(student));
        when(staffRepository.findById(any(UUID.class)))
            .thenReturn(Optional.of(staff));
        when(teacherRepository.findById(any(UUID.class)))
            .thenReturn(Optional.of(teacher));
        when(pendingUserRepository.findById(any(UUID.class)))
            .thenReturn(Optional.of(pendingUser));


        when(requestTypeRepository.findByType(any(RequestTypeEnum.class)))
            .thenReturn(Optional.of(requestType));

        when(requestStatusRepository.findByStatus(any(RequestStatusEnum.class)))
            .thenReturn(Optional.of(requestStatus));

        for(String role : roles) {
            userRequestService.createRequest(RequestTypeEnum.BECOME_STAFF, new ShortUserInfo(UUID.randomUUID().toString(), role));
        }

        assertEquals(Optional.of(student), studentRepository.findById(UUID.randomUUID()));
        assertEquals(Optional.of(teacher), teacherRepository.findById(UUID.randomUUID()));
        assertEquals(Optional.of(staff), staffRepository.findById(UUID.randomUUID()));
        assertEquals(Optional.of(pendingUser), pendingUserRepository.findById(UUID.randomUUID()));

        assertEquals(Optional.of(requestType), requestTypeRepository.findByType(RequestTypeEnum.BECOME_STAFF));
        assertEquals(Optional.of(requestStatus), requestStatusRepository.findByStatus(RequestStatusEnum.PENDING));
        verify(userRequestRepository, times(4)).save(any(UserRequest.class));

        assertThrows(IllegalArgumentException.class,
            () -> userRequestService.createRequest(
                RequestTypeEnum.BECOME_STAFF,
                new ShortUserInfo(UUID.randomUUID().toString(), "wrongRole")
            )
        );
    }

    @Test
    void approveRequestShouldSetStatusToApproved() {
        UserRequest userRequest = new UserRequest(
            UUID.randomUUID(),
            null,
            null,
            new RequestStatus(UUID.randomUUID(), RequestStatusEnum.APPROVED)
        );

        RequestStatus status = new RequestStatus(
            UUID.randomUUID(),
            RequestStatusEnum.APPROVED
        );

        when(userRequestRepository.findById(any(UUID.class)))
            .thenReturn(Optional.of(userRequest));
        when(requestStatusRepository.findByStatus(RequestStatusEnum.APPROVED))
            .thenReturn(Optional.of(status));

        userRequestService.approveRequest(userRequest.getId());
        assertEquals(userRequestRepository.findById(UUID.randomUUID()), Optional.of(userRequest));
        assertEquals(requestStatusRepository.findByStatus(RequestStatusEnum.APPROVED), Optional.of(status));
        verify(userRequestRepository).save(userRequest);
    }

    @Test
    void rejectRequestShouldSetStatusToRejected() {
        UserRequest userRequest = new UserRequest(
            UUID.randomUUID(),
            null,
            null,
            new RequestStatus(UUID.randomUUID(), RequestStatusEnum.REJECTED)
        );

        RequestStatus status = new RequestStatus(
            UUID.randomUUID(),
            RequestStatusEnum.REJECTED
        );

        when(userRequestRepository.findById(any(UUID.class)))
            .thenReturn(Optional.of(userRequest));
        when(requestStatusRepository.findByStatus(RequestStatusEnum.REJECTED))
            .thenReturn(Optional.of(status));

        userRequestService.rejectRequest(userRequest.getId());
        assertEquals(userRequestRepository.findById(UUID.randomUUID()), Optional.of(userRequest));
        assertEquals(requestStatusRepository.findByStatus(RequestStatusEnum.REJECTED), Optional.of(status));
        verify(userRequestRepository).save(userRequest);
    }

    @Test
    void findAllShouldReturnAllUserRequests() {
        List<UserRequest> userRequests = Arrays.asList(
            new UserRequest(), new UserRequest(), new UserRequest()); // Sample user requests

        Page<UserRequest> page = new PageImpl<>(userRequests);

        when(userRequestRepository.findAll(any(Pageable.class))).thenReturn(page);

        List<UserRequestDTO> result = userRequestService.findAll(1, 10).getContent();

        assertEquals(userRequests.size(), result.size());
    }

    @Test
    void findAllWithoutPaginationShouldReturnAllUserRequests() {
        List<UserRequest> userRequests = Arrays.asList(
            new UserRequest(), new UserRequest(), new UserRequest()); // Sample user requests

        when(userRequestRepository.findAll()).thenReturn(userRequests);

        List<UserRequestDTO> result = userRequestService.findAll();

        assertEquals(userRequests.size(), result.size());
    }

    @Test
    void findByStatusShouldReturnUserRequestsByStatus() {
        List<UserRequest> userRequests = Arrays.asList(
            new UserRequest(), new UserRequest(), new UserRequest());

        RequestStatus status = new RequestStatus(
            UUID.randomUUID(),
            RequestStatusEnum.PENDING
        );

        when(requestStatusRepository.findByStatus(any(RequestStatusEnum.class))).thenReturn(Optional.of(status));
        when(userRequestRepository.findByStatus(any(RequestStatus.class))).thenReturn(userRequests);

        List<UserRequestDTO> result = userRequestService.findByStatus(RequestStatusEnum.PENDING);

        assertEquals(userRequests.size(), result.size());
    }

    @Test
    void findByTypeShouldReturnUserRequestsByType() {
        List<UserRequest> userRequests = new ArrayList<>(List.of(new UserRequest(), new UserRequest(), new UserRequest()));

        RequestType requestType = new RequestType(
            UUID.randomUUID(),
            RequestTypeEnum.BECOME_STAFF
        );

        userRequests.forEach(userRequest -> userRequest.setType(requestType));

        when(requestTypeRepository.findByType(any(RequestTypeEnum.class))).thenReturn(Optional.of(requestType));
        when(userRequestRepository.findByType(any(RequestType.class))).thenReturn(userRequests);

        List<UserRequestDTO> result = userRequestService.findByType(RequestTypeEnum.BECOME_STAFF);

        assertEquals(userRequests.size(), result.size());
    }


    @Test
    void deleteRequestShouldDeleteUserRequest() {
        UserRequestDTO requestDTO = new UserRequestDTO(
            UUID.randomUUID().toString(),
            null,
            null,
            null,
            null
        );

        userRequestService.deleteRequest(requestDTO);

        verify(userRequestRepository).deleteById(UUID.fromString(requestDTO.getId()));
    }
}
