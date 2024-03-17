package ua.foxminded.muzychenko.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.foxminded.muzychenko.dto.RequestTypeDTO;
import ua.foxminded.muzychenko.entity.RequestType;
import ua.foxminded.muzychenko.enums.RequestTypeEnum;
import ua.foxminded.muzychenko.repository.RequestTypeRepository;
import ua.foxminded.muzychenko.service.mapper.RequestTypeMapper;

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
    classes = RequestTypeService.class)
public class RequestTypeServiceTest {
    @MockBean
    private RequestTypeRepository requestTypeRepository;
    @MockBean
    private RequestTypeMapper requestTypeMapper;
    @Autowired
    private RequestTypeService requestTypeService;

    @Test
    void createRequestTypeShouldSaveRequestType() {
        RequestTypeDTO requestTypeDTO = new RequestTypeDTO(UUID.randomUUID().toString(), RequestTypeEnum.BECOME_STAFF);
        RequestType requestType = new RequestType(UUID.fromString(requestTypeDTO.getId()), RequestTypeEnum.BECOME_STAFF);

        when(requestTypeRepository.save(any(RequestType.class))).thenReturn(requestType);

        requestTypeService.createRequestType(requestTypeDTO);

        verify(requestTypeRepository, times(1)).save(any(RequestType.class));
    }

    @Test
    void findAllRequestTypesShouldReturnListOfRequestTypes() {
        RequestType requestType1 = new RequestType(UUID.randomUUID(), RequestTypeEnum.BECOME_STAFF);
        RequestType requestType2 = new RequestType(UUID.randomUUID(), RequestTypeEnum.BECOME_TEACHER);

        List<RequestType> requestTypes = List.of(requestType1, requestType2);
        when(requestTypeRepository.findAll()).thenReturn(requestTypes);

        RequestTypeDTO requestTypeDTO1 = new RequestTypeDTO(requestType1.getId().toString(), requestType1.getType());
        RequestTypeDTO requestTypeDTO2 = new RequestTypeDTO(requestType2.getId().toString(), requestType2.getType());

        when(requestTypeMapper.mapRequestTypeToDTO(requestType1)).thenReturn(requestTypeDTO1);
        when(requestTypeMapper.mapRequestTypeToDTO(requestType2)).thenReturn(requestTypeDTO2);

        List<RequestTypeDTO> result = requestTypeService.findAllRequestTypes();

        assertEquals(2, result.size());
        assertEquals(requestTypeDTO1, result.get(0));
        assertEquals(requestTypeDTO2, result.get(1));
    }

    @Test
    void getRequestTypeByIdShouldReturnRequestType() {
        UUID id = UUID.randomUUID();
        RequestType requestType = new RequestType(id, RequestTypeEnum.BECOME_STAFF);
        RequestTypeDTO requestTypeDTO = new RequestTypeDTO(id.toString(), RequestTypeEnum.BECOME_STAFF);

        when(requestTypeRepository.findById(id)).thenReturn(Optional.of(requestType));
        when(requestTypeMapper.mapRequestTypeToDTO(requestType)).thenReturn(requestTypeDTO);

        RequestTypeDTO result = requestTypeService.getRequestTypeById(id);

        assertEquals(requestTypeDTO, result);
    }

    @Test
    void updateRequestTypeShouldUpdateRequestType() {
        RequestTypeDTO requestTypeDTO = new RequestTypeDTO(UUID.randomUUID().toString(), RequestTypeEnum.BECOME_STAFF);
        RequestType requestType = new RequestType(UUID.fromString(requestTypeDTO.getId()), RequestTypeEnum.BECOME_STAFF);

        when(requestTypeRepository.findById(UUID.fromString(requestTypeDTO.getId()))).thenReturn(Optional.of(requestType));

        requestTypeService.updateRequestType(requestTypeDTO);

        assertEquals(RequestTypeEnum.BECOME_STAFF, requestType.getType());
        verify(requestTypeRepository, times(1)).save(requestType);
    }

    @Test
    void deleteRequestTypeShouldDeleteRequestType() {
        RequestTypeDTO requestTypeDTO = new RequestTypeDTO(UUID.randomUUID().toString(), RequestTypeEnum.BECOME_STAFF);

        requestTypeService.deleteRequestType(requestTypeDTO);

        verify(requestTypeRepository, times(1)).deleteById(UUID.fromString(requestTypeDTO.getId()));
    }

    @Test
    void getRequestTypeByIdShouldThrowEntityNotFoundException_whenRequestTypeNotFound() {
        UUID id = UUID.randomUUID();

        when(requestTypeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> requestTypeService.getRequestTypeById(id));
    }
}
