package ua.foxminded.muzychenko.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.dto.RequestStatusDTO;
import ua.foxminded.muzychenko.entity.RequestStatus;
import ua.foxminded.muzychenko.repository.RequestStatusRepository;
import ua.foxminded.muzychenko.service.mapper.RequestStatusMapper;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RequestStatusService {

    private final RequestStatusRepository requestStatusRepository;
    private final RequestStatusMapper requestStatusMapper;

    @Transactional
    public void createRequestStatus(RequestStatusDTO requestStatusDTO) {
        requestStatusRepository.save(new RequestStatus(
            UUID.fromString(requestStatusDTO.getId()),
            requestStatusDTO.getStatus()
            )
        );
    }

    @Transactional(readOnly = true)
    public RequestStatusDTO getRequestStatusById(UUID id) {
        return requestStatusMapper.mapRequestStatusToDTO(
            requestStatusRepository.findById(id).orElseThrow(EntityNotFoundException::new)
        );
    }

    @Transactional(readOnly = true)
    public List<RequestStatusDTO> findAllRequestStatuses() {
        return requestStatusRepository.findAll()
            .stream()
            .map(requestStatusMapper::mapRequestStatusToDTO)
            .toList();
    }

    @Transactional
    public void updateRequestStatus(RequestStatusDTO requestStatusDTO) {
        RequestStatus requestStatus = requestStatusRepository.findById(UUID.fromString(requestStatusDTO.getId()))
            .orElseThrow(EntityNotFoundException::new);

        requestStatus.setStatus(requestStatusDTO.getStatus());

        requestStatusRepository.save(requestStatus);
    }

    @Transactional
    public void deleteRequestStatus(RequestStatusDTO requestStatusDTO) {
        requestStatusRepository.deleteById(UUID.fromString(requestStatusDTO.getId()));
    }
}
