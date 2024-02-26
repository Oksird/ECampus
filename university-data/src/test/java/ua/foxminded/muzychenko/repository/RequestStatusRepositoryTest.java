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
import ua.foxminded.muzychenko.enums.RequestStatusEnum;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = DataConfiguration.class)
@SpringBootTest(classes = RequestStatusRepository.class)
@Import(DataTestConfig.class)
@Transactional
class RequestStatusRepositoryTest {

    @Autowired
    private RequestStatusRepository requestStatusRepository;

    @Test
    void findByStatusShouldReturnCorrectEntity() {
        RequestStatus actualStatusEntity = requestStatusRepository.findByStatus(RequestStatusEnum.APPROVED)
            .orElseThrow(EntityNotFoundException::new);

        RequestStatus expectedStatusEntity = requestStatusRepository.findAll()
            .stream()
            .filter(requestStatus -> requestStatus.getStatus().equals(RequestStatusEnum.APPROVED))
            .findAny()
            .orElseThrow(EntityNotFoundException::new);

        assertEquals(expectedStatusEntity, actualStatusEntity);
    }
}
