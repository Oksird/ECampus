package ua.foxminded.muzychenko.service.mapper;

import lombok.AllArgsConstructor;
import ua.foxminded.muzychenko.config.Mapper;
import ua.foxminded.muzychenko.dto.UserRequestDTO;
import ua.foxminded.muzychenko.entity.UserRequest;

@Mapper
@AllArgsConstructor
public class UserRequestMapper {

    private final RequestStatusMapper requestStatusMapper;
    private final RequestTypeMapper requestTypeMapper;

    public UserRequestDTO mapUserRequestToDTO(UserRequest userRequest) {
        return new UserRequestDTO(
            userRequest.getId().toString(),
            userRequest.getUser().getUserId().toString(),
            userRequest.getUser().getRole(),
            requestTypeMapper.mapRequestTypeToDTO(userRequest.getType()),
            requestStatusMapper.mapRequestStatusToDTO(userRequest.getStatus())
        );
    }
}
