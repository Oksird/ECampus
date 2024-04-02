package ua.foxminded.muzychenko.service.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.foxminded.muzychenko.dto.RequestStatusDTO;
import ua.foxminded.muzychenko.dto.RequestTypeDTO;
import ua.foxminded.muzychenko.dto.UserRequestDTO;
import ua.foxminded.muzychenko.entity.AbstractUser;
import ua.foxminded.muzychenko.entity.RequestStatus;
import ua.foxminded.muzychenko.entity.RequestType;
import ua.foxminded.muzychenko.entity.Staff;
import ua.foxminded.muzychenko.entity.UserRequest;
import ua.foxminded.muzychenko.enums.RequestStatusEnum;
import ua.foxminded.muzychenko.enums.RequestTypeEnum;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = UserRequestMapper.class)
public class UserRequestMapperTest {
    @MockBean
    private RequestStatusMapper requestStatusMapper;
    @MockBean
    private RequestTypeMapper requestTypeMapper;
    @Autowired
    private UserRequestMapper userRequestMapper;

    @Test
    void mapUserRequestToDTOShouldMapEntityToDTO() {
        AbstractUser abstractUser = new Staff(
            UUID.randomUUID(),
            "fn",
            "ln",
            "em",
            "pass",
            "pn",
            "addr"
        );

        RequestType requestType = new RequestType(
            UUID.randomUUID(),
            RequestTypeEnum.BECOME_STAFF
        );

        RequestStatus requestStatus = new RequestStatus(
            UUID.randomUUID(),
            RequestStatusEnum.APPROVED
        );

        UserRequest userRequest = new UserRequest(
            UUID.randomUUID(),
            abstractUser,
            requestType,
            requestStatus
        );

        RequestTypeDTO requestTypeDTO = new RequestTypeDTO(
            requestType.getId().toString(),
            requestType.getType()
        );

        RequestStatusDTO requestStatusDTO = new RequestStatusDTO(
            requestStatus.getId().toString(),
            requestStatus.getStatus()
        );

        UserRequestDTO userRequestDTO = new UserRequestDTO(
            userRequest.getId().toString(),
            userRequest.getUser().getUserId().toString(),
            userRequest.getUser().getRole(),
            requestTypeDTO,
            requestStatusDTO
        );

        when(requestTypeMapper.mapRequestTypeToDTO(any(RequestType.class)))
            .thenReturn(requestTypeDTO);
        when(requestStatusMapper.mapRequestStatusToDTO(any(RequestStatus.class)))
            .thenReturn(requestStatusDTO);

        assertEquals(userRequestDTO, userRequestMapper.mapUserRequestToDTO(userRequest));
    }
}
