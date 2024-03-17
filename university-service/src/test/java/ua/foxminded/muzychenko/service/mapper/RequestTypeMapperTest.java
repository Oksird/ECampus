package ua.foxminded.muzychenko.service.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.foxminded.muzychenko.dto.RequestTypeDTO;
import ua.foxminded.muzychenko.entity.RequestType;
import ua.foxminded.muzychenko.enums.RequestTypeEnum;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = RequestTypeMapper.class)
public class RequestTypeMapperTest {

    @Autowired
    private RequestTypeMapper requestTypeMapper;

    @Test
    void mapRequestTypeToDTOShouldReturnMappedEntity() {
        RequestType requestType = new RequestType(
            UUID.randomUUID(),
            RequestTypeEnum.BECOME_STAFF
        );

        assertEquals(new RequestTypeDTO(requestType.getId().toString(), requestType.getType()), requestTypeMapper.mapRequestTypeToDTO(requestType));
    }
}
