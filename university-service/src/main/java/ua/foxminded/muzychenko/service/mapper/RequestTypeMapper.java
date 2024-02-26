package ua.foxminded.muzychenko.service.mapper;

import ua.foxminded.muzychenko.config.Mapper;
import ua.foxminded.muzychenko.dto.RequestTypeDTO;
import ua.foxminded.muzychenko.entity.RequestType;

@Mapper
public class RequestTypeMapper {
    public RequestTypeDTO mapRequestTypeToDTO(RequestType requestType) {
        return new RequestTypeDTO(
            requestType.getId().toString(),
            requestType.getType()
        );
    }
}
