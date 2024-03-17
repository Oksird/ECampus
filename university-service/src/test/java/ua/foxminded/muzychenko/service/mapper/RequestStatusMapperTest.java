package ua.foxminded.muzychenko.service.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.foxminded.muzychenko.dto.RequestStatusDTO;
import ua.foxminded.muzychenko.entity.RequestStatus;
import ua.foxminded.muzychenko.enums.RequestStatusEnum;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = RequestStatusMapper.class)
public class RequestStatusMapperTest {

    @Autowired
    private RequestStatusMapper requestStatusMapper;

    @Test
    void mapRequestStatusToDTOShouldReturnMappedEntity() {
        RequestStatus requestStatus = new RequestStatus(
            UUID.randomUUID(),
            RequestStatusEnum.APPROVED
        );

        assertEquals(new RequestStatusDTO(requestStatus.getId().toString(), requestStatus.getStatus()), requestStatusMapper.mapRequestStatusToDTO(requestStatus));
    }
}
