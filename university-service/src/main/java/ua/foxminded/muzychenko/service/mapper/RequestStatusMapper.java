package ua.foxminded.muzychenko.service.mapper;

import ua.foxminded.muzychenko.config.Mapper;
import ua.foxminded.muzychenko.dto.RequestStatusDTO;
import ua.foxminded.muzychenko.entity.RequestStatus;

@Mapper
public class RequestStatusMapper {

    public RequestStatusDTO mapRequestStatusToDTO(RequestStatus requestStatus) {
        return new RequestStatusDTO(
            requestStatus.getId().toString(),
            requestStatus.getStatus()
        );
    }
}
