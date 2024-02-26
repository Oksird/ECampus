package ua.foxminded.muzychenko.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.muzychenko.entity.RequestStatus;
import ua.foxminded.muzychenko.enums.RequestStatusEnum;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RequestStatusRepository extends JpaRepository<RequestStatus, UUID> {
    Optional<RequestStatus> findByStatus(RequestStatusEnum requestStatusEnum);
}
