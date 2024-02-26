package ua.foxminded.muzychenko.repository;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.DataConfiguration;
import ua.foxminded.muzychenko.DataTestConfig;
import ua.foxminded.muzychenko.entity.RequestStatus;
import ua.foxminded.muzychenko.entity.RequestType;
import ua.foxminded.muzychenko.entity.UserRequest;
import ua.foxminded.muzychenko.enums.RequestStatusEnum;
import ua.foxminded.muzychenko.enums.RequestTypeEnum;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = DataConfiguration.class)
@SpringBootTest(classes = UserRequestRepository.class)
@Import(DataTestConfig.class)
@Transactional
class UserRequestRepositoryTest {

    @Autowired
    private UserRequestRepository userRequestRepository;
    @Autowired
    private RequestStatusRepository requestStatusRepository;
    @Autowired
    private RequestTypeRepository requestTypeRepository;

    @Test
    void findByStatusShouldReturnRequestsWithCorrectStatus() {
        RequestStatus requestStatus = requestStatusRepository.findByStatus(RequestStatusEnum.APPROVED)
            .orElseThrow(EntityNotFoundException::new);

        List<UserRequest> actualUserRequests = userRequestRepository.findByStatus(requestStatus);
        List<UserRequest> expectedUserRequests = userRequestRepository.findAll()
            .stream()
            .filter(request -> request.getStatus().getStatus().equals(RequestStatusEnum.APPROVED))
            .toList();

        assertEquals(expectedUserRequests, actualUserRequests);
    }

    @Test
    void findByTypeShouldReturnRequestsWithCorrectType() {
        RequestType requestType = requestTypeRepository.findByType(RequestTypeEnum.BECOME_STAFF)
            .orElseThrow(EntityNotFoundException::new);

        List<UserRequest> expectedUserRequests = userRequestRepository.findAll()
            .stream()
            .filter(request -> request.getType().getType().equals(RequestTypeEnum.BECOME_STAFF))
            .toList();
        List<UserRequest> actualUserRequests = userRequestRepository.findByType(requestType);

        assertEquals(expectedUserRequests, actualUserRequests);
    }
}
