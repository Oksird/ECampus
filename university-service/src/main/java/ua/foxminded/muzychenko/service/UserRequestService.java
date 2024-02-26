package ua.foxminded.muzychenko.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.dto.UserRequestDTO;
import ua.foxminded.muzychenko.dto.profile.UserInfo;
import ua.foxminded.muzychenko.entity.AbstractUser;
import ua.foxminded.muzychenko.entity.UserRequest;
import ua.foxminded.muzychenko.enums.RequestStatusEnum;
import ua.foxminded.muzychenko.enums.RequestTypeEnum;
import ua.foxminded.muzychenko.exception.UserNotFoundException;
import ua.foxminded.muzychenko.repository.RequestStatusRepository;
import ua.foxminded.muzychenko.repository.RequestTypeRepository;
import ua.foxminded.muzychenko.repository.StaffRepository;
import ua.foxminded.muzychenko.repository.StudentRepository;
import ua.foxminded.muzychenko.repository.TeacherRepository;
import ua.foxminded.muzychenko.repository.UserRequestRepository;
import ua.foxminded.muzychenko.service.mapper.UserRequestMapper;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserRequestService {

    private final UserRequestRepository userRequestRepository;
    private final UserRequestMapper userRequestMapper;
    private final StudentRepository studentRepository;
    private final StaffRepository staffRepository;
    private final TeacherRepository teacherRepository;
    private final RequestTypeRepository requestTypeRepository;
    private final RequestStatusRepository requestStatusRepository;

    @Transactional
    public void createRequest(RequestTypeEnum requestTypeEnum, UserInfo userInfo) {
        AbstractUser abstractUser;

        switch (userInfo.getRole()) {
            case "STUDENT":
                abstractUser = studentRepository.findByEmail(userInfo.getEmail()).orElseThrow(UserNotFoundException::new);
                break;
            case "TEACHER":
                abstractUser = teacherRepository.findByEmail(userInfo.getEmail()).orElseThrow(UserNotFoundException::new);
            case "STAFF":
                abstractUser = staffRepository.findByEmail(userInfo.getEmail()).orElseThrow(UserNotFoundException::new);
            default:
                abstractUser = null;
        }

        UserRequest request = new UserRequest(
            UUID.randomUUID(),
            abstractUser,
            requestTypeRepository.findByType(requestTypeEnum).orElseThrow(EntityNotFoundException::new),
            requestStatusRepository.findByStatus(RequestStatusEnum.PENDING).orElseThrow(EntityNotFoundException::new)
        );

        userRequestRepository.save(request);
    }

    @Transactional
    public void approveRequest(UserRequestDTO requestDTO) {
        UserRequest request = userRequestRepository.findById(UUID.fromString(requestDTO.getId()))
            .orElseThrow(EntityNotFoundException::new);

        request.setStatus(
            requestStatusRepository.findByStatus(RequestStatusEnum.APPROVED)
                .orElseThrow(EntityNotFoundException::new)
        );

        userRequestRepository.save(request);
    }

    @Transactional
    public void rejectRequest(UserRequestDTO requestDTO) {
        UserRequest request = userRequestRepository.findById(UUID.fromString(requestDTO.getId()))
            .orElseThrow(EntityNotFoundException::new);

        request.setStatus(
            requestStatusRepository.findByStatus(RequestStatusEnum.REJECTED)
                .orElseThrow(EntityNotFoundException::new)
        );

        userRequestRepository.save(request);
    }

    @Transactional(readOnly = true)
    public Page<UserRequestDTO> findAll(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<UserRequest> userRequestPage = userRequestRepository.findAll(pageable);
        return userRequestPage.map(userRequestMapper::mapUserRequestToDTO);
    }

    @Transactional(readOnly = true)
    public List<UserRequestDTO> findAll() {
        return userRequestRepository.findAll()
            .stream()
            .map(userRequestMapper::mapUserRequestToDTO)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<UserRequestDTO> findByStatus(RequestStatusEnum status) {
        return userRequestRepository.findByStatus(requestStatusRepository.findByStatus(status).orElseThrow(EntityNotFoundException::new))
            .stream()
            .map(userRequestMapper::mapUserRequestToDTO)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<UserRequestDTO> findByType(RequestTypeEnum type) {
        return userRequestRepository.findByType(requestTypeRepository.findByType(type).orElseThrow(EntityNotFoundException::new))
            .stream()
            .map(userRequestMapper::mapUserRequestToDTO)
            .toList();
    }

    @Transactional
    public void deleteRequest(UserRequestDTO userRequestDTO) {
        userRequestRepository.deleteById(UUID.fromString(userRequestDTO.getId()));
    }
}
