package ua.foxminded.muzychenko.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.muzychenko.entity.RequestType;
import ua.foxminded.muzychenko.enums.RequestTypeEnum;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RequestTypeRepository extends JpaRepository<RequestType, UUID> {
    Optional<RequestType> findByType(RequestTypeEnum requestTypeEnum);
}
