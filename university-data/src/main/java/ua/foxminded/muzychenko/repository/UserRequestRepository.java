package ua.foxminded.muzychenko.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.muzychenko.entity.RequestStatus;
import ua.foxminded.muzychenko.entity.RequestType;
import ua.foxminded.muzychenko.entity.UserRequest;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRequestRepository extends JpaRepository<UserRequest, UUID> {
    List<UserRequest> findByStatus(RequestStatus status);

    List<UserRequest> findByType(RequestType requestType);

}
