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
import ua.foxminded.muzychenko.entity.RequestType;
import ua.foxminded.muzychenko.enums.RequestTypeEnum;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = DataConfiguration.class)
@SpringBootTest(classes = RequestTypeRepository.class)
@Import(DataTestConfig.class)
@Transactional
class RequestTypeRepositoryTest {

    @Autowired
    private RequestTypeRepository requestTypeRepository;

    @Test
    void findByTypeShouldReturnCorrectEntity() {
        RequestType actualRequestType = requestTypeRepository.findByType(RequestTypeEnum.BECOME_STAFF)
            .orElseThrow(EntityNotFoundException::new);

        RequestType expectedRequestType = requestTypeRepository.findAll()
            .stream()
            .filter(requestType -> requestType.getType().equals(RequestTypeEnum.BECOME_STAFF))
            .findAny()
            .orElseThrow(EntityNotFoundException::new);

        assertEquals(expectedRequestType, actualRequestType);
    }
}
