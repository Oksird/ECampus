package ua.foxminded.muzychenko.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.dto.RequestTypeDTO;
import ua.foxminded.muzychenko.entity.RequestType;
import ua.foxminded.muzychenko.repository.RequestTypeRepository;
import ua.foxminded.muzychenko.service.mapper.RequestTypeMapper;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RequestTypeService {

    private final RequestTypeRepository requestTypeRepository;
    private final RequestTypeMapper requestTypeMapper;

    @Transactional
    public void createRequestType(RequestTypeDTO requestTypeDTO) {
        requestTypeRepository.save(new RequestType(
                UUID.fromString(requestTypeDTO.getId()),
                requestTypeDTO.getType()
            )
        );
    }

    @Transactional(readOnly = true)
    public List<RequestTypeDTO> findAllRequestTypes() {
        return requestTypeRepository.findAll()
            .stream()
            .map(requestTypeMapper::mapRequestTypeToDTO)
            .toList();
    }

    @Transactional(readOnly = true)
    public RequestTypeDTO getRequestTypeById(UUID id) {
        return requestTypeMapper.mapRequestTypeToDTO(
            requestTypeRepository.findById(id).orElseThrow(EntityNotFoundException::new)
        );
    }

    @Transactional
    public void updateRequestType(RequestTypeDTO requestTypeDTO) {
        RequestType requestType = requestTypeRepository.findById(UUID.fromString(requestTypeDTO.getId()))
            .orElseThrow(EntityNotFoundException::new);

        requestType.setType(requestTypeDTO.getType());

        requestTypeRepository.save(requestType);
    }

    @Transactional
    public void deleteRequestType(RequestTypeDTO requestTypeDTO) {
        requestTypeRepository.deleteById(UUID.fromString(requestTypeDTO.getId()));
    }
}
