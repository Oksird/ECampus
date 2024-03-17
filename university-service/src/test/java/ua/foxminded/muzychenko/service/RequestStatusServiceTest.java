package ua.foxminded.muzychenko.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.foxminded.muzychenko.dto.RequestStatusDTO;
import ua.foxminded.muzychenko.entity.RequestStatus;
import ua.foxminded.muzychenko.enums.RequestStatusEnum;
import ua.foxminded.muzychenko.repository.RequestStatusRepository;
import ua.foxminded.muzychenko.service.mapper.RequestStatusMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = RequestStatusService.class)
public class RequestStatusServiceTest {

    @MockBean
    private RequestStatusRepository requestStatusRepository;
    @MockBean
    private RequestStatusMapper requestStatusMapper;
    @Autowired
    private RequestStatusService requestStatusService;

    @Test
    void createRequestStatusShouldSaveRequestStatus() {
        RequestStatusDTO requestStatusDTO = new RequestStatusDTO(UUID.randomUUID().toString(), RequestStatusEnum.PENDING);
        RequestStatus requestStatus = new RequestStatus(UUID.fromString(requestStatusDTO.getId()), RequestStatusEnum.PENDING);

        when(requestStatusRepository.save(any(RequestStatus.class))).thenReturn(requestStatus);

        requestStatusService.createRequestStatus(requestStatusDTO);

        verify(requestStatusRepository, times(1)).save(any(RequestStatus.class));
    }

    @Test
    void getRequestStatusByIdShouldReturnRequestStatus() {
        UUID id = UUID.randomUUID();
        RequestStatus requestStatus = new RequestStatus(id, RequestStatusEnum.PENDING);
        RequestStatusDTO requestStatusDTO = new RequestStatusDTO(id.toString(), RequestStatusEnum.PENDING);

        when(requestStatusRepository.findById(id)).thenReturn(Optional.of(requestStatus));
        when(requestStatusMapper.mapRequestStatusToDTO(requestStatus)).thenReturn(requestStatusDTO);

        RequestStatusDTO result = requestStatusService.getRequestStatusById(id);

        assertEquals(requestStatusDTO, result);
    }

    @Test
    void findAllRequestStatusesShouldReturnListOfRequestStatuses() {
        RequestStatus requestStatus1 = new RequestStatus(UUID.randomUUID(), RequestStatusEnum.PENDING);
        RequestStatus requestStatus2 = new RequestStatus(UUID.randomUUID(), RequestStatusEnum.APPROVED);

        List<RequestStatus> requestStatuses = List.of(requestStatus1, requestStatus2);
        when(requestStatusRepository.findAll()).thenReturn(requestStatuses);

        RequestStatusDTO requestStatusDTO1 = new RequestStatusDTO(requestStatus1.getId().toString(), requestStatus1.getStatus());
        RequestStatusDTO requestStatusDTO2 = new RequestStatusDTO(requestStatus2.getId().toString(), requestStatus2.getStatus());

        when(requestStatusMapper.mapRequestStatusToDTO(requestStatus1)).thenReturn(requestStatusDTO1);
        when(requestStatusMapper.mapRequestStatusToDTO(requestStatus2)).thenReturn(requestStatusDTO2);

        List<RequestStatusDTO> result = requestStatusService.findAllRequestStatuses();

        assertEquals(2, result.size());
        assertEquals(requestStatusDTO1, result.get(0));
        assertEquals(requestStatusDTO2, result.get(1));
    }

    @Test
    void updateRequestStatusShouldUpdateRequestStatus() {
        RequestStatusDTO requestStatusDTO = new RequestStatusDTO(UUID.randomUUID().toString(), RequestStatusEnum.APPROVED);
        RequestStatus requestStatus = new RequestStatus(UUID.fromString(requestStatusDTO.getId()), RequestStatusEnum.PENDING);

        when(requestStatusRepository.findById(UUID.fromString(requestStatusDTO.getId()))).thenReturn(Optional.of(requestStatus));

        requestStatusService.updateRequestStatus(requestStatusDTO);

        assertEquals(RequestStatusEnum.APPROVED, requestStatus.getStatus());
        verify(requestStatusRepository, times(1)).save(requestStatus);
    }

    @Test
    void deleteRequestStatusShouldDeleteRequestStatus() {
        RequestStatusDTO requestStatusDTO = new RequestStatusDTO(UUID.randomUUID().toString(), RequestStatusEnum.PENDING);

        requestStatusService.deleteRequestStatus(requestStatusDTO);

        verify(requestStatusRepository, times(1)).deleteById(UUID.fromString(requestStatusDTO.getId()));
    }

    @Test
    void getRequestStatusByIdShouldThrowEntityNotFoundException_whenRequestStatusNotFound() {
        UUID id = UUID.randomUUID();

        when(requestStatusRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> requestStatusService.getRequestStatusById(id));
    }

    @Test
    void updateRequestStatusShouldThrowEntityNotFoundException_whenRequestStatusNotFound() {
        RequestStatusDTO requestStatusDTO = new RequestStatusDTO(UUID.randomUUID().toString(), RequestStatusEnum.APPROVED);

        when(requestStatusRepository.findById(UUID.fromString(requestStatusDTO.getId()))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> requestStatusService.updateRequestStatus(requestStatusDTO));
    }
}
